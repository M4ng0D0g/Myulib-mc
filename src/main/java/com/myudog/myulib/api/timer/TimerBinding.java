package com.myudog.myulib.api.timer;

import java.util.LinkedHashSet;
import java.util.Set;

public record TimerBinding(int id, Set<Long> ticks, TimerTickBasis basis, TimerAction action) {
	public TimerBinding {
		ticks = ticks == null ? Set.of() : Set.copyOf(new LinkedHashSet<>(ticks));
	}

	public boolean matches(long tick) {
		return ticks.contains(tick);
	}

	// Compatibility helper for old call sites that treated a binding as single-tick.
	public long tick() {
		return ticks.isEmpty() ? -1L : ticks.iterator().next();
	}
}
