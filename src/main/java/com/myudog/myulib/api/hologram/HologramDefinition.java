package com.myudog.myulib.api.hologram;

import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public record HologramDefinition(
        @NotNull Identifier id,
        @NotNull Identifier dimensionId,
        @NotNull AABB bounds,
        String label,
        @NotNull HologramStyle style
) {
    public HologramDefinition(@NotNull Identifier id,
                              @NotNull Identifier dimensionId,
                              @NotNull AABB bounds,
                              String label) {
        this(id, dimensionId, bounds, label, HologramStyle.defaults());
    }
}