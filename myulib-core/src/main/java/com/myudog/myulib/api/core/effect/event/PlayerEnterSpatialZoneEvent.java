package com.myudog.myulib.api.core.effect.event;

import com.myudog.myulib.api.core.effect.SpatialEffect;
import com.myudog.myulib.api.core.event.IEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

public record PlayerEnterSpatialZoneEvent(
        Identifier zoneId,
        ServerPlayer player,
        SpatialEffect effect
) implements IEvent {
}

