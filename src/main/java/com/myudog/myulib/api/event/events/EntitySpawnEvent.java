package com.myudog.myulib.api.event.events;

import com.myudog.myulib.api.event.FailableEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class EntitySpawnEvent implements FailableEvent {
    private final Entity entity;
    private final Level Level;
    private String errorMessage;

    public EntitySpawnEvent(Entity entity, Level Level) {
        this(entity, Level, null);
    }

    public EntitySpawnEvent(Entity entity, Level Level, String errorMessage) {
        this.entity = entity;
        this.Level = Level;
        this.errorMessage = errorMessage;
    }

    public Entity getEntity() {
        return entity;
    }

    public Level getLevel() {
        return Level;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}


