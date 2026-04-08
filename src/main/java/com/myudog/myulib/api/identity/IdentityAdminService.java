package com.myudog.myulib.api.identity;

import com.myudog.myulib.api.ui.ConfigurationUiBridge;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.UnaryOperator;

public final class IdentityAdminService {
    private IdentityAdminService() {
    }

    public static IdentityGroupDefinition create(IdentityGroupDefinition group) {
        return IdentityManager.register(group);
    }

    public static IdentityGroupDefinition delete(String groupId) {
        return IdentityManager.unregister(groupId);
    }

    public static IdentityGroupDefinition update(String groupId, UnaryOperator<IdentityGroupDefinition> updater) {
        return IdentityManager.update(groupId, updater);
    }

    public static boolean assign(UUID playerId, String groupId) {
        return IdentityManager.assign(playerId, groupId);
    }

    public static boolean revoke(UUID playerId, String groupId) {
        return IdentityManager.revoke(playerId, groupId);
    }

    public static Set<String> groupIdsOf(UUID playerId) {
        return IdentityManager.groupIdsOf(playerId);
    }

    public static List<IdentityGroupDefinition> groupsOf(UUID playerId) {
        return IdentityManager.groupsOf(playerId);
    }

    public static Map<String, IdentityGroupDefinition> list() {
        return IdentityManager.snapshot();
    }

    public static void openEditor(String groupId, ConfigurationUiBridge ui) {
        if (ui != null) {
            ui.openIdentityGroupEditor(groupId);
        }
    }
}

