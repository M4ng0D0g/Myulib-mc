package com.myudog.myulib.api.permission;

public final class WorldInteractionPermissionHooks {
    private WorldInteractionPermissionHooks() {
    }

    public static PermissionResolution evaluate(WorldInteractionPermissionContext context) {
        return PermissionManager.evaluate(context.toPermissionContext());
    }

    public static boolean isDenied(WorldInteractionPermissionContext context) {
        return PermissionManager.isDenied(context.toPermissionContext());
    }
}

