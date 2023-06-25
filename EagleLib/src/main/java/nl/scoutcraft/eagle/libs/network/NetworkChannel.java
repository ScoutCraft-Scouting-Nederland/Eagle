package nl.scoutcraft.eagle.libs.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class NetworkChannel<T> {

    private static final Logger LOGGER = Logger.getLogger("Eagle Network");
    private static final boolean DEBUG_MODE = false;
    private static final JsonParser JSON = new JsonParser();
    private static final Gson GSON = new Gson();

    protected final String name;
    private final T downHandler;
    @Nullable private final T localHandler;

    private final Map<UUID, NetworkRequest<T, ?>> pending;
    private final Map<Long, Object> processing;

    public NetworkChannel(String name, Class<T> clazz, @Nullable T localHandler, Object plugin) {
        this.name = name;
        this.downHandler = this.downHandler(clazz);
        this.localHandler = localHandler;
        this.pending = new HashMap<>();
        this.processing = new HashMap<>();

        this.register(plugin);
    }

    public <R> NetworkRequest<T, R> request() {
        NetworkRequest<T, R> request = new NetworkRequest<>(this, this.downHandler, UUID.randomUUID());
        this.processing.put(Thread.currentThread().getId(), request);
        return request;
    }

    @Nullable
    public Object getConnection() {
        return this.processing.get(Thread.currentThread().getId());
    }

    @SuppressWarnings("UnstableApiUsage")
    protected void handlePluginMessage(String tag, Object conn, byte[] data) {
        if (!tag.equals(this.name)) return;
        ByteArrayDataInput input = ByteStreams.newDataInput(data);
        String[] header = input.readUTF().split(":");
        String methodName = header[1];
        UUID id = UUID.fromString(header[2]);

        this.scheduleAsync(() -> {
            if (header[0].equalsIgnoreCase("response")) {
                this.handleResponse(id, methodName, input.readUTF());
            } else if (this.localHandler != null) {
                this.handleRequest(conn, id, methodName, input.readUTF());
            }
        });
    }

    // TODO: Send method + arg types with the request
    private void handleResponse(UUID id, String methodName, @Nullable String data) {
        NetworkRequest<T, ?> request = this.pending.remove(id);

        if (request != null) {
            request.handle(request.getReturnType().equals(Void.TYPE) || data == null || data.isEmpty() || data.equals("null") ? null : GSON.fromJson(data, request.getReturnType()));
            this.debug("Handling response:" + methodName + ":" + id + " {" + data + "}");
        }
    }

    private void handleRequest(Object conn, UUID id, String methodName, @Nullable String data) {
        this.processing.put(Thread.currentThread().getId(), conn);

        this.debug("Handling request:" + methodName + ":" + id + " {" + data + "}");

        JsonArray json = data == null || data.isEmpty() || data.equals("null") ? new JsonArray() : JSON.parse(data).getAsJsonArray();
        Method method = this.getMethod(this.localHandler.getClass(), methodName, json.size());
        Object[] args = new Object[json.size()];
        for (int i = 0; i < args.length; i++)
            args[i] = GSON.fromJson(json.get(i).getAsString(), method.getParameterTypes()[i]);

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("response:" + methodName + ":" + id);
        output.writeUTF(GSON.toJson(this.invokeMethod(this.localHandler, method, args))); // TODO: Send args classes with methodName in header
        this.sendData(conn, output);
        this.processing.remove(Thread.currentThread().getId());
    }

    protected String argsToJson(@Nullable Object[] args) {
        if (args == null || args.length == 0) return "";

        JsonArray array = new JsonArray();
        for (Object arg : args)
            array.add(GSON.toJson(arg));

        return array.toString();
    }

    @SuppressWarnings("unchecked")
    private T downHandler(Class<T> clazz) {
        return (T) java.lang.reflect.Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            NetworkRequest<T, ?> request = (NetworkRequest<T, ?>) this.processing.remove(Thread.currentThread().getId());

            if (request != null) {
                this.pending.put(request.getId(), request);
                request.send(method, args);
            }

            return null;
        });
    }

    @Nullable
    private Method getMethod(Class<?> clazz, String name, int argCount) {
        for (Method method : clazz.getDeclaredMethods())
            if (method.getName().equals(name) && method.getParameterTypes().length == argCount)
                return method;
        return null;
    }

    private Object invokeMethod(Object obj, Method method, Object[] args) {
        if (obj == null || method == null)
            return null;

        try {
            return method.invoke(obj, args);
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException exc) {
            LOGGER.log(Level.SEVERE, "Failed to invoke local handler method!", exc);
        }
        return null;
    }

    protected void debug(String msg) {
        if (DEBUG_MODE) LOGGER.info(msg);
    }

    protected abstract void register(Object plugin);

    protected abstract void sendData(Object conn, ByteArrayDataOutput output);

    protected abstract void scheduleAsync(Runnable task);

    protected abstract void scheduleAsync(Runnable task, long millis);
}
