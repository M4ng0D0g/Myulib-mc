package com.myudog.myulib.api.field;

import java.util.Map;
import java.util.Objects;

public record FieldDefinition(
    String id,
    String ownerId,
    String dimensionId,
    FieldBounds bounds,
    FieldRole role,
    Map<String, String> metadata
) {
    public FieldDefinition {
        id = Objects.requireNonNull(id, "id");
        ownerId = Objects.requireNonNull(ownerId, "ownerId");
        dimensionId = dimensionId == null ? "" : dimensionId;
        bounds = Objects.requireNonNull(bounds, "bounds");
        role = role == null ? FieldRole.SUB : role;
        metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
    }

    public FieldDefinition withRole(FieldRole newRole) {
        return new FieldDefinition(id, ownerId, dimensionId, bounds, newRole, metadata);
    }

    public FieldDefinition withDimensionId(String newDimensionId) {
        return new FieldDefinition(id, ownerId, newDimensionId, bounds, role, metadata);
    }
}


