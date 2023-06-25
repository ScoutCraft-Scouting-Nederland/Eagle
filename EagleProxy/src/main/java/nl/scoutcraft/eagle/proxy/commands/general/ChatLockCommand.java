package nl.scoutcraft.eagle.proxy.commands.general;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.chat.ChatManager;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandBuilder;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandContext;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommand;
import nl.scoutcraft.eagle.proxy.commands.lib.IPlayerCommandExecutor;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.commands.lib.exc.MessageException;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.IPlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.MessagePlaceholder;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;

public class ChatLockCommand implements ICommand {

    private final ChatManager chatManager = EagleProxy.getInstance().getChatManager();

    @Override
    public CommandBuilder build() {
        return ICommand.builder("chatlock", "lockchat")
                .permission(Perms.CHAT_LOCK)
                .usage("/chatlock {status|all|current|<server>}")
//                .executor("status")
                .child(ICommand.node(Args.path("status", "state").optional()).permission(Perms.CHAT_LOCK_STATUS).executor(this::sendStatus))
                .child(ICommand.node("all", "global").permission(Perms.CHAT_LOCK_GLOBAL).executor(this::toggleGlobal))
                .child(ICommand.node("current", "local").permission(Perms.CHAT_LOCK_SERVER).executor((IPlayerCommandExecutor) (p, c) -> this.toggleLock(p, p.getCurrentServer().get().getServer())))
                .child(ICommand.node(Args.server("target")).permission(Perms.CHAT_LOCK_SERVER).executor(this::toggleLock));
    }

    private void sendStatus(CommandSource sender, CommandContext context) {
        sender.sendMessage(TextUtils.line(79));
        CommandMessages.CHATLOCK_STATUS_GLOBAL.send(sender, new MessagePlaceholder("%state%", this.chatManager.isGlobalChatLock() ? CommandMessages.DISABLED : CommandMessages.ENABLED));
        EagleProxy.getProxy().getAllServers().forEach(server -> CommandMessages.CHATLOCK_STATUS_SERVER.send(sender, new Placeholder("%server%", server.getServerInfo().getName()), new MessagePlaceholder("%state%", this.chatManager.isChatLocked(server.getServerInfo().getName()) ? CommandMessages.DISABLED : CommandMessages.ENABLED)));
        sender.sendMessage(TextUtils.line(79));
    }

    private void toggleGlobal(CommandSource sender, CommandContext context) {
        if (this.chatManager.isGlobalChatLock()) {
            this.chatManager.disableGlobalChatLock();
            IPlaceholder placeholder = new Placeholder("%unlocker%", sender instanceof Player player ? player.getUsername() : "Console");
            EagleProxy.getProxy().getAllPlayers().forEach(p -> CommandMessages.CHATLOCK_UNLOCK.send(p, placeholder));
            CommandMessages.CHATLOCK_YOU_UNLOCKED.send(sender, new Placeholder("%target%", "global"));
        } else {
            this.chatManager.enableGlobalChatLock();
            IPlaceholder placeholder = new Placeholder("%locker%", sender instanceof Player player ? player.getUsername() : "Console");
            EagleProxy.getProxy().getAllPlayers().forEach(p -> CommandMessages.CHATLOCK_LOCK.send(p, placeholder));
            CommandMessages.CHATLOCK_YOU_LOCKED.send(sender, new Placeholder("%target%", "global"));
        }
    }

    private void toggleLock(CommandSource sender, CommandContext context) throws MessageException {
        this.toggleLock(sender, context.<RegisteredServer>get("target").orElseThrow(() -> new MessageException(CommandMessages.GENERAL_INVALID_SERVER, new Placeholder("%server%", context.getRaw("target")))));
    }

    private void toggleLock(CommandSource sender, RegisteredServer server) {
        String serverName = server.getServerInfo().getName();
        String senderName = sender instanceof Player player ? player.getUsername() : "Console";

        if (this.chatManager.isChatLocked(serverName)) {
            this.chatManager.disableChatlock(serverName);
            IPlaceholder placeholder = new Placeholder("%unlocker%", senderName);
            server.getPlayersConnected().forEach(p -> CommandMessages.CHATLOCK_UNLOCK.send(p, placeholder));
            CommandMessages.CHATLOCK_YOU_UNLOCKED.send(sender, new Placeholder("%target%", serverName));
        } else {
            this.chatManager.enableChatLock(serverName);
            IPlaceholder placeholder = new Placeholder("%locker%", senderName);
            server.getPlayersConnected().forEach(p -> CommandMessages.CHATLOCK_LOCK.send(p, placeholder));
            CommandMessages.CHATLOCK_YOU_LOCKED.send(sender, new Placeholder("%target%", serverName));
        }
    }
}
