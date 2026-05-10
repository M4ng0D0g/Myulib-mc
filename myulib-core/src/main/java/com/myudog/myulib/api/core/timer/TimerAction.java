package com.myudog.myulib.api.core.timer;

@FunctionalInterface
public interface TimerAction {
    void invoke(TimerSnapshot snapshot);
}
