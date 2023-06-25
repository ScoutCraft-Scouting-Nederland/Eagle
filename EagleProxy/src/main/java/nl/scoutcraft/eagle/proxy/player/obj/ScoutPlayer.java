package nl.scoutcraft.eagle.proxy.player.obj;

import com.velocitypowered.api.proxy.Player;
import net.luckperms.api.LuckPermsProvider;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.party.Party;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ScoutPlayer {

    private final UUID playerId;
    private final UUID sessionId;
    private final LocalDateTime login;

    private final List<Party> partyInvites;

    private String prefix;
    private boolean inGame;
    private boolean spying;
    private boolean vanished;
    private int playtime;
    private boolean loaded;

    @Nullable private String displayName;
    @Nullable private String textureProperty;
    @Nullable private Locale locale;
    @Nullable private String discordId;
    @Nullable private UUID replyTarget;

    public ScoutPlayer(UUID uuid, LocalDateTime login) {
        this.playerId = uuid;
        this.sessionId = UUID.randomUUID();
        this.login = login;
        this.partyInvites = new ArrayList<>();
        this.prefix = LuckPermsProvider.get().getUserManager().getUser(uuid).getCachedData().getMetaData().getPrefix();
        this.inGame = false;
        this.spying = false;
        this.vanished = false;
        this.playtime = 0;
        this.loaded = true;
        this.displayName = null;
        this.textureProperty = null;
        this.locale = null;
        this.discordId = null;
        this.replyTarget = null;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public Optional<Player> getPlayer() {
        return EagleProxy.getProxy().getPlayer(this.playerId);
    }

    public UUID getSessionId() {
        return this.sessionId;
    }

    public List<Party> getPartyInvites() {
        return this.partyInvites;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isInGame() {
        return this.inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public boolean isSpying() {
        return this.spying;
    }

    public void setSpying(boolean spying) {
        this.spying = spying;
    }

    public boolean isVanished() {
        return this.vanished;
    }

    public void setVanished(boolean vanished) {
        this.vanished = vanished;
    }

    public int getPlaytime() {
        return this.getPlaytime(LocalDateTime.now());
    }

    public int getPlaytime(LocalDateTime until) {
        return this.playtime + (int) this.login.until(until, ChronoUnit.SECONDS);
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public int getRawPlaytime() {
        return this.playtime;
    }

    public void setRawPlaytime(int playtime) {
        this.playtime = playtime;
    }

    public Optional<String> getDisplayName() {
        return Optional.ofNullable(this.displayName);
    }

    public void setDisplayName(@Nullable String displayName) {
        Set<String> names = EagleProxy.getInstance().getPlayerManager().getNames();

        if (this.displayName != null)
            names.remove(this.displayName);

        this.displayName = displayName;

        if (this.displayName != null)
            names.add(this.displayName);
    }

    public Optional<String> getTextureProperty() {
        return Optional.ofNullable(this.textureProperty);
    }

    public void setTextureProperty(@Nullable String textureProperty) {
        this.textureProperty = textureProperty;
    }

    public Optional<Locale> getLocale() {
        return Optional.ofNullable(this.locale);
    }

    public void setLocale(@Nullable Locale locale) {
        this.locale = locale;
    }

    public Optional<String> getDiscordId() {
        return Optional.ofNullable(this.discordId);
    }

    public void setDiscordId(@Nullable String discordId) {
        this.discordId = discordId;
    }

    public Optional<UUID> getReplyTarget() {
        return Optional.ofNullable(this.replyTarget);
    }

    public void setReplyTarget(@Nullable UUID replyTarget) {
        this.replyTarget = replyTarget;
    }
}
