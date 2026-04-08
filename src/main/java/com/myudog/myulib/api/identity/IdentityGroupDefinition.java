package com.myudog.myulib.api.identity;

import com.myudog.myulib.api.permission.PermissionGrant;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public record IdentityGroupDefinition(
    String id,
    String displayName,
    int priority,
    List<PermissionGrant> grants,
    Map<String, String> metadata
) {
    public IdentityGroupDefinition {
        id = Objects.requireNonNull(id, "id");
        displayName = displayName == null ? id : displayName;
        grants = grants == null ? List.of() : List.copyOf(grants);
        metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
    }
}

