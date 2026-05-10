package com.myudog.myulib.api.core.object.event;

import com.myudog.myulib.api.core.event.IEvent;
import com.myudog.myulib.api.core.object.IObjectRt;
import net.minecraft.world.damagesource.DamageSource;

public record ObjectDeathEvent(IObjectRt target, DamageSource source) implements IEvent {}
