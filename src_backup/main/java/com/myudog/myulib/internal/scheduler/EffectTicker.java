package com.myudog.myulib.internal.scheduler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
public final class EffectTicker {
    private static final CopyOnWriteArrayList<Supplier<Boolean>> activeTasks = new CopyOnWriteArrayList<>();
    private static final AtomicBoolean registered = new AtomicBoolean(false);
    private EffectTicker() {
    }
    public static void register() {
        if (registered.compareAndSet(false, true)) {
            ServerTickEvents.START_SERVER_TICK.register(server -> tick());
        }
    }
    public static void addTask(Supplier<Boolean> task) {
        register();
        activeTasks.add(task);
    }
    private static void tick() {
        for (Supplier<Boolean> task : activeTasks) {
            boolean keepGoing;
            try {
                keepGoing = Boolean.TRUE.equals(task.get());
            } catch (Throwable throwable) {
                keepGoing = false;
            }
            if (!keepGoing) {
                activeTasks.remove(task);
            }
        }
    }
}