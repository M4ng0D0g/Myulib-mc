package com.myudog.myulib.api.permission;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record PermissionContext(
    UUID playerId,
    String permissionNode,
    String dimensionId,
    String fieldId,
    Map<String, String> metadata
) {
    public PermissionContext(UUID playerId, String permissionNode, String dimensionId, String fieldId, Map<String, String> metadata) {
        this.playerId = Objects.requireNonNull(playerId, "playerId");
        this.permissionNode = Objects.requireNonNullElse(permissionNode, "*");
        this.dimensionId = dimensionId == null ? "" : dimensionId;
        this.fieldId = fieldId == null ? "" : fieldId;
        this.metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
    }
}


