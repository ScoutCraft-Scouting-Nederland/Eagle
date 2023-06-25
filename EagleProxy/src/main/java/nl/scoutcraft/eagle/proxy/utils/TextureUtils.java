package nl.scoutcraft.eagle.proxy.utils;

import com.google.gson.JsonParser;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public class TextureUtils {

    @Nullable
    public static String requestProfile(UUID uuid) {
        String textureProperty = null;

        try (InputStreamReader reader = new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false").openStream())) {
            textureProperty = JsonParser.parseReader(reader).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString();
        } catch (NullPointerException | IOException | IllegalStateException ignored) {
            EagleProxy.getInstance().getLogger().warn("Failed to get player texture property: " + ignored.getMessage());
        }

        return textureProperty;
    }
}
