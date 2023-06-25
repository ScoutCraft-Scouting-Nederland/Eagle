package nl.scoutcraft.eagle.proxy.event;

import nl.scoutcraft.eagle.proxy.io.ConfigAdaptor;

public record ConfigReloadEvent(ConfigAdaptor config) {

    public ConfigAdaptor getConfig() {
        return this.config;
    }
}
