package com.myudog.myulib.api.framework.game.features;

import com.myudog.myulib.api.framework.game.GameInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.Set;

public interface FieldFeature {
    boolean bindField(@NotNull UUID fieldId);

    boolean isInsideGameBounds(@NotNull Identifier dimensionId, @NotNull Vec3 position);

    Set<UUID> getFieldsAt(@NotNull Identifier dimensionId, @NotNull Vec3 position);

    Set<UUID> getActiveFields();

    void unbindField(@NotNull UUID fieldId);

    void clean(GameInstance<?, ?, ?> instance);
}
