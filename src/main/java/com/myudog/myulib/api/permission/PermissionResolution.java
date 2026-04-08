package com.myudog.myulib.api.permission;

public record PermissionResolution(PermissionDecision decision, PermissionLayer layer, String sourceId, String ruleId) {
    public static PermissionResolution pass() {
        return new PermissionResolution(PermissionDecision.PASS, null, null, null);
    }

    public boolean isDenied() {
        return decision == PermissionDecision.DENY;
    }
}

