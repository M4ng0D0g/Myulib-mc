package com.myudog.myulib.api.permission;

public enum PermissionLayer {
    GLOBAL,
    DIMENSION,
    FIELD,
    USER;

    public boolean isHigherThan(PermissionLayer other) {
        return other != null && ordinal() > other.ordinal();
    }
}

