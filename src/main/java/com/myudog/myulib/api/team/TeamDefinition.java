package com.myudog.myulib.api.team;

import com.myudog.myulib.api.core.Token;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public record TeamDefinition(
        @NotNull UUID uuid,
        @NotNull MutableComponent translationKey,
        @NotNull TeamColor color,
        Map<TeamFlag, Boolean> flags,
        int playerLimit
) {
    public static final String ROUTE = "team";

    public TeamDefinition {
        // 優化 Flags 儲存
        EnumMap<TeamFlag, Boolean> optimizedFlags = new EnumMap<>(TeamFlag.class);
        if (flags != null) optimizedFlags.putAll(flags);
        flags = optimizedFlags;

        if (playerLimit < 0) throw new IllegalArgumentException("playerLimit 必須為 0 (無限制) 或正整數");
    }

    public TeamDefinition(@NotNull String token, @NotNull MutableComponent translationKey, @NotNull TeamColor color, Map<TeamFlag, Boolean> flags) {
        this(stableUuid(token), translationKey, color, flags, 0);
    }

    public TeamDefinition(@NotNull String token, @NotNull MutableComponent translationKey, @NotNull TeamColor color, Map<TeamFlag, Boolean> flags, int playerLimit) {
        this(stableUuid(token), translationKey, color, flags, playerLimit);
    }

    public TeamDefinition(@NotNull net.minecraft.resources.Identifier id, @NotNull MutableComponent translationKey, @NotNull TeamColor color, Map<TeamFlag, Boolean> flags, int playerLimit) {
        this(stableUuid(id.toString()), translationKey, color, flags, playerLimit);
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