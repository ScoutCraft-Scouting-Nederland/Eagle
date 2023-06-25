package nl.scoutcraft.eagle.server.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public interface IMenu extends Listener {

    void open(Player player);
    void close();
    void update();

    void setTitle(String title);
    void setTitle(Component title);
}
