package com.myudog.myulib.api.core.event;

@FunctionalInterface
public interface IEventListener<T extends IEvent> {
    ProcessResult handle(T event);
}
