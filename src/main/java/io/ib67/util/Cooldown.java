package io.ib67.util;

import java.time.Duration;

public class Cooldown {
    private final long distance;
    private volatile long lastTime;

    private Cooldown(long time) {
        this.distance = time;
    }

    @Deprecated
    public static Cooldown of(long time) {
        return new Cooldown(time);
    }

    public static Cooldown of(Duration time) {
        return new Cooldown(time.toMillis());
    }

    public boolean isAvailable() {
        return System.currentTimeMillis() - lastTime >= distance;
    }

    public void waitFor() {
        while (!isAvailable()) {
            Thread.yield(); // yield to other threads
        }
    }

    public void breaks() {
        lastTime = 0;
    }
    public boolean refresh(){
        if (!isAvailable()) {
            return false;
        }
        lastTime = System.currentTimeMillis();
        return true;
    }
}
