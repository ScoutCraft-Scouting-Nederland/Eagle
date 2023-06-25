package nl.scoutcraft.eagle.libs.sql;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public abstract class SQLCache<T extends Identifiable<UUID>> {

    private final Map<UUID, Node<T>> values;
    private final long cleanupMillis;

    public SQLCache(long keepInMemory, TimeUnit unit) {
        this.values = new ConcurrentHashMap<>();
        this.cleanupMillis = unit.toMillis(keepInMemory);

        this.scheduleCleaner(this::cleanup);
    }

    public void add(T value) {
        this.values.put(value.getIdentifier(), new Node<>(System.currentTimeMillis(), value));
    }

    public void remove(UUID uuid) {
        this.values.remove(uuid);
    }

    public Optional<T> get(UUID uuid) {
        return Optional.ofNullable(this.values.get(uuid)).map(Node::value);
    }

    public Stream<T> stream() {
        return this.values.values().stream().map(Node::value);
    }

    private void cleanup() {
        long toRemove = System.currentTimeMillis() - this.cleanupMillis;

        this.values.values().removeIf(node -> node.age < toRemove);
    }

    public abstract void scheduleCleaner(Runnable runnable);

    private record Node<T extends Identifiable<UUID>>(long age, T value) {}
}
