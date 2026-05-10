package com.myudog.myulib.api.core.effect.event;

import com.myudog.myulib.api.core.event.IEvent;
import com.myudog.myulib.api.core.effect.SpatialEffect;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

public record PlayerLeaveSpatialZoneEvent(
        Identifier zoneId,
        ServerPlayer player,
        SpatialEffect effect
) implements IEvent {
}

