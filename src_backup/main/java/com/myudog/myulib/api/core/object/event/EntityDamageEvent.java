package com.myudog.myulib.api.core.object.event;

import com.myudog.myulib.api.core.event.IEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public final class EntityDamageEvent implements IEvent {
    private final LivingEntity victim;
    private final DamageSource source;
    private final float amount;
    private boolean canceled;

    public EntityDamageEvent(LivingEntity victim, DamageSource source, float amount) {
        this.victim = victim;
        this.source = source;
        this.amount = amount;
        this.canceled = false;
    }

    public LivingEntity getVictim() {
        return victim;
    }

    public DamageSource getSource() {
        return source;
    }

    public float getAmount() {
        return amount;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}

