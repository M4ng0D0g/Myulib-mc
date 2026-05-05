package com.myudog.myulib.api.hologram;

import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record HologramDefinition(
        @NotNull UUID uuid,
        @NotNull Identifier dimensionId,
        @NotNull AABB bounds,
        String label,
        @NotNull HologramStyle style
) {
    public static final String ROUTE = "hologram";

    public HologramDefinition(@NotNull UUID uuid,
                              @NotNull Identifier dimensionId,
                              @NotNull AABB bounds,
                              String label) {
        this(uuid, dimensionId, bounds, label, HologramStyle.defaults());
    }

    public HologramDefinition(@NotNull String token,
                              @NotNull Identifier dimensionId,
                              @NotNull AABB bounds,
                              String label,
                              @NotNull HologramStyle style) {
        this(stableUuid(token), dimensionId, bounds, label, style);
    }

    public HologramDefinition(@NotNull Identifier id,
                              @NotNull Identifier dimensionId,
                              @NotNull AABB bounds,
                              String label,
                              @NotNull HologramStyle style) {
        this(stableUuid(id.toString()), dimensionId, bounds, label, style);
    }

    public UUID id() {
        return uuid;
    }

    public UUID token() {
        return uuid;
    }

    private static UUID stableUuid(String token) {
        return UUID.nameUUIDFromBytes(token.getBytes(StandardCharsets.UTF_8));
    }
}