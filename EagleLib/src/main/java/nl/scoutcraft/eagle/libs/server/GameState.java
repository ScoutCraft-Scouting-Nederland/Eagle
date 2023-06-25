package nl.scoutcraft.eagle.libs.server;

import org.jetbrains.annotations.Nullable;

public enum GameState {

    OFFLINE("offline"),
    WAITING("waiting"),
    FULL("full"),
    IN_GAME("in-game"),
    RESTARTING("restarting");

    private final String id;

    GameState(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    @Nullable
    public static GameState of(@Nullable String id) {
        if (id == null) return null;

        for (GameState state : values())
            if (state.id.equalsIgnoreCase(id))
                return state;
        return null;
    }
}
