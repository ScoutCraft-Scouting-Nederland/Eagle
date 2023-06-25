package nl.scoutcraft.eagle.libs.network;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.function.Consumer;

public class NetworkRequest<T, R> {

    private static final long TIMEOUT_MILLIS = 2_000;

    private final NetworkChannel<T> channel;
    private final T downHandler;
    private final UUID id;

    @Nullable private Consumer<R> handler;
    @Nullable private Runnable timeoutHandler;
    private long timeout;
    private boolean handled;

    private Class<R> returnType;
    private Object conn;

    protected NetworkRequest(NetworkChannel<T> channel, T downHandler, UUID id) {
        this.channel = channel;
        this.downHandler = downHandler;
        this.id = id;
        this.timeout = TIMEOUT_MILLIS;
        this.handled = false;
    }

    public NetworkRequest<T, R> onResponse(@Nullable Consumer<R> handler) {
        this.handler = handler;
        return this;
    }

    public NetworkRequest<T, R> onTimeout(@Nullable Runnable handler) {
        this.timeoutHandler = handler;
        return this;
    }

    @Deprecated
    public NetworkRequest<T, R> timeout(long timeout) {
        return this.setTimeout(timeout);
    }

    public NetworkRequest<T, R> setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public T setTarget(Object conn) {
        this.conn = conn;
        return this.downHandler;
    }

    @SuppressWarnings({"UnstableApiUsage", "unchecked"})
    protected void send(Method method, @Nullable Object[] args) {
        this.returnType = (Class<R>) method.getReturnType();

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("request:" + method.getName() + ":" + this.id.toString());
        output.writeUTF(this.channel.argsToJson(args));

        this.channel.debug("Sending request: " + this.channel.name + "#" + method.getName() + ":" + this.id);
        this.channel.sendData(this.conn, output);
        this.channel.scheduleAsync(this::handleTimeout, this.timeout);
    }

    @SuppressWarnings("unchecked")
    protected void handle(Object value) {
        if (this.handled) return;
        this.handled = true;

        if (this.handler != null)
            this.handler.accept((R) value);
    }

    private void handleTimeout() {
        if (!this.handled && this.timeoutHandler != null) {
            this.handled = true;
            this.timeoutHandler.run();
        }
    }

    protected UUID getId() {
        return this.id;
    }

    protected Class<R> getReturnType() {
        return this.returnType;
    }
}
