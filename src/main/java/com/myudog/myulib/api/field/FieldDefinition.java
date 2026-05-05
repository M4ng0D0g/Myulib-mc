package com.myudog.myulib.api.field;

import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.nio.charset.StandardCharsets;

/**
 * 定義一個獨立的遊戲/保護區域。
 * 權限相關設定交由外部 Permission 系統處理，可將資料存放於 fieldData 中。
 */
public record FieldDefinition(
        @NotNull UUID uuid,
        @NotNull Identifier dimensionId, // 例如: minecraft:overworld
        @NotNull AABB bounds,            // 核心：原版碰撞箱
        Map<String, Object> fieldData // 未來放置權限表 (RoleGroup, Enum 狀態) 的擴充槽
) {
    public static final String ROUTE = "field";

    public FieldDefinition {
        fieldData = fieldData == null ? new HashMap<>() : new HashMap<>(fieldData);
    }

    public FieldDefinition(@NotNull String token, @NotNull Identifier dimensionId, @NotNull AABB bounds, Map<String, Object> fieldData) {
        this(stableUuid(token), dimensionId, bounds, fieldData);
    }

    public FieldDefinition(@NotNull Identifier id, @NotNull Identifier dimensionId, @NotNull AABB bounds, Map<String, Object> fieldData) {
        this(stableUuid(id.toString()), dimensionId, bounds, fieldData);
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