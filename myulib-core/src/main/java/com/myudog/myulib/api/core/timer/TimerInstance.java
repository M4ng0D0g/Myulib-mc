package com.myudog.myulib.api.core.timer;

import java.util.UUID;

public final class TimerInstance {

    // 🌟 1. 改為 UUID，作為在 TimerFeatureImpl 中 Map 查詢的唯一鑰匙
    public final UUID instanceId;

    // 🌟 2. 更名為 defId，明確表示這是指向 TimerManager 註冊表中的藍圖
    public final UUID defId;

    public final Long ownerEntityId;
    public TimerPayload payload;
    public TimerStatus status;
    public long elapsedTicks;
    public long lastUpdatedTick;
    public long pausedTicks;

    // 建構子同步更新參數
    public TimerInstance(UUID instanceId, UUID defId, Long ownerEntityId, TimerPayload payload) {
        this.instanceId = instanceId;
        this.defId = defId;
        this.ownerEntityId = ownerEntityId;
        this.payload = payload;
        this.status = TimerStatus.IDLE;
    }

    public boolean isRunning() { return status == TimerStatus.RUNNING; }
    public boolean isPaused() { return status == TimerStatus.PAUSED; }
    public boolean isStopped() { return status == TimerStatus.STOPPED; }
    public boolean isCompleted() { return status == TimerStatus.COMPLETED; }
}