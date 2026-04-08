package com.myudog.myulib.api.permission;

import java.util.Objects;

public record PermissionGrant(String id, PermissionLayer layer, String node, PermissionDecision decision, int priority) {
    public PermissionGrant {
        id = Objects.requireNonNullElse(id, "");
        layer = Objects.requireNonNull(layer, "layer");
        node = Objects.requireNonNullElse(node, "*");
        decision = Objects.requireNonNull(decision, "decision");
    }

    public boolean matches(String requestedNode) {
        return "*".equals(node) || Objects.equals(node, requestedNode);
    }
}


