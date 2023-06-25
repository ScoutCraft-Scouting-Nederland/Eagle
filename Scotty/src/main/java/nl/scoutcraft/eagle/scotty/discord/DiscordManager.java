package nl.scoutcraft.eagle.scotty.discord;

import com.velocitypowered.api.proxy.Player;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.query.QueryOptions;
import nl.scoutcraft.eagle.scotty.Scotty;
import nl.scoutcraft.eagle.scotty.config.ConfigAdaptor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DiscordManager {

    private final Scotty plugin;
    private final Map<String, String[]> tokens; // k=token, v[0]=userId, v[1]=embedId
    private final DiscordListener listener;

    private final List<Runnable> readyActions;

    private JDA api;
    private Guild guild;
    private Role verified;
    private final List<Role> rankRoles;

    public DiscordManager(Scotty plugin) {
        this.plugin = plugin;
        this.tokens = new HashMap<>();
        this.rankRoles = new ArrayList<>();
        this.listener = new DiscordListener(this);
        this.readyActions = new ArrayList<>();
    }

    public void start() {
        ConfigAdaptor config = Scotty.getInstance().getConfig();

        try {
            this.api = JDABuilder.createDefault(config.getNode("token").getString("")).build();
            this.api.setAutoReconnect(true);
            this.api.addEventListener(this.listener);
            this.setPlayerCount();
        } catch (Exception exception) {
            this.plugin.getLogger().error("Failed to start discord bot!", exception);
        }
    }

    public void close() {
        this.tokens.values().forEach(values -> this.editMessage(values[0], values[1], new Embed(EmbedTypes.EXPIRED)));
        this.tokens.clear();

        if (this.api != null)
            this.api.shutdown();
    }

    public boolean isReady() {
        return this.guild != null;
    }

    public void addReadyAction(Runnable action) {
        this.readyActions.add(action);
    }

    public void onReady() {
        if (this.api == null) return;

        ConfigAdaptor config = this.plugin.getConfig();

        this.guild = this.api.getGuildById(config.getNode("guildId").getString(""));
        if (this.guild == null) return;

        this.verified = this.guild.getRoleById(config.getNode("verified-role-id").getString(""));
        try {
            config.getNode("rank-roles").getList(String.class, Collections.emptyList()).forEach(r -> this.rankRoles.add(this.getRole(r)));
        } catch (SerializationException exc) {
            Scotty.getInstance().getLogger().error("Failed to load discord ranks from config!", exc);
        }

        this.readyActions.forEach(Runnable::run);
        this.readyActions.clear();
    }

    public void onJoin(Player player, String userId) {
        if (this.api == null) return;
        this.setPlayerCount();
        this.updateDiscordNameAndRoles(player, userId);
    }

    public void onQuit(UUID playerId) {
        if (this.api == null) return;
        this.setPlayerCount();
    }

    public void registerChecker(IDiscordRegisterChecker checker) {
       this.listener.registerChecker(checker);
    }

    public void sendMessage(String userId, String message, Consumer<String> idAction) {
        this.executeWithMember(userId, mem -> mem.getUser().openPrivateChannel().queue(c -> c.sendMessage(message).queue(m -> idAction.accept(m.getId()))));
    }

    public void sendMessage(String userId, Embed embed, Consumer<String> idAction) {
        this.executeWithMember(userId, mem -> mem.getUser().openPrivateChannel().queue(c -> c.sendMessageEmbeds(embed.type().build(embed.params())).queue(m -> idAction.accept(m.getId()))));
    }

    public void editMessage(String userId, String messageId, String message) {
        this.executeWithMember(userId, mem -> mem.getUser().openPrivateChannel().queue(c -> c.editMessageById(messageId, message).queue()));
    }

    public void editMessage(String userId, String embedId, Embed embed) {
        this.executeWithMember(userId, mem -> mem.getUser().openPrivateChannel().queue(c -> c.editMessageEmbedsById(embedId, embed.type().build(embed.params())).queue()));
    }

    public void sendChannelMessage(DiscordChannel channel, String message, Consumer<String> idAction) {
        Optional.ofNullable(this.guild.getTextChannelById(channel.getChannelId())).ifPresent(c -> c.sendMessage(message).queue(m -> idAction.accept(m.getId())));
    }

    public void sendChannelMessage(DiscordChannel channel, Embed embed, Consumer<String> idAction) {
        Optional.ofNullable(this.guild.getTextChannelById(channel.getChannelId())).ifPresent(c -> c.sendMessageEmbeds(embed.type().build(embed.params())).queue(m -> idAction.accept(m.getId())));
    }

    public void editChannelMessage(DiscordChannel channel, String messageId, String message) {
        Optional.ofNullable(this.guild.getTextChannelById(channel.getChannelId())).ifPresent(c -> c.editMessageById(messageId, message).queue());
    }

    public void editChannelMessage(DiscordChannel channel, String embedId, Embed embed) {
        Optional.ofNullable(this.guild.getTextChannelById(channel.getChannelId())).ifPresent(c -> c.editMessageEmbedsById(embedId, embed.type().build(embed.params())).queue());
    }

    public void updateDiscordNameAndRoles(Player player, String discordId) {
        if (discordId == null)
            return;

        this.executeWithMember(discordId, member -> {
            try {
                member.getGuild().modifyNickname(member, player.getUsername()).queue();

                if (this.verified != null && !member.getRoles().contains(this.verified))
                    member.getGuild().addRoleToMember(member, this.verified).queue();

                List<Role> ranks = LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId()).getNodes(NodeType.INHERITANCE).stream()
                        .filter(Node::getValue)
                        .map(node -> this.getRole(node.getGroupName()))
                        .filter(Objects::nonNull)
                        .filter(this.rankRoles::contains)
                        .collect(Collectors.toList());

                if (ranks.isEmpty())
                    return;

                List<Role> memberRoles = member.getRoles();
                memberRoles.stream()
                        .filter(r -> !ranks.contains(r) && this.rankRoles.contains(r))
                        .forEach(r -> this.guild.removeRoleFromMember(member, r).queue());
                ranks.stream()
                        .filter(rank -> !memberRoles.contains(rank))
                        .forEach(rank -> member.getGuild().addRoleToMember(member, rank).queue());
            } catch (PermissionException ignored) {
            }
        });
    }

    public void register(String token, User user, String embedId) {
        this.tokens.put(token, new String[]{user.getId(), embedId});

        Scotty.getProxy().getScheduler()
                .buildTask(this.plugin, () -> {
                    if (!this.tokens.containsKey(token)) return;

                    user.openPrivateChannel().queue(channel -> channel.editMessageEmbedsById(embedId, EmbedTypes.EXPIRED.build()).queue());
                    this.tokens.remove(token);
                })
                .delay(5, TimeUnit.MINUTES)
                .schedule();
    }

    @Nullable
    public String[] removeToken(String token) {
        return this.tokens.remove(token);
    }

    @Nullable
    private Role getRole(String name) {
        for (Role role : this.guild.getRoles())
            if (role.getName().equalsIgnoreCase(name))
                return role;
        return null;
    }

    private void executeWithMember(String userId, Consumer<Member> action) {
        if (userId == null || action == null || this.guild == null) return;

        try {
            this.guild.retrieveMemberById(userId).queue(action);
        } catch (ErrorResponseException exc) {
        }
    }

    private void setActivity(Activity activity) {
        if (this.api != null)
            this.api.getPresence().setActivity(activity);
    }

    private void setPlayerCount() {
        int count = Scotty.getProxy().getPlayerCount();
        this.setActivity(Activity.watching(count + " speler" + (count != 1 ? "s" : "") + " online!"));
    }
}
