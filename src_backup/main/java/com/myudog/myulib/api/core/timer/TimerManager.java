package com.myudog.myulib.api.core.timer;

import com.myudog.myulib.Myulib;
import com.myudog.myulib.api.core.debug.DebugFeature;
import com.myudog.myulib.api.core.debug.DebugLogManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class TimerManager {

    public static final TimerManager INSTANCE = new TimerManager();

    // 🌟 全域藍圖庫：Map<DefinitionUUID, TimerDefinition>
    private final Map<UUID, TimerDefinition> TIMERS = new LinkedHashMap<>();

    // 🌟 全域實例追蹤庫：Map<InstanceUUID, TimerInstance>
    private final Map<UUID, TimerInstance> INSTANCES = new ConcurrentHashMap<>();

    private TimerManager() {
    }

    public void install() {
        // 1. 掛載伺服器 Tick 事件 (驅動 Timer 的核心引擎)
        ServerTickEvents.END_SERVER_TICK.register(TimerManager.INSTANCE::update);

        // 2. 掛載伺服器停止事件 (安全機制)
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            TIMERS.clear();
            INSTANCES.clear();
            System.out.println("[Myulib] TimerManager 已成功釋放！");
        });

        System.out.println("[Myulib] TimerManager 事件已成功掛載！");
    }

    public void register(TimerDefinition timer) {
        if (!validate(timer)) {
            throw new IllegalArgumentException("TimerDefinition 驗證失敗: " + (timer == null ? "null" : timer.uuid));
        }
        TIMERS.put(timer.uuid, timer);
        DebugLogManager.INSTANCE.log(DebugFeature.TIMER,
                "register defId=" + timer.uuid + ",duration=" + timer.durationTicks + ",mode=" + timer.mode);
    }

    public boolean validate(TimerDefinition timer) {
        return timer != null && timer.uuid != null && !TIMERS.containsKey(timer.uuid);
    }

    public TimerDefinition unregister(UUID defUuid) {
        DebugLogManager.INSTANCE.log(DebugFeature.TIMER, "unregister defId=" + defUuid);
        return TIMERS.remove(defUuid);
    }

    public boolean hasDefinition(UUID defUuid) {
        return TIMERS.containsKey(defUuid);
    }

    public TimerDefinition getDefinition(UUID defUuid) {
        return TIMERS.get(defUuid);
    }

    // ==========================================================================================
    // 實例管理 (Instance Management) - 統一使用 UUID
    // ==========================================================================================

    /**
     * 創建一個計時器實例，並配發一個唯一的 Instance UUID
     */
    public UUID createInstance(UUID defUuid, Long ownerEntityId, TimerPayload payload, boolean autoStart) {
        TimerDefinition timer = TIMERS.get(defUuid);
        if (timer == null) {
            throw new IllegalArgumentException("Unknown timer definition: " + defUuid);
        }

        UUID instanceId = UUID.randomUUID(); // 🌟 配發實例專屬的 UUID
        TimerInstance instance = new TimerInstance(instanceId, defUuid, ownerEntityId, payload);
        INSTANCES.put(instanceId, instance);

        DebugLogManager.INSTANCE.log(DebugFeature.TIMER,
                "create instance=" + instanceId + ",defId=" + defUuid + ",owner=" + ownerEntityId + ",autoStart=" + autoStart);

        if (autoStart) {
            start(instanceId);
        }
        return instanceId;
    }

    public TimerInstance getInstance(UUID instanceId) {
        return INSTANCES.get(instanceId);
    }

    public TimerSnapshot getSnapshot(UUID instanceId) {
        TimerInstance instance = INSTANCES.get(instanceId);
        if (instance == null) {
            return null;
        }
        TimerDefinition timer = TIMERS.get(instance.defId); // 🌟 讀取 defId
        if (timer == null) {
            return null;
        }
        long remaining = Math.max(0L, timer.durationTicks - instance.elapsedTicks);
        return new TimerSnapshot(instanceId, instance.ownerEntityId, timer, instance.status, instance.elapsedTicks, remaining, instance.payload, instance.lastUpdatedTick);
    }

    public List<TimerInstance> findInstances(Long ownerEntityId) {
        return INSTANCES.values().stream().filter(instance -> Objects.equals(instance.ownerEntityId, ownerEntityId)).toList();
    }

    public boolean isRunning(UUID instanceId) { return getInstance(instanceId) != null && getInstance(instanceId).isRunning(); }
    public boolean isPaused(UUID instanceId) { return getInstance(instanceId) != null && getInstance(instanceId).isPaused(); }
    public boolean isStopped(UUID instanceId) { return getInstance(instanceId) != null && getInstance(instanceId).isStopped(); }
    public boolean isCompleted(UUID instanceId) { return getInstance(instanceId) != null && getInstance(instanceId).isCompleted(); }

    public void start(UUID instanceId) {
        updateState(instanceId, TimerStatus.RUNNING, snapshot -> snapshot.timer().startedActions.forEach(action -> action.invoke(snapshot)));
    }

    /**
     * 快速建立一次性計時器
     */
    public UUID start(long ticks, Consumer<UUID> onExpire) {
        String timerKey = "auto_timer_" + UUID.randomUUID();
        UUID defId = stableUuid(timerKey);

        TimerDefinition timer = new TimerDefinition(defId, ticks, TimerMode.COUNT_DOWN, true)
                .onCompleted(snapshot -> {
                    if (onExpire != null) {
                        onExpire.accept(snapshot.instanceId());
                    }
                    unregister(defId); // 執行完畢自動註銷藍圖
                });
        register(timer);
        return createInstance(defId, null, null, true);
    }

    public void pause(UUID instanceId) {
        updateState(instanceId, TimerStatus.PAUSED, snapshot -> snapshot.timer().pausedActions.forEach(action -> action.invoke(snapshot)));
    }

    public void resume(UUID instanceId) {
        updateState(instanceId, TimerStatus.RUNNING, snapshot -> snapshot.timer().resumedActions.forEach(action -> action.invoke(snapshot)));
    }

    public void stop(UUID instanceId) {
        updateState(instanceId, TimerStatus.STOPPED, snapshot -> snapshot.timer().stoppedActions.forEach(action -> action.invoke(snapshot)));
    }

    public void reset(UUID instanceId, boolean clearPayload) {
        TimerInstance instance = INSTANCES.get(instanceId);
        if (instance == null) return;
        instance.elapsedTicks = 0;
        instance.lastUpdatedTick = 0;
        instance.pausedTicks = 0;
        instance.status = TimerStatus.IDLE;
        if (clearPayload) {
            instance.payload = null;
        }
        DebugLogManager.INSTANCE.log(DebugFeature.TIMER,
                "reset instance=" + instanceId + ",clearPayload=" + clearPayload);
    }

    public void setElapsedTicks(UUID instanceId, long ticks) {
        TimerInstance instance = INSTANCES.get(instanceId);
        if (instance != null) {
            instance.elapsedTicks = Math.max(0L, ticks);
        }
    }

    public void setRemainingTicks(UUID instanceId, long ticks) {
        TimerInstance instance = INSTANCES.get(instanceId);
        if (instance != null) {
            TimerDefinition timer = TIMERS.get(instance.defId);
            if (timer != null) {
                setElapsedTicks(instanceId, Math.max(0L, timer.durationTicks - ticks));
            }
        }
    }

    public void setPayload(UUID instanceId, TimerPayload payload) {
        TimerInstance instance = INSTANCES.get(instanceId);
        if (instance != null) {
            instance.payload = payload;
        }
    }

    public int timerDefinitionCount() {
        return TIMERS.size();
    }

    public int timerInstanceCount() {
        return INSTANCES.size();
    }

    // ==========================================================================================
    // 核心 Tick 引擎
    // ==========================================================================================

    public void update(MinecraftServer server) {
        java.util.Iterator<Map.Entry<UUID, TimerInstance>> iterator = INSTANCES.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, TimerInstance> entry = iterator.next();
            UUID instanceId = entry.getKey();
            TimerInstance instance = entry.getValue();

            // 防漏水機制：如果計時器已經停止或完成，將其從背景移除
            if (instance.isStopped() || instance.isCompleted()) {
                iterator.remove();
                continue;
            }

            if (!instance.isRunning()) continue;

            instance.elapsedTicks++;
            instance.lastUpdatedTick++;
            TimerDefinition timer = TIMERS.get(instance.defId);

            if (timer == null) continue;

            TimerSnapshot snapshot = getSnapshot(instanceId);
            if (snapshot != null) {
                long remaining = Math.max(0L, timer.durationTicks - instance.elapsedTicks);

                for (TimerBinding binding : timer.elapsedBindings.values()) {
                    if (binding.basis() == TimerTickBasis.ELAPSED && binding.matches(instance.elapsedTicks)) {
                        binding.action().invoke(snapshot);
                    }
                }

                for (TimerBinding binding : timer.remainingBindings.values()) {
                    if (binding.basis() == TimerTickBasis.REMAINING && binding.matches(remaining)) {
                        binding.action().invoke(snapshot);
                    }
                }

                if (instance.elapsedTicks >= timer.durationTicks) {
                    instance.status = TimerStatus.COMPLETED;
                    timer.completedActions.forEach(action -> action.invoke(snapshot));
                    DebugLogManager.INSTANCE.log(DebugFeature.TIMER,
                            "completed instance=" + instanceId + ",defId=" + instance.defId + ",elapsed=" + instance.elapsedTicks);

                    if (timer.autoStopOnComplete) {
                        instance.status = TimerStatus.STOPPED;
                    }
                }
            }
        }
    }

    private void updateState(UUID instanceId, TimerStatus status, java.util.function.Consumer<TimerSnapshot> actionConsumer) {
        TimerInstance instance = INSTANCES.get(instanceId);
        if (instance == null) return;

        instance.status = status;
        DebugLogManager.INSTANCE.log(DebugFeature.TIMER, "state instance=" + instanceId + " -> " + status);

        TimerSnapshot snapshot = getSnapshot(instanceId);
        if (snapshot != null) {
            actionConsumer.accept(snapshot);
        }
    }

    private static UUID stableUuid(String token) {
        return UUID.nameUUIDFromBytes(token.getBytes(StandardCharsets.UTF_8));
    }
}