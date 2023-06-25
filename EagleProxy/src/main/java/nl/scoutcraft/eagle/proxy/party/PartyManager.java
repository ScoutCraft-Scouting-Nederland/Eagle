package nl.scoutcraft.eagle.proxy.party;

import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import nl.scoutcraft.eagle.libs.network.NetworkChannel;
import nl.scoutcraft.eagle.libs.network.channels.IPartyChannel;
import nl.scoutcraft.eagle.libs.party.PartyInfo;
import nl.scoutcraft.eagle.libs.party.PartyInvitation;
import nl.scoutcraft.eagle.libs.party.PartyMemberInfo;
import nl.scoutcraft.eagle.libs.utils.Colors;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.party.PartyCommand;
import nl.scoutcraft.eagle.proxy.commands.party.PartySubCommand;
import nl.scoutcraft.eagle.proxy.io.ProxyNetworkChannel;
import nl.scoutcraft.eagle.proxy.locale.PartyMessages;
import nl.scoutcraft.eagle.proxy.locale.api.ComponentPlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.MessagePlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.player.obj.ScoutPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class PartyManager implements IPartyChannel {

    private final EagleProxy plugin;
    private final NetworkChannel<IPartyChannel> channel;

    private final Map<UUID, Party> parties;

    public PartyManager(EagleProxy plugin) {
        this.plugin = plugin;
        this.channel = new ProxyNetworkChannel<>("eagle:party", IPartyChannel.class, this, plugin);
        this.parties = new HashMap<>();
    }

    @Nullable
    public Party getParty(UUID playerId) {
        return this.parties.get(playerId);
    }

    @Nullable
    public Party getParty(Player player) {
        return this.parties.get(player.getUniqueId());
    }

    public Party createParty(Player player) {
        Party party = getParty(player);
        if (party != null)
            return party;

        party = new Party(player);
        this.parties.put(player.getUniqueId(), party);
        this.channel.request().setTarget(player).onPartyCreate(this.getPartyInfo(party));
        return party;
    }

    public void deleteParty(Party party) {
        if (!party.getMembers().isEmpty()) {
            party.getMembers().forEach(PartyMessages.PARTY_DISSOLVED::send);
            party.getMembers().clear();
        }

        EagleProxy.getProxy().getAllServers().forEach(s -> this.channel.request().setTarget(s).onPartyDisband(party.getId()));

        while (this.parties.values().remove(party));
    }

    public boolean addMember(ScoutPlayer member, Party party) {
        this.parties.put(member.getPlayerId(), party);
        return party.addMember(member);
    }

    public boolean removeMember(Player member, Party party) {
        this.parties.remove(member.getUniqueId());
        return party.removeMember(member);
    }

    public boolean removeMember(UUID uuid, String username, Party party) {
        this.parties.remove(uuid);
        return party.removeMember(uuid, username);
    }

    public boolean kickMember(Player member, Party party) {
        this.parties.remove(member.getUniqueId());
        return party.kickMember(member);
    }

    public void openMenu(Player player) {
        Party party = this.getParty(player);
        PartyInfo info = party == null ? null : this.getPartyInfo(party);
        this.channel.request()
                .setTimeout(175)
                .onTimeout(() -> this.sendChatMenu(player))
                .setTarget(player)
                .openMenu(player.getUniqueId(), info);
    }

    public void sendChatMenu(Player player) {
        player.sendMessage(TextUtils.text(TextUtils.line(31)).append(Component.text(" Party Help ", Colors.GOLD)).append(TextUtils.line(31)));

        PartyCommand.getCommandList().forEach(subCommand -> {
            String command = "/party " + subCommand.getCommandName() + (!subCommand.getUsage().isEmpty() ? " " : "") + subCommand.getUsage();
            Component comp = Component.text(command)
                    .hoverEvent(HoverEvent.showText(PartyMessages.PARTY_INFO_COMMAND_HOVER.get(player)))
                    .clickEvent(ClickEvent.suggestCommand(command));
            PartyMessages.PARTY_INFO_FORMAT.send(player, new ComponentPlaceholder("%command%", comp), new MessagePlaceholder("%desc%", subCommand.getMessage()));
        });

        player.sendMessage(TextUtils.line(79));
    }

    public void onQuit(UUID uuid, String username) {
        Party party = this.getParty(uuid);
        if (party == null) return;

        if (party.isLeader(uuid)) {
            if (!party.newLeader()) {
                this.deleteParty(party);
                return;
            }
            if (!party.getMembers().isEmpty())
                party.getMembers().forEach(member -> {
                    PartyMessages.LEADER_LEFT.send(member, new Placeholder("%oldLeader%", username));
                    PartyMessages.NEW_LEADER.send(member, new Placeholder("%newLeader%", party.getLeader().getUsername()));
                });
            PartyMessages.LEADER_LEFT.send(party.getLeader(), new Placeholder("%oldLeader%", username));
            PartyMessages.HAVE_PROMOTED.send(party.getLeader());
            return;
        }

        this.removeMember(uuid, username, party);
    }

    private PartyInfo getPartyInfo(Party party) {
        if (party == null) return null;

        List<UUID> members = new ArrayList<>();
        party.getMembers().forEach(mem -> members.add(mem.getUniqueId()));

        return new PartyInfo(party.getId(), party.isPublicParty(), party.getLeader().getUniqueId(), members);
    }

    @Override
    public UUID getPartyId(UUID playerId) {
        return EagleProxy.getProxy().getPlayer(playerId).map(this::getParty).map(Party::getId).orElse(null);
    }

    @Override
    public PartyInfo getPartyInfo(UUID playerId) {
        return this.getPartyInfo(this.getParty(playerId));
    }

    @Override
    public List<PartyMemberInfo> getMembers(UUID playerId) {
        Party party = this.getParty(playerId);
        if (party == null)
            return Collections.emptyList();

        return party.getMembers().stream().map(member -> new PartyMemberInfo(member.getUniqueId(), member.getUsername(), this.plugin.getPlayerManager().getScoutPlayer(member).getTextureProperty().orElse(null))).collect(Collectors.toList());
    }

    @Override
    public List<PartyInvitation> getInvites(UUID playerId) {
        ScoutPlayer scoutPlayer = this.plugin.getPlayerManager().getScoutPlayer(playerId);
        if (scoutPlayer == null)
            return Collections.emptyList();

        return scoutPlayer.getPartyInvites().stream().map(invite -> new PartyInvitation(invite.getId(), invite.getLeader().getUsername(), this.plugin.getPlayerManager().getScoutPlayer(invite.getLeader()).getTextureProperty().orElse(null))).collect(Collectors.toList());
    }
}
