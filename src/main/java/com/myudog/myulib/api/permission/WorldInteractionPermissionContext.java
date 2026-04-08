package com.myudog.myulib.api.permission;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record WorldInteractionPermissionContext(
    UUID playerId,
    PermissionAction action,
    String targetType,
    String targetId,
    String dimensionId,
    String fieldId,
    Map<String, String> metadata
) {
    public WorldInteractionPermissionContext(UUID playerId,
                                             PermissionAction action,
                                             String targetType,
                                             String targetId,
                                             String dimensionId,
                                             String fieldId,
                                             Map<String, String> metadata) {
        this.playerId = Objects.requireNonNull(playerId, "playerId");
        this.action = Objects.requireNonNull(action, "action");
        this.targetType = targetType == null ? "" : targetType;
        this.targetId = targetId == null ? "" : targetId;
        this.dimensionId = dimensionId == null ? "" : dimensionId;
        this.fieldId = fieldId == null ? "" : fieldId;
        this.metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
    }

    public String permissionNode() {
        StringBuilder builder = new StringBuilder("world.").append(action.name().toLowerCase());
        if (!targetType.isBlank()) {
            builder.append('.').append(targetType.toLowerCase());
        }
        if (!targetId.isBlank()) {
            builder.append('.').append(targetId.toLowerCase());
        }
        return builder.toString();
    }

    public PermissionContext toPermissionContext() {
        return new PermissionContext(playerId, permissionNode(), dimensionId, fieldId, metadata);
    }
}


