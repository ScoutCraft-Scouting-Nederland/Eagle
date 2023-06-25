package nl.scoutcraft.eagle.proxy.commands.lib;

import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.proxy.commands.lib.args.Argument;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CommandContext {

    private final Map<String, CommandContextPair<?>> values;
    private final String[] raw;
    private int index;

    public CommandContext(String[] raw) {
        this(raw, 0);
    }

    private CommandContext(String[] raw, int index) {
        this.values = new HashMap<>();
        this.raw = raw;
        this.index = index;
    }

    public <T> void add(Argument<T> arg, String raw, @Nullable T parsed) {
        this.values.put(arg.getName(), new CommandContextPair<>(raw, parsed));
    }

    public boolean has(String name) {
        return this.values.containsKey(name);
    }

    public <T> boolean has(String name, Class<T> clazz) {
        CommandContextPair<?> pair = this.values.get(name);
        return pair != null && pair.parsed != null && clazz.isAssignableFrom(pair.parsed.getClass());
    }

    public <T> Optional<T> get(String name) {
        try {
            return Optional.ofNullable(this.values.get(name)).map(pair -> (T) pair.parsed);
        } catch (ClassCastException exc) {
            return Optional.empty();
        }
    }

    public <T> Optional<T> get(String name, Class<T> clazz) {
        Object parsed = this.values.get(name).parsed;
        return parsed == null || !clazz.isAssignableFrom(parsed.getClass()) ? Optional.empty() : Optional.of((T) parsed);
    }

    public String getRaw(String name) {
        return Optional.ofNullable(this.values.get(name)).map(p -> p.raw).orElse("Unknown");
    }

    @Nullable
    public String getNext() {
        return this.hasRemaining() ? this.raw[this.index] : null;
    }

    @Nullable
    public String removeNext() {
        return this.hasRemaining() ? this.raw[this.index++] : null;
    }

    public boolean hasRemaining() {
        return this.raw.length > this.index;
    }

    public int getRemainingSize() {
        return this.raw.length - this.index;
    }

    public String[] getRemaining() {
        return TextUtils.subarray(this.index, this.raw);
    }

    public CommandContext copyRaw() {
        return new CommandContext(this.raw.clone(), this.index);
    }

    @Override
    public String toString() {
        return "CommandContext{" +
                "values=" + this.values +
                ", raw=" + Arrays.toString(this.raw) +
                ", raw.length=" + this.raw.length +
                ", index=" + this.index +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandContext)) return false;
        CommandContext that = (CommandContext) o;
        return index == that.index && Objects.equals(values, that.values) && Arrays.equals(raw, that.raw);
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hash(this.values, this.index) + Arrays.hashCode(this.raw);
    }

    private record CommandContextPair<T>(String raw, @Nullable T parsed) {}
}
