package nl.scoutcraft.eagle.proxy.utils;

import com.velocitypowered.api.scheduler.ScheduledTask;
import nl.scoutcraft.eagle.proxy.EagleProxy;

import java.util.concurrent.TimeUnit;

public abstract class Task implements Runnable {

    private int delay;
    private int interval;
    private TimeUnit timeUnit;

    private ScheduledTask scheduledTask;

    public Task(int interval, TimeUnit timeUnit) {
        this(interval, interval, timeUnit);
    }

    public Task(int delay, int interval, TimeUnit timeUnit) {
        this.delay = delay;
        this.interval = interval;
        this.timeUnit = timeUnit;
    }

    public void start() {
        if (this.scheduledTask == null)
            this.scheduledTask = EagleProxy.getProxy().getScheduler().buildTask(EagleProxy.getInstance(), this).delay(this.delay, this.timeUnit).repeat(this.interval, this.timeUnit).schedule();
    }

    public void stop() {
        if (this.scheduledTask != null) {
            this.scheduledTask.cancel();
            this.scheduledTask = null;
        }
    }

    public void setInterval(int interval, TimeUnit timeUnit) {
        boolean running = this.scheduledTask != null;
        this.stop();

        this.delay = interval;
        this.interval = interval;
        this.timeUnit = timeUnit;

        if (running) this.start();
    }
}
