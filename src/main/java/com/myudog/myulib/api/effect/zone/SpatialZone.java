package com.myudog.myulib.api.effect.zone;

import com.myudog.myulib.api.effect.SpatialEffect;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public interface SpatialZone {
    Identifier id();

    ResourceKey<Level> dimension();

    SpatialEffect effect();

    boolean contains(ServerPlayer player);
}

