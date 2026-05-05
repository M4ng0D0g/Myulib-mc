package com.myudog.myulib.api.rolegroup;

import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public record RoleGroupDefinition(
        @NotNull UUID uuid,
        @NotNull MutableComponent translationKey,
        int priority, // 數值越大，覆蓋權限的優先級越高
        Map<String, String> metadata,
        Set<UUID> members
) {
    public static final String ROUTE = "rolegroup";

    public RoleGroupDefinition {
        metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
        members = members == null ? Set.of() : Set.copyOf(members);
    }

    public RoleGroupDefinition(@NotNull String token, @NotNull MutableComponent translationKey, int priority, Map<String, String> metadata, Set<UUID> members) {
        this(stableUuid(token), translationKey, priority, metadata, members);
    }

    public RoleGroupDefinition(@NotNull net.minecraft.resources.Identifier id, @NotNull MutableComponent translationKey, int priority, Map<String, String> metadata, Set<UUID> members) {
        this(stableUuid(id.toString()), translationKey, priority, metadata, members);
    }

    public UUID id() {
        return uuid;
    }

    public UUID token() {
        return uuid;
    }

    public boolean hasMember(UUID playerId) {
        return members != null && members.contains(playerId);
    }

    private static UUID stableUuid(String token) {
        return UUID.nameUUIDFromBytes(token.getBytes(StandardCharsets.UTF_8));
    }
}