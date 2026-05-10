package com.myudog.myulib.internal.monitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.atomic.AtomicInteger;
public final class VFXMonitor {
    private static final Logger logger = LoggerFactory.getLogger("MyuVFX-Monitor");
    private static final AtomicInteger particleCount = new AtomicInteger(0);
    public static volatile int maxParticlesPerSecond = 10000;
    private static volatile long lastResetTime = System.currentTimeMillis();
    private VFXMonitor() {
    }
    public static boolean requestSpawn(int amount) {
        checkReset();
        if (particleCount.get() + amount > maxParticlesPerSecond) {
            return false;
        }
        particleCount.addAndGet(amount);
        return true;
    }
    private static void checkReset() {
        long now = System.currentTimeMillis();
        if (now - lastResetTime > 1000L) {
            if (particleCount.get() > 0) {
                logger.debug("Current VFX Load: {} particles/sec", particleCount.get());
            }
            particleCount.set(0);
            lastResetTime = now;
        }
    }
    public static int getCurrentTPSLoad() {
        return particleCount.get();
    }
}