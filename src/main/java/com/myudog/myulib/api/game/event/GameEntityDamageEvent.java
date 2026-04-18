package com.myudog.myulib.api.game.event;

import com.myudog.myulib.api.event.Event;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public record GameEntityDamageEvent(
        LivingEntity victim,
        DamageSource source,
        float amount
) implements Event {
}

