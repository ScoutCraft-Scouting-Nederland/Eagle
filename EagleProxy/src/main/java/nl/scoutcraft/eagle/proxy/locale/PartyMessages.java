package nl.scoutcraft.eagle.proxy.locale;

import nl.scoutcraft.eagle.libs.locale.Internationalization;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.locale.api.CompoundMessage;
import nl.scoutcraft.eagle.proxy.locale.api.IMessage;
import nl.scoutcraft.eagle.proxy.locale.api.Message;
import nl.scoutcraft.eagle.proxy.locale.api.MessagePlaceholder;

public final class PartyMessages {

    private static final Internationalization LANG = EagleProxy.getInstance().getLangManager().getPartyLang();

    public static final IMessage PREFIX = new Message(LANG, "party.prefix");
    public static final IMessage ALREADY_IN_PARTY = new CompoundMessage(LANG, "party.already_in_party", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage ALREADY_IN_SERVER = new CompoundMessage(LANG, "party.already_in_server", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage ALREADY_INVITED = new CompoundMessage(LANG, "party.already_invited", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage ALREADY_MEMBER = new CompoundMessage(LANG, "party.already_member", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage ALREADY_MODE = new CompoundMessage(LANG, "party.already_mode", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage ARE_INVITED = new CompoundMessage(LANG, "party.are_invited", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage BEEN_WARPED = new CompoundMessage(LANG, "party.been_warped", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage CHAT_FORMAT = new CompoundMessage(LANG, "party.chat_format", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage COULDNT_KICK = new CompoundMessage(LANG, "party.couldnt_kick", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage COULDNT_LEAVE = new CompoundMessage(LANG, "party.couldnt_leave", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage CREATED_PARTY = new CompoundMessage(LANG, "party.created_party", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage ENTER_NAME = new CompoundMessage(LANG, "party.enter_name", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage HAS_JOINED = new CompoundMessage(LANG, "party.has_joined", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage HAS_KICKED = new CompoundMessage(LANG, "party.has_kicked", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage HAS_TELEPORTED = new CompoundMessage(LANG, "party.has_teleported", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage HAVE_JOINED = new CompoundMessage(LANG, "party.have_joined", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage HAVE_PROMOTED = new CompoundMessage(LANG, "party.have_promoted", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage HAVE_TELEPORTED = new CompoundMessage(LANG, "party.have_teleported", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage HAVE_WARPED = new CompoundMessage(LANG, "party.have_warped", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage INFO_CHAT = new Message(LANG, "party.info_chat");
    public static final IMessage INFO_CREATE = new Message(LANG, "party.info_create");
    public static final IMessage INFO_DISBAND = new Message(LANG, "party.info_disband");
    public static final IMessage INFO_HELP = new Message(LANG, "party.info_help");
    public static final IMessage INFO_INFO = new Message(LANG, "party.info_info");
    public static final IMessage INFO_INVITE = new Message(LANG, "party.info_invite");
    public static final IMessage INFO_JOIN = new Message(LANG, "party.info_join");
    public static final IMessage INFO_KICK = new Message(LANG, "party.info_kick");
    public static final IMessage INFO_LEAVE = new Message(LANG, "party.info_leave");
    public static final IMessage INFO_PRIVATE = new Message(LANG, "party.info_private");
    public static final IMessage INFO_PUBLIC = new Message(LANG, "party.info_public");
    public static final IMessage INFO_TELEPORT = new Message(LANG, "party.info_teleport");
    public static final IMessage INFO_TRANSFER = new Message(LANG, "party.info_transfer");
    public static final IMessage INFO_WARP = new Message(LANG, "party.info_warp");
    public static final IMessage INVITED = new CompoundMessage(LANG, "party.invited", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage INVITE_EXPIRED = new CompoundMessage(LANG, "party.invite_expired", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage INVITE_SELF = new CompoundMessage(LANG, "party.invite_self", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage JOIN_PARTY = new CompoundMessage(LANG, "party.join_party", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage LEADER = new Message(LANG, "party.leader");
    public static final IMessage LEADER_LEFT = new CompoundMessage(LANG, "party.leader_left", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage LEFT_PARTY = new CompoundMessage(LANG, "party.left_party", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage MEMBERS = new Message(LANG, "party.members");
    public static final IMessage NEW_LEADER = new CompoundMessage(LANG, "party.new_leader", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage NO_COMMAND = new CompoundMessage(LANG, "party.no_command", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage NO_PARTY = new CompoundMessage(LANG, "party.no_party", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage NOT_IN_PARTY = new CompoundMessage(LANG, "party.not_in_party", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage NO_INVITES = new CompoundMessage(LANG, "party.no_invites", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage NOT_JOINED = new CompoundMessage(LANG, "party.not_joined", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage NOT_MEMBER = new CompoundMessage(LANG, "party.not_member", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage NOT_PARTY_LEADER = new CompoundMessage(LANG, "party.not_party_leader", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage NOW_MODE = new CompoundMessage(LANG, "party.now_mode", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage PARTY_DISSOLVED = new CompoundMessage(LANG, "party.party_dissolved", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage P_INVITE_EXPIRED = new CompoundMessage(LANG, "party.p_invite_expired", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage PLAYERS_NAME = new CompoundMessage(LANG, "party.players_name", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage PLAYER_NOT_ONLINE = new CompoundMessage(LANG, "party.not_online", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage PROMOTED = new CompoundMessage(LANG, "party.promoted", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage PUB_LEFT_PARTY = new CompoundMessage(LANG, "party.pub_left_party", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage PUB_PROMOTED = new CompoundMessage(LANG, "party.pub_promoted", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SELF_TELEPORT = new CompoundMessage(LANG, "party.self_teleport", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage SUBMIT_MESSAGE = new CompoundMessage(LANG, "party.submit_message", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage WERE_KICKED = new CompoundMessage(LANG, "party.were_kicked", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage YOU_KICKED = new CompoundMessage(LANG, "party.you_kicked", new MessagePlaceholder("%prefix%", PREFIX));

    public static final IMessage PARTY_INFO_COMMAND_HOVER = new Message(LANG, "party.info.command.hover");
    public static final IMessage PARTY_INFO_FORMAT = new Message(LANG, "party.info.format");

    private PartyMessages() {
    }
}
