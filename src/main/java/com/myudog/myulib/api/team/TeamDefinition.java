package com.myudog.myulib.api.team;

import net.minecraft.network.chat.MutableComponent;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public record TeamDefinition(
        String id,
        MutableComponent translationKey,
        TeamColor color,
        Map<TeamFlag, Boolean> flags
) {
    public TeamDefinition {
        EnumMap<TeamFlag, Boolean> optimizedFlags = new EnumMap<>(TeamFlag.class);
        if (flags != null && !flags.isEmpty()) {
            optimizedFlags.putAll(flags);
        }
        flags = optimizedFlags;
    }
}

