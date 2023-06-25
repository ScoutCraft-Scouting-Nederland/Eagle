package nl.scoutcraft.eagle.server.listener;

import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import nl.scoutcraft.eagle.libs.utils.Colors;
import nl.scoutcraft.eagle.server.EagleServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TablistListener implements Listener {

    private final Scoreboard board;

    public TablistListener() {
        this.board = Bukkit.getScoreboardManager().getNewScoreboard();
        Team staff = board.registerNewTeam("a_staff");
        Team leiding = board.registerNewTeam("b_leiding");
        Team bleiding = board.registerNewTeam("c_bleiding");
        Team developer = board.registerNewTeam("d_developer");
        Team avonturier = board.registerNewTeam("e_avonturier");
        Team vormgever = board.registerNewTeam("f_vormgever");
        Team bouwer = board.registerNewTeam("g_bouwer");
        Team opkomst = board.registerNewTeam("h_opkomst");
        Team scout = board.registerNewTeam("i_scout");
        Team speler = board.registerNewTeam("j_speler");
        Team gast = board.registerNewTeam("k_gast");

        staff.prefix(Component.text(" ", Colors.RLEIDING));
        leiding.prefix(Component.text(" ", Colors.GLEIDING));
        bleiding.prefix(Component.text(" ", Colors.BOUWER));
        developer.prefix(Component.text(" ", Colors.DEVELOPER));
        avonturier.prefix(Component.text(" ", Colors.AVONTURIER));
        vormgever.prefix(Component.text(" ", Colors.VORMGEVER));
        bouwer.prefix(Component.text(" ", Colors.BOUWER));
        opkomst.prefix(Component.text(" ", Colors.OPKOMST));
        scout.prefix(Component.text(" ", Colors.SCOUT));
        speler.prefix(Component.text(" ", Colors.SPELER));
        gast.prefix(Component.text(" ", Colors.GAST));

        Bukkit.getServer().getServicesManager().load(LuckPerms.class).getEventBus().subscribe(EagleServer.getInstance(), UserDataRecalculateEvent.class, this::onLuckPing);
    }

    private void onLuckPing(UserDataRecalculateEvent event) {
        Player player = Bukkit.getPlayer(event.getUser().getUniqueId());
        if (player == null || !player.isOnline())
            return;

        synchronized (this.board) {
            this.removePlayer(player);
            this.updatePlayer(player);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        synchronized (this.board) {
            this.updatePlayer(event.getPlayer());
        }
    }

    private void updatePlayer(Player player) {
        String team = "k_gast";

        if (hasGroupPermission(player, "staff")) team = "a_staff";
        else if (hasGroupPermission(player, "leiding")) team = "b_leiding";
        else if (hasGroupPermission(player, "builder.leiding")) team = "c_bleiding";
        else if (hasGroupPermission(player, "developer")) team = "d_developer";
        else if (hasGroupPermission(player, "avonturier")) team = "e_avonturier";
        else if (hasGroupPermission(player, "vormgever")) team = "f_vormgever";
        else if (hasGroupPermission(player, "builder")) team = "g_bouwer";
        else if (hasGroupPermission(player, "opkomst")) team = "h_opkomst";
        else if (hasGroupPermission(player, "scout")) team = "i_scout";
        else if (hasGroupPermission(player, "player")) team = "j_speler";

        this.board.getTeam(team).addEntry(player.getName());
        player.playerListName(Component.text(player.getName(), this.board.getTeam(team).prefix().color()));
        player.setScoreboard(this.board);
    }

    private void removePlayer(Player player) {
        Team team = this.board.getEntryTeam(player.getName());
        if (team != null)
            team.removeEntry(player.getName());
    }

    private boolean hasGroupPermission(Player player, String group) {
        return player.hasPermission("scoutcraft.group." + group);
    }
}
