package com.myudog.myulib.api.core.timer;

public final class TimerEvents {
    private TimerEvents() {
    }

    public record TimerStartedEvent(TimerSnapshot snapshot) {
    }

    public record TimerPausedEvent(TimerSnapshot snapshot) {
    }

    public record TimerResumedEvent(TimerSnapshot snapshot) {
    }

    public record TimerResetEvent(TimerSnapshot snapshot) {
    }

    public record TimerStoppedEvent(TimerSnapshot snapshot) {
    }

    public record TimerTickEvent(TimerSnapshot snapshot) {
    }

    public record TimerCheckpointEvent(TimerSnapshot snapshot, int bindingId, TimerTickBasis basis, long tick) {
    }

    public record TimerCompletedEvent(TimerSnapshot snapshot) {
    }
}
