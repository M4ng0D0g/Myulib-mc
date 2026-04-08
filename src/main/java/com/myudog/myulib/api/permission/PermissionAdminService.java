package com.myudog.myulib.api.permission;

import com.myudog.myulib.api.ui.ConfigurationUiBridge;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.UnaryOperator;

public final class PermissionAdminService {
    private PermissionAdminService() {
    }

    public static PermissionGrant grantGlobal(PermissionGrant grant) {
        return PermissionManager.grantGlobal(grant);
    }

    public static PermissionGrant grantDimension(String dimensionId, PermissionGrant grant) {
        return PermissionManager.grantDimension(dimensionId, grant);
    }

    public static PermissionGrant grantField(String fieldId, PermissionGrant grant) {
        return PermissionManager.grantField(fieldId, grant);
    }

    public static PermissionGrant grantUser(UUID playerId, PermissionGrant grant) {
        return PermissionManager.grantUser(playerId, grant);
    }

    public static PermissionGrant updateGlobal(String grantId, UnaryOperator<PermissionGrant> updater) {
        return PermissionManager.updateGlobal(grantId, updater);
    }

    public static PermissionGrant updateDimension(String dimensionId, String grantId, UnaryOperator<PermissionGrant> updater) {
        return PermissionManager.updateDimension(dimensionId, grantId, updater);
    }

    public static PermissionGrant updateField(String fieldId, String grantId, UnaryOperator<PermissionGrant> updater) {
        return PermissionManager.updateField(fieldId, grantId, updater);
    }

    public static PermissionGrant updateUser(UUID playerId, String grantId, UnaryOperator<PermissionGrant> updater) {
        return PermissionManager.updateUser(playerId, grantId, updater);
    }

    public static PermissionResolution evaluate(PermissionContext context) {
        return PermissionManager.evaluate(context);
    }

    public static boolean isDenied(PermissionContext context) {
        return PermissionManager.isDenied(context);
    }

    public static PermissionResolution evaluate(WorldInteractionPermissionContext context) {
        return PermissionManager.evaluate(context);
    }

    public static boolean isDenied(WorldInteractionPermissionContext context) {
        return PermissionManager.isDenied(context);
    }

    public static List<PermissionGrant> globalRules() {
        return PermissionManager.globalRules();
    }

    public static Map<String, List<PermissionGrant>> dimensionRules() {
        return PermissionManager.dimensionRules();
    }

    public static Map<String, List<PermissionGrant>> fieldRules() {
        return PermissionManager.fieldRules();
    }

    public static Map<UUID, List<PermissionGrant>> userRules() {
        return PermissionManager.userRules();
    }

    public static void openEditor(PermissionLayer layer, String scopeId, ConfigurationUiBridge ui) {
        if (ui != null) {
            ui.openPermissionEditor(layer, scopeId);
        }
    }
}

