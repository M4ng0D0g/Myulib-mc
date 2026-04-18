package com.myudog.myulib.internal.event;

import com.myudog.myulib.api.event.Event;
import com.myudog.myulib.api.event.EventPriority;
import com.myudog.myulib.api.event.ProcessResult;
import com.myudog.myulib.api.event.listener.EventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDispatcherImpl {
    private static final class PrioritizedListener<T extends Event> implements Comparable<PrioritizedListener<T>> {
        private final int priority;
        private final EventListener<T> listener;

        private PrioritizedListener(int priority, EventListener<T> listener) {
            this.priority = priority;
            this.listener = listener;
        }

        @Override
        public int compareTo(PrioritizedListener<T> other) {
            return Integer.compare(this.priority, other.priority);
        }
    }

    private final Map<Class<? extends Event>, List<PrioritizedListener<? extends Event>>> listeners = new HashMap<>();

    public <T extends Event> EventListener<T> subscribe(Class<T> eventType, EventListener<T> listener) {
        return subscribe(eventType, EventPriority.NORMAL, listener);
    }

    public <T extends Event> EventListener<T> subscribe(Class<T> eventType, int priority, EventListener<T> listener) {
        List<PrioritizedListener<? extends Event>> list = listeners.computeIfAbsent(eventType, key -> new ArrayList<>());
        list.add(new PrioritizedListener<>(priority, listener));
        list.sort(Comparator.comparingInt(entry -> entry.priority));
        return listener;
    }

    public <T extends Event> void unsubscribe(Class<T> eventType, EventListener<T> listener) {
        List<PrioritizedListener<? extends Event>> list = listeners.get(eventType);
        if (list == null) {
            return;
        }
        list.removeIf(entry -> entry.listener.equals(listener));
        if (list.isEmpty()) {
            listeners.remove(eventType);
        }
    }

    public ProcessResult dispatch(Event event) {
        List<PrioritizedListener<? extends Event>> list = listeners.get(event.getClass());
        if (list == null || list.isEmpty()) {
            return ProcessResult.PASS;
        }

        List<PrioritizedListener<? extends Event>> snapshot = new ArrayList<>(list);
        for (PrioritizedListener<? extends Event> wrapper : snapshot) {
            @SuppressWarnings("unchecked")
            PrioritizedListener<Event> typedWrapper = (PrioritizedListener<Event>) wrapper;
            ProcessResult result = typedWrapper.listener.handle(event);
            if (result != ProcessResult.PASS) {
                return result;
            }
        }
        return ProcessResult.PASS;
    }

    public void clear() {
        listeners.clear();
    }
}
