package com.myudog.myulib.api.events;

import com.myudog.myulib.api.core.ecs.IComponent;
import com.myudog.myulib.api.core.event.IEvent;

public class ComponentAddedEvent implements IEvent {
    private final int entityId;
    private final IComponent component;

    public ComponentAddedEvent(int entityId, IComponent component) {
        this.entityId = entityId;
        this.component = component;
    }

    public int getEntityId() {
        return entityId;
    }

    public IComponent getComponent() {
        return component;
    }
}
