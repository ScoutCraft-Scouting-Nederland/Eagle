package nl.scoutcraft.eagle.proxy.player.tasks;

import com.velocitypowered.api.proxy.Player;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.io.DatabaseManager;
import nl.scoutcraft.eagle.proxy.locale.GeneralMessages;
import nl.scoutcraft.eagle.proxy.locale.api.IMessage;
import nl.scoutcraft.eagle.proxy.locale.api.Placeholder;
import nl.scoutcraft.eagle.proxy.utils.Task;

import java.util.concurrent.TimeUnit;

public class StaffAnnouncerTask extends Task {

    private static final DatabaseManager SQL_MANAGER = EagleProxy.getInstance().getSQLManager();

    public StaffAnnouncerTask(EagleProxy plugin) {
        super(plugin.getConfigAdaptor().getNode("staffannouncer_interval_seconds").getInt(300), TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        int openHelpops = SQL_MANAGER.getUnassignedHelpopCount();
        int openReports = SQL_MANAGER.getUnassignedReportCount();
        int openBugs = SQL_MANAGER.getUnassignedBugCount();

        for (Player player : EagleProxy.getProxy().getAllPlayers())
            sendInfo(player, openHelpops, openReports, openBugs);
    }

    public static void sendInfo(Player player) {
        if (player.isActive())
            sendInfo(player, SQL_MANAGER.getUnassignedHelpopCount(), SQL_MANAGER.getUnassignedReportCount(), SQL_MANAGER.getUnassignedBugCount());
    }

    private static void sendInfo(Player player, int openHelpops, int openReports, int openBugs) {
        boolean hasHelpopsPerm = player.hasPermission(Perms.HELPOPS_ANNOUNCEMENT);
        boolean hasReportsPerm = player.hasPermission(Perms.REPORTS_ANNOUNCEMENT);
        boolean hasBugsPerm = player.hasPermission(Perms.BUGS_ANNOUNCEMENT);

        int assignedHelpops = SQL_MANAGER.getAssignedHelpopCount(player.getUniqueId());
        int assignedReports = SQL_MANAGER.getAssignedReportCount(player.getUniqueId());
        int assignedBugs = SQL_MANAGER.getAssignedBugCount(player.getUniqueId());

        if ((hasHelpopsPerm && (openHelpops > 0 || assignedHelpops > 0)) || (hasReportsPerm && (openReports > 0 || assignedReports > 0)) || (hasBugsPerm && (openBugs > 0 || assignedBugs > 0))) {
            player.sendMessage(TextUtils.line(79));
            if (hasHelpopsPerm) sendHelpopsInfo(player, openHelpops, assignedHelpops);
            if (hasReportsPerm) sendReportsInfo(player, openReports, assignedReports);
            if (hasBugsPerm) sendBugsInfo(player, openBugs, assignedBugs);
            player.sendMessage(TextUtils.line(79));
        }
    }

    private static void sendHelpopsInfo(Player player, int openHelpops, int assignedHelpops) {
        if (openHelpops != 0)
            sendMessage(player, GeneralMessages.HELPOPS_OPEN, GeneralMessages.HELPOPS_OPEN_ONE, openHelpops);
        if (assignedHelpops != 0)
            sendMessage(player, GeneralMessages.HELPOPS_ACCEPTED, GeneralMessages.HELPOPS_ACCEPTED_ONE, assignedHelpops);
    }

    private static void sendReportsInfo(Player player, int openReports, int assignedReports) {
        if (openReports != 0)
            sendMessage(player, GeneralMessages.REPORTS_OPEN, GeneralMessages.REPORTS_OPEN_ONE, openReports);
        if (assignedReports != 0)
            sendMessage(player, GeneralMessages.REPORTS_ACCEPTED, GeneralMessages.REPORTS_ACCEPTED_ONE, assignedReports);
    }

    private static void sendBugsInfo(Player player, int openBugs, int assignedBugs) {
        if (openBugs != 0) sendMessage(player, GeneralMessages.BUGS_OPEN, GeneralMessages.BUGS_OPEN_ONE, openBugs);
        if (assignedBugs != 0)
            sendMessage(player, GeneralMessages.BUGS_ACCEPTED, GeneralMessages.BUGS_ACCEPTED_ONE, assignedBugs);
    }

    private static void sendMessage(Player player, IMessage normal, IMessage singular, int count) {
        if (count == 1) singular.send(player);
        else normal.send(player, new Placeholder("%count%", Integer.toString(count)));
    }
}
