package nl.scoutcraft.eagle.scotty.discord;

import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.function.Function;

public class EmbedType {

    private final int id;
    private final Class<?>[] params;
    private final Function<Object[], MessageEmbed> builder;

    EmbedType(int id, Function<Object[], MessageEmbed> builder, Class<?>... params) {
        this.id = id;
        this.params = params;
        this.builder = builder;
    }

    public int getId() {
        return this.id;
    }

    public Class<?>[] getParams() {
        return this.params;
    }

    public MessageEmbed build(Object... params) {
        return this.builder.apply(params);
    }
}
