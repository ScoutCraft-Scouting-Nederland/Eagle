package nl.scoutcraft.eagle.server.data;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.SerializationUtils;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class UUIDListTagType implements PersistentDataType<byte[], List<UUID>> {

    @Override
    @NotNull
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    @NotNull
    public Class<List<UUID>> getComplexType() {
        return ((Class<List<UUID>>)((Class<?>)List.class));
    }

    @Override
    public byte[] toPrimitive(@NotNull List<UUID> list, @NotNull PersistentDataAdapterContext context) {
        return SerializationUtils.serialize(list instanceof Serializable ? (Serializable) list : Lists.newArrayList(list));
    }

    @Override
    @NotNull
    public List<UUID> fromPrimitive(@NotNull byte[] bytes, @NotNull PersistentDataAdapterContext context) {
        Object data = SerializationUtils.deserialize(bytes);
        return (data instanceof List) ? (List<UUID>) data : Lists.newArrayList((Iterable<UUID>) data);
    }
}
