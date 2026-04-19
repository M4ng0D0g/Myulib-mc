package com.myudog.myulib.api.team;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public record TeamDefinition(
        @NotNull Identifier id,
        @NotNull MutableComponent translationKey,
        @NotNull TeamColor color,
        Map<TeamFlag, Boolean> flags,
        int playerLimit
) {
    public TeamDefinition(@NotNull Identifier id,
                          @NotNull MutableComponent translationKey,
                          @NotNull TeamColor color,
                          Map<TeamFlag, Boolean> flags) {
        this(id, translationKey, color, flags, 0);
    }

    public TeamDefinition {
        EnumMap<TeamFlag, Boolean> optimizedFlags = new EnumMap<>(TeamFlag.class);
        if (flags != null && !flags.isEmpty()) {
            optimizedFlags.putAll(flags);
        }
        flags = optimizedFlags;

        if (playerLimit < 0) {
            throw new IllegalArgumentException("playerLimit must be 0 (unlimited) or > 0");
        }
    }
}

