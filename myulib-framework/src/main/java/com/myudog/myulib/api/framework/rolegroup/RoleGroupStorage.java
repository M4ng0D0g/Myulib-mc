package com.myudog.myulib.api.framework.rolegroup;

import net.minecraft.server.MinecraftServer;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface RoleGroupStorage {
    void initialize(MinecraftServer server);

    Map<UUID, RoleGroupDefinition> loadGroups();

    Map<UUID, Set<UUID>> loadAssignments();

    void saveGroup(RoleGroupDefinition group);

    void deleteGroup(UUID groupUuid);

    void saveAssignments(UUID playerId, Set<UUID> groupUuids);
}

