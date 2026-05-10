package com.myudog.myulib.api.core.effect.zone;

import com.myudog.myulib.api.core.effect.SpatialEffect;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public record AabbSpatialZone(
        Identifier id,
        ResourceKey<Level> dimension,
        SpatialEffect effect,
        AABB bounds
) implements SpatialZone {

    @Override
    public boolean contains(ServerPlayer player) {
        if (player.level().dimension() != dimension) {
            return false;
        }
        return bounds.contains(player.position());
    }
}

