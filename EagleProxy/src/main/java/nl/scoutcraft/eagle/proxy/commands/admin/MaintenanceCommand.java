package nl.scoutcraft.eagle.proxy.commands.admin;

import com.velocitypowered.api.command.CommandSource;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.lib.CommandBuilder;
import nl.scoutcraft.eagle.proxy.commands.lib.ICommand;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Args;
import nl.scoutcraft.eagle.proxy.locale.CommandMessages;
import nl.scoutcraft.eagle.proxy.locale.api.MessagePlaceholder;

public class MaintenanceCommand implements ICommand {

    private static final EagleProxy PLUGIN = EagleProxy.getInstance();

    @Override
    public CommandBuilder build() {
        return ICommand.builder("maintenance")
                .permission(Perms.MAINTENANCE)
                .usage("/maintenance {on|off|status}")
//                .executor("status")
                .child(ICommand.node(Args.path("status", "state").optional()).executor((s, c) -> CommandMessages.MAINTENANCE_STATUS.send(s, new MessagePlaceholder("%state%", PLUGIN.getProxyManager().isMaintenanceMode() ? CommandMessages.ENABLED : CommandMessages.DISABLED))))
                .child((s, c) -> this.setMaintenanceMode(s, true), "on")
                .child((s, c) -> this.setMaintenanceMode(s, false), "off");
    }

    private void setMaintenanceMode(CommandSource sender, boolean value) {
        if (PLUGIN.getProxyManager().isMaintenanceMode() == value) {
            CommandMessages.MAINTENANCE_ALREADY_SET.send(sender, new MessagePlaceholder("%state%", value ? CommandMessages.ENABLED : CommandMessages.DISABLED));
            return;
        }

        PLUGIN.getProxyManager().setMaintenanceMode(value);
    }
}
