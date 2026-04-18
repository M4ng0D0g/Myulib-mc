package com.myudog.myulib.api.game.event;

import com.myudog.myulib.api.event.Event;
import com.myudog.myulib.api.game.object.IGameObject;
import net.minecraft.world.damagesource.DamageSource;

public record GameObjectDamageEvent(IGameObject target, DamageSource source, float amount) implements Event {}
