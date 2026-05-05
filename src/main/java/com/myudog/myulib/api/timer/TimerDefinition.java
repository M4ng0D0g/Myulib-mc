package com.myudog.myulib.api.timer;

import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.UUID;

public final class TimerDefinition {
    public final UUID uuid;
    public final long durationTicks;
    public final TimerMode mode;
    public final boolean autoStopOnComplete;

    public final Map<Integer, TimerBinding> elapsedBindings = new LinkedHashMap<>();
    public final Map<Integer, TimerBinding> remainingBindings = new LinkedHashMap<>();
    public final List<TimerAction> startedActions = new ArrayList<>();
    public final List<TimerAction> pausedActions = new ArrayList<>();
    public final List<TimerAction> resumedActions = new ArrayList<>();
    public final List<TimerAction> resetActions = new ArrayList<>();
    public final List<TimerAction> stoppedActions = new ArrayList<>();
    public final List<TimerAction> completedActions = new ArrayList<>();
    private int nextBindingId = 1;

    public TimerDefinition(@NotNull UUID uuid, long durationTicks) {
        this(uuid, durationTicks, TimerMode.COUNT_UP, true);
    }
    public TimerDefinition(@NotNull String token, long durationTicks) {
        this(stableUuid(token), durationTicks, TimerMode.COUNT_UP, true);
    }
    public TimerDefinition(@NotNull Identifier id, long durationTicks) {
        this(stableUuid(id.toString()), durationTicks, TimerMode.COUNT_UP, true);
    }
    public TimerDefinition(@NotNull UUID uuid, long durationTicks, TimerMode mode, boolean autoStopOnComplete) {
        this.uuid = Objects.requireNonNull(uuid, "uuid 不得為空");
        this.durationTicks = Math.max(0L, durationTicks);
        this.mode = mode == null ? TimerMode.COUNT_UP : mode;
        this.autoStopOnComplete = autoStopOnComplete;
    }
    public TimerDefinition(@NotNull String token, long durationTicks, TimerMode mode, boolean autoStopOnComplete) {
        this(stableUuid(token), durationTicks, mode, autoStopOnComplete);
    }
    public TimerDefinition(@NotNull Identifier id, long durationTicks, TimerMode mode, boolean autoStopOnComplete) {
        this(stableUuid(id.toString()), durationTicks, mode, autoStopOnComplete);
    }

    public TimerDefinition onElapsedTick(long tick, TimerAction action) { return onElapsedTick(tick, action, false); }
    public TimerDefinition onRemainingTick(long tick, TimerAction action) { return onRemainingTick(tick, action, false); }
    public TimerDefinition onElapsedTick(long tick, TimerAction action, boolean replace) { return addBinding(elapsedBindings, Set.of(tick), TimerTickBasis.ELAPSED, action, replace); }
    public TimerDefinition onRemainingTick(long tick, TimerAction action, boolean replace) { return addBinding(remainingBindings, Set.of(tick), TimerTickBasis.REMAINING, action, replace); }

    public TimerDefinition onElapsedTick(TimerAction action, int... ticks) { return onElapsedTick(action, false, ticks); }
    public TimerDefinition onRemainingTick(TimerAction action, int... ticks) { return onRemainingTick(action, false, ticks); }
    public TimerDefinition onElapsedTick(TimerAction action, boolean replace, int... ticks) {
        return addBinding(elapsedBindings, normalizeTicks(ticks), TimerTickBasis.ELAPSED, action, replace);
    }
    public TimerDefinition onRemainingTick(TimerAction action, boolean replace, int... ticks) {
        return addBinding(remainingBindings, normalizeTicks(ticks), TimerTickBasis.REMAINING, action, replace);
    }
    public TimerDefinition onStarted(TimerAction action) { startedActions.add(action); return this; }
    public TimerDefinition onPaused(TimerAction action) { pausedActions.add(action); return this; }
    public TimerDefinition onResumed(TimerAction action) { resumedActions.add(action); return this; }
    public TimerDefinition onReset(TimerAction action) { resetActions.add(action); return this; }
    public TimerDefinition onStopped(TimerAction action) { stoppedActions.add(action); return this; }
    public TimerDefinition onCompleted(TimerAction action) { completedActions.add(action); return this; }
    public boolean removeBinding(int bindingId) { return elapsedBindings.remove(bindingId) != null || remainingBindings.remove(bindingId) != null; }

    private TimerDefinition addBinding(Map<Integer, TimerBinding> bindings, Set<Long> ticks, TimerTickBasis basis, TimerAction action, boolean replace) {
        if (ticks == null || ticks.isEmpty() || action == null || basis == null) {
            return this;
        }
        int id = nextBindingId++;
        if (!replace && bindings.values().stream().anyMatch(binding -> binding.basis() == basis && !Collections.disjoint(binding.ticks(), ticks))) {
            return this;
        }
        bindings.put(id, new TimerBinding(id, ticks, basis, action));
        return this;
    }

    private static Set<Long> normalizeTicks(int... ticks) {
        if (ticks == null || ticks.length == 0) {
            return Set.of();
        }
        Set<Long> normalized = new LinkedHashSet<>();
        for (int tick : ticks) {
            if (tick < 0) {
                continue;
            }
            normalized.add((long) tick);
        }
        return normalized;
    }

    private static UUID stableUuid(String token) {
        return UUID.nameUUIDFromBytes(token.getBytes(StandardCharsets.UTF_8));
    }
}
