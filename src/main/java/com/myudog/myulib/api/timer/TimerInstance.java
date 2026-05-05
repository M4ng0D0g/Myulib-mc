package com.myudog.myulib.api.timer;

import java.util.UUID;

public final class TimerInstance {
    public final int timerEntityId;
    public final UUID timerId;
    public final Long ownerEntityId;
    public TimerPayload payload;
    public TimerStatus status;
    public long elapsedTicks;
    public long lastUpdatedTick;
    public long pausedTicks;

    public TimerInstance(int timerEntityId, UUID timerId, Long ownerEntityId, TimerPayload payload) {
        this.timerEntityId = timerEntityId;
        this.timerId = timerId;
        this.ownerEntityId = ownerEntityId;
        this.payload = payload;
        this.status = TimerStatus.IDLE;
    }

    public boolean isRunning() { return status == TimerStatus.RUNNING; }
    public boolean isPaused() { return status == TimerStatus.PAUSED; }
    public boolean isStopped() { return status == TimerStatus.STOPPED; }
    public boolean isCompleted() { return status == TimerStatus.COMPLETED; }
}
