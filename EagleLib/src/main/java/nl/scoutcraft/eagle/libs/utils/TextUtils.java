package nl.scoutcraft.eagle.libs.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.ChatColor;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TextUtils {

    private static final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx";
    private static final char COLOR_CHAR = '\u00A7';

    private static final Pattern HEX_PATTERN = Pattern.compile("#([a-fA-F0-9]{6})");

    private static final LegacyComponentSerializer AMPERSAND = LegacyComponentSerializer.legacyAmpersand();
    private static final PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.plainText();
    private static final int pixels = 154;

    public static String toString(Component msg) {
        return AMPERSAND.serialize(msg);
    }

    public static String toPlainString(Component msg) {
        return PLAIN.serialize(msg);
    }

    public static String toBukkitString(String str) {

        // Translate default &-codes
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] == '&' && ALL_CODES.indexOf(chars[i + 1]) > -1) {
                chars[i] = ChatColor.COLOR_CHAR;
                chars[i + 1] = Character.toLowerCase( chars[i + 1] );
            }
        }
        str = new String(chars);


        // Translate hex &-codes
        Matcher match = HEX_PATTERN.matcher(str);

        while (match.find()) {
            str = str.substring(0, match.start()) + ChatColor.of(str.substring(match.start(), match.end())) + str.substring(match.end());
            match = HEX_PATTERN.matcher(str);
        }

        return str;
    }

    public static String toBukkitString(Component component) {
        return toBukkitString(nl.scoutcraft.eagle.libs.utils.TextUtils.toString(component));
    }

    public static Component stripColor(Component msg) {
        return Component.text(toPlainString(msg));
    }

    public static Component colorize(String str) {
        return AMPERSAND.deserialize(str);
    }

    public static Component colorize(Component msg) {
        return AMPERSAND.deserialize(AMPERSAND.serialize(msg));
    }

    public static Component permColorize(String str, boolean hasPerm) {
        return hasPerm ? colorize(str) : Component.text(str);
    }

    public static String capitalize(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static String concat(String separator, String... values) {
        if (values.length == 0) return "";

        StringBuilder str = new StringBuilder(values[0]);
        for (int i = 1; i < values.length; i++)
            str.append(separator).append(values[i]);

        return str.toString();
    }

    public static String[] subarray(int startIndex, String[] args) {
        if (startIndex >= args.length) return new String[0];

        String[] out = new String[args.length - startIndex];
        System.arraycopy(args, startIndex, out, 0, out.length);
        return out;
    }

    public static Component line(int pixels) {
        return Component.text(repeat(" ", pixels), Colors.GRAY, TextDecoration.STRIKETHROUGH);
    }

    private static String repeat(String str, int repeat) {
        if (str == null) return null;
        if (repeat <= 0) return "";
        int inputLength = str.length();
        if (repeat == 1 || inputLength == 0) return str;
        if (inputLength == 1 && repeat <= 8192) {
            final char[] buf = new char[repeat];
            Arrays.fill(buf, str.charAt(0));
            return new String(buf);
        }

        int outputLength = inputLength * repeat;
        switch (inputLength) {
            case 1:
                char ch = str.charAt(0);
                char[] output1 = new char[outputLength];
                for (int i = repeat - 1; i >= 0; i--) output1[i] = ch;
                return new String(output1);
            case 2:
                char ch0 = str.charAt(0);
                char ch1 = str.charAt(1);
                char[] output2 = new char[outputLength];
                for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
                    output2[i] = ch0;
                    output2[i + 1] = ch1;
                }
                return new String(output2);
            default:
                return IntStream.range(0, repeat).mapToObj(i -> str).collect(Collectors.joining());
        }
    }

    public static TextComponent.Builder text(Component comp) {
        return Component.text().append(comp);
    }

    public static TextComponent.Builder text(Component... components) {
        TextComponent.Builder builder = Component.text();

        for (Component comp : components)
            builder.append(comp);

        return builder;
    }

    public static TextComponent.Builder text(String text) {
        return Component.text().append(Component.text(text));
    }

    public static TextComponent.Builder text(String text, TextColor color) {
        return Component.text().append(Component.text(text, color));
    }
}
