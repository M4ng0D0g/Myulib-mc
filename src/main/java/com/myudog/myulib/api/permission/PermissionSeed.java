package com.myudog.myulib.api.permission;

import java.util.Objects;

public record PermissionSeed(PermissionLayer layer, String scopeId, PermissionGrant grant) {
    public PermissionSeed {
        layer = Objects.requireNonNull(layer, "layer");
        scopeId = scopeId == null ? "" : scopeId;
        grant = Objects.requireNonNull(grant, "grant");
    }
}

