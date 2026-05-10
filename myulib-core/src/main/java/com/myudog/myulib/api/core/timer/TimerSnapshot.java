package com.myudog.myulib.api.core.timer;

import java.util.UUID; // 🌟 別忘了引入 UUID;

public record TimerSnapshot(
        UUID instanceId,    // 🌟 核心修正：將 int timerEntityId 改為 UUID instanceId
        Long ownerEntityId,
        TimerDefinition timer,
        TimerStatus status,
        long elapsedTicks,
        long remainingTicks,
        TimerPayload payload,
        long currentTick
) {
    public long durationTicks() {
        return timer == null ? 0L : timer.durationTicks;
    }

    public TimerMode mode() {
        return timer == null ? TimerMode.COUNT_UP : timer.mode;
    }

    public double progress() {
        long duration = Math.max(1L, durationTicks());
        return Math.min(1.0d, Math.max(0.0d, (double) elapsedTicks / duration));
    }

    @SuppressWarnings("unchecked")
    public <T extends TimerPayload> T payloadAs() {
        return (T) payload;
    }

    public <T extends TimerPayload> T requirePayload() {
        if (payload == null) throw new IllegalStateException("Timer payload is missing");
        return payloadAs();
    }
}