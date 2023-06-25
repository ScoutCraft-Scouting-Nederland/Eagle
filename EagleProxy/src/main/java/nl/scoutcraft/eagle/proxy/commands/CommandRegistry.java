package nl.scoutcraft.eagle.proxy.commands;

import com.velocitypowered.api.command.CommandManager;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.commands.admin.EagleCommand;
import nl.scoutcraft.eagle.proxy.commands.admin.MaintenanceCommand;
import nl.scoutcraft.eagle.proxy.commands.admin.ServerManagerCommand;
import nl.scoutcraft.eagle.proxy.commands.admin.TeamChatCommand;
import nl.scoutcraft.eagle.proxy.commands.general.*;
import nl.scoutcraft.eagle.proxy.commands.message.MessageCommand;
import nl.scoutcraft.eagle.proxy.commands.message.ReplyCommand;
import nl.scoutcraft.eagle.proxy.commands.message.SocialSpyCommand;
import nl.scoutcraft.eagle.proxy.commands.party.PartyCommand;

public final class CommandRegistry {

    public static void register(CommandManager cm, EagleProxy plugin) {
        new EagleCommand().register(cm, plugin);
        new MaintenanceCommand().register(cm, plugin);
        new ServerManagerCommand().register(cm, plugin);
        new TeamChatCommand().register(cm, plugin);

        new AnnounceCommand().register(cm, plugin);
        new BugCommand().register(cm, plugin);
        new BugsCommand().register(cm, plugin);
        new ChatLockCommand().register(cm, plugin);
        new ChatClearCommand().register(cm, plugin);
        new DiscordCommand().register(cm, plugin);
        new FindCommand().register(cm, plugin);
        new GListCommand().register(cm, plugin);
        new GTPCommand().register(cm, plugin);
        new GTPHereCommand().register(cm, plugin);
        new HelpopCommand().register(cm, plugin);
        new HelpopsCommand().register(cm, plugin);
        new IgnoreCommand().register(cm, plugin);
        new LangCommand().register(cm, plugin);
        new LobbyCommand(plugin).register(cm, plugin);
        new LookupCommand(plugin).register(cm, plugin);
        new NickCommand().register(cm, plugin);
        new PingCommand().register(cm, plugin);
        new PlaytimeCommand(plugin).register(cm, plugin);
        new RealnameCommand().register(cm, plugin);
        new RegisterCommand(plugin).register(cm, plugin);
        new ReportCommand().register(cm, plugin);
        new ReportsCommand().register(cm, plugin);
        new SeenCommand(plugin).register(cm, plugin);
        new SendCommand().register(cm, plugin);
        new ServerCommand().register(cm, plugin);
        new UnignoreCommand(plugin).register(cm, plugin);
        new UnregisterCommand(plugin).register(cm, plugin);
        new WhereamiCommand().register(cm, plugin);

        new MessageCommand().register(cm, plugin);
        new ReplyCommand().register(cm, plugin);
        new SocialSpyCommand().register(cm, plugin);

        cm.register(cm.metaBuilder("party").build(), new PartyCommand(plugin));
    }

    private CommandRegistry() {
    }

    /*

    # Vanish (LATER)
    ## Remove from tabcompleters + commands (msg, ignore, seen?, realname, playtime?, ping?, find?)

    # DONE
    # ServerManager -> Al eerder gemaakt door Franck, so he'll do that.

    # DONE
    # TP to player on other server -> /gtp

    # DONE
    # Combine the 2 pingtasks

    # DONE
    # AntiAD(vertisement)

    # DONE
    # AntiCaps

    # DONE
    # AntiSpam

    # Filter on NickNames... (COULD)
    ## list of not possible names.

    # DONE
    # DISCORDMODULE
    ## /register possibility - sync mc with dc.
    ## send helpops & reports to channel in dc.
    ## send entire chatlog to dc.

    # DONE
    # HelpOP -> /helpop {message}
    # Possibility to send a helpop.
    # Insert all needed data.

    # DONE
    # Reports -> /reports list & /reports accept {id} & /reports close {id}
    # Message to players with perm on join with how many reports are open.
    ## LIST:            %id% by %reporter% to %reportee% for %report% on %time% %date%.
    ## ACCEPT:          To acknowledge a report (you will have to handle it)
    ## CLOSE:           To close ID.

    # DONE
    # Dynamic MOTD
    ## Possibility to rotate through multiple MOTD's, or only one if there's only one.

    # DONE (when server restarts/stops)
    # Kickmove
    ## To send players to lobby when they're getting kicked.

    # DONE
    # PlayTime -> /playtime & /playtime {player|top}
    ## Schedule db update

    # DONE
    # Scheduled Announcements.
    ## In order, alleen chat

    # DONE
    # TeamChat
    ## Command to toggle on/off
    ## Usage: '@' and after that your message, it will be broadcasted to all people with the right perms.

    # DONE
    # Language -> /lang
    # Save to DB, load from DB

    # DONE
    # Lobby
    ## Force players on join to join one of the available lobby's, use loadbalancer for this.
    ## also when using /lobby send them to one of the available lobby's.

    # DONE
    # SeenCommand -> modular (time, only taking whats needed > months, weeks, days, hours, minutes, seconds)
    ##  when online:    "Player %player% is Online since %minutes% minutes %seconds% seconds."
    ##  when offline:   "Player %player% is Offline since %months% months %weeks% weeks %days% days %hours% hours %minutes% minutes %seconds% seconds."

    # DONE
    # Ping -> /ping & /ping {player}
    # Get ping of player to server.

    # DONE
    # Maintenance -> /maintenance {on|off|status}
    # When enabled only allow players with permission to join network.

    # DONE
    # Ignore -> /ignore {player}
    # Ignore another player's chat & msg's.
    # Disallow to ignore players with perm (like staff).

    # DONE
    # ChatLock -> /chatlock {status|current|server|all}
    # Lock all chat messages except for people with permission.
    # Status shows all active chatlocks
    #   Global lock active: true|false
    #   Server locks: TentRed, TentGreen

    # DONE
    # Announce -> /announce {current|server|all} {message}
    # Announce message across ScoutCraft.

     */
}
