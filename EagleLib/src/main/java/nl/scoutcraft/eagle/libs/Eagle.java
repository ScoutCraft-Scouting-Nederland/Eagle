package nl.scoutcraft.eagle.libs;

public final class Eagle {

    private static IEagle plugin;

    public static IEagle getInstance() {
        return plugin;
    }

    public static void init(IEagle plugin) {
        Eagle.plugin = plugin;
    }

    private Eagle(){}
}
