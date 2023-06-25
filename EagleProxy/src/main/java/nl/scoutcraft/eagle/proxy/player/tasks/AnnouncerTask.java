package nl.scoutcraft.eagle.proxy.player.tasks;

import nl.scoutcraft.eagle.libs.locale.Internationalization;
import nl.scoutcraft.eagle.libs.utils.Perms;
import nl.scoutcraft.eagle.proxy.EagleProxy;
import nl.scoutcraft.eagle.proxy.locale.api.*;
import nl.scoutcraft.eagle.proxy.utils.Task;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AnnouncerTask extends Task {

    private final IMessage[] messages;
    private int index;

    public AnnouncerTask(EagleProxy plugin) {
        super(plugin.getConfigAdaptor().getNode("announcer_interval_seconds").getInt(120), TimeUnit.SECONDS);

        Internationalization lang = plugin.getLangManager().getAnnouncementLang();
        IPlaceholder prefix = new MessagePlaceholder("%prefix%", new Message(lang, "prefix"));
        List<IMessage> messages = new ArrayList<>();
        Enumeration<String> keys = lang.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (!key.equalsIgnoreCase("prefix"))
                messages.add(new CompoundMessage(lang, key, prefix));
        }

        this.messages = messages.toArray(new IMessage[messages.size()]);
        this.index = 0;
    }

    @Override
    public void run() {
        IMessage message = this.messages[this.index++];
        if (this.index >= this.messages.length) this.index = 0;
        EagleProxy.getProxy().getAllPlayers().stream().filter(p -> !p.hasPermission(Perms.ANNOUNCEMENTS_BYPASS)).forEach(message::send);
    }
}
