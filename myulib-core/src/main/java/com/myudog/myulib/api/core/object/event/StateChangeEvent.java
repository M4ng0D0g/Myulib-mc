package com.myudog.myulib.api.core.object.event;

import com.myudog.myulib.api.core.state.IState;
import com.myudog.myulib.api.core.event.IEvent;

/**
 * Fired when the state of an object changes.
 * Completely decoupled from GameInstance.
 */
public record StateChangeEvent<S extends IState<?>>(
        Object source,
        S previousState,
        S newState
) implements IEvent {
}
