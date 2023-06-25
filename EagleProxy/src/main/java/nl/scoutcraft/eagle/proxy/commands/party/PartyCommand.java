package nl.scoutcraft.eagle.proxy.commands.party;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.party.sub.*;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.PartyMessages;
import nl.scoutcraft.eagle.proxy.party.Party;
import nl.scoutcraft.eagle.proxy.party.PartyManager;
import nl.scoutcraft.eagle.proxy.utils.CommandUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PartyCommand implements SimpleCommand {

    private static final List<PartySubCommand> COMMAND_LIST = new ArrayList<>();
    private static final List<String> COMMANDS = new ArrayList<>();

    private final PartyManager partyManager;

    public PartyCommand(EagleProxy plugin) {
        this.partyManager = plugin.getPartyManager();

        COMMAND_LIST.add(new Chat(this.partyManager));
        COMMAND_LIST.add(new Create(this.partyManager));
        COMMAND_LIST.add(new Disband(this.partyManager));
        COMMAND_LIST.add(new Info(this.partyManager));
        COMMAND_LIST.add(new Help());
        COMMAND_LIST.add(new Invite(this.partyManager));
        COMMAND_LIST.add(new Join(this.partyManager));
        COMMAND_LIST.add(new Kick(this.partyManager));
        COMMAND_LIST.add(new Leave(this.partyManager));
        COMMAND_LIST.add(new Private(this.partyManager));
        COMMAND_LIST.add(new Public(this.partyManager));
        COMMAND_LIST.add(new Teleport(this.partyManager));
        COMMAND_LIST.add(new Transfer(this.partyManager));
        COMMAND_LIST.add(new Warp(this.partyManager));
        for (PartySubCommand subCommand : COMMAND_LIST) {
            Collections.addAll(COMMANDS, subCommand.getCommands());
        }
    }

    public static List<PartySubCommand> getCommandList() {
        return COMMAND_LIST;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        if (!(sender instanceof Player player)) {
            CommandMessages.NO_CONSOLE.send(sender);
            return;
        }

        if (args.length == 0) {
            this.partyManager.openMenu(player);
            return;
        }

        PartySubCommand subCommand = getCommand(args[0]);

        if (subCommand == null) {
            PartyMessages.NO_COMMAND.send(player);
            return;
        }

        if (!sender.hasPermission(subCommand.getPermission())) {
            CommandMessages.NO_PERMISSION.send(sender);
            return;
        }

        subCommand.onCommand(player, TextUtils.subarray(1, args));
    }

    @Nullable
    private PartySubCommand getCommand(String name) {
        for (PartySubCommand subCommand : COMMAND_LIST) {
            if (subCommand.getClass().getSimpleName().equalsIgnoreCase(name) || subCommand.matches(name))
                return subCommand;
        }

        return null;
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        if (!(sender instanceof Player player) || args.length == 0)
            return new ArrayList<>();

        if (args.length == 1)
            return CommandUtils.copyPartialMatches(args[0], COMMANDS, new ArrayList<>());

        if (args.length == 2) {
            List<String> arguments = new ArrayList<>();
            if (args[0].equalsIgnoreCase("invite")) {
                for (Player online : EagleProxy.getProxy().getAllPlayers()) {
                    arguments.add(online.getUsername());
                }
            } else if (args[0].equalsIgnoreCase("join")) {
                List<Party> invites = EagleProxy.getInstance().getPlayerManager().getScoutPlayer(player).getPartyInvites();
                if (!invites.isEmpty()) {
                    for (Party party : invites)
                        arguments.add(party.getLeader().getUsername());
                } else {
                    for (Player online : EagleProxy.getProxy().getAllPlayers())
                        arguments.add(online.getUsername());
                }
            } else if (args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("transfer")) {
                Party party = this.partyManager.getParty(player);
                if (party != null)
                    arguments = party.getMembers().stream().map(Player::getUsername).collect(Collectors.toList());
            }
            return CommandUtils.copyPartialMatches(args[1], arguments, new ArrayList<>());
        }

        return new ArrayList<>();
    }
}
