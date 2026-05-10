package com.myudog.myulib.api.core.ui;

import com.myudog.myulib.api.framework.permission.ScopeLayer;
import net.minecraft.resources.Identifier;

public final class ConfigurationUiRegistry {
    private static volatile ConfigurationUiBridge bridge;

    private ConfigurationUiRegistry() {
    }

    public static void setBridge(ConfigurationUiBridge bridge) {
        ConfigurationUiRegistry.bridge = bridge;
    }

    public static ConfigurationUiBridge bridge() {
        return bridge;
    }

    public static void openFieldEditor(Identifier fieldId) {
        ConfigurationUiBridge current = bridge;
        if (current != null) {
            current.openFieldEditor(fieldId);
        }
    }

    public static void openIdentityGroupEditor(Identifier groupId) {
        ConfigurationUiBridge current = bridge;
        if (current != null) {
            current.openIdentityGroupEditor(groupId);
        }
    }

    public static void openRoleGroupEditor(Identifier groupId) {
        ConfigurationUiBridge current = bridge;
        if (current != null) {
            current.openRoleGroupEditor(groupId);
        }
    }

    public static void openTeamEditor(Identifier teamId) {
        ConfigurationUiBridge current = bridge;
        if (current != null) {
            current.openTeamEditor(teamId);
        }
    }

    public static void openPermissionEditor(ScopeLayer layer, String scopeId) {
        ConfigurationUiBridge current = bridge;
        if (current != null) {
            current.openPermissionEditor(layer, scopeId);
        }
    }
}
