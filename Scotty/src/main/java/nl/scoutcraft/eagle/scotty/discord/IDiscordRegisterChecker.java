package nl.scoutcraft.eagle.scotty.discord;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface IDiscordRegisterChecker {

    /**
     * Method used to check whether a player can register a Discord account to the Minecraft account.
     *
     * @param userId The Discord id of the player.
     * @return An error message, or null if the check passed.
     */
    @Nullable
    String checkRegister(String userId);
}
