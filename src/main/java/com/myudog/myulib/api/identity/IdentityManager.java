package com.myudog.myulib.api.identity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.UnaryOperator;

public final class IdentityManager {
    private static final Map<String, IdentityGroupDefinition> GROUPS = new LinkedHashMap<>();
    private static final Map<UUID, Set<String>> PLAYER_GROUPS = new LinkedHashMap<>();

    private IdentityManager() {
    }

    public static void install() {
    }

    public static IdentityGroupDefinition register(IdentityGroupDefinition group) {
        Objects.requireNonNull(group, "group");
        GROUPS.put(group.id(), group);
        return group;
    }

    public static IdentityGroupDefinition update(String groupId, UnaryOperator<IdentityGroupDefinition> updater) {
        Objects.requireNonNull(groupId, "groupId");
        Objects.requireNonNull(updater, "updater");
        IdentityGroupDefinition existing = GROUPS.get(groupId);
        if (existing == null) {
            return null;
        }
        IdentityGroupDefinition updated = Objects.requireNonNull(updater.apply(existing), "updated group");
        GROUPS.put(groupId, updated);
        return updated;
    }

    public static IdentityGroupDefinition unregister(String groupId) {
        return GROUPS.remove(groupId);
    }

    public static IdentityGroupDefinition get(String groupId) {
        return GROUPS.get(groupId);
    }

    public static List<IdentityGroupDefinition> groups() {
        return List.copyOf(GROUPS.values());
    }

    public static Map<String, IdentityGroupDefinition> snapshot() {
        return Map.copyOf(GROUPS);
    }

    public static boolean assign(UUID playerId, String groupId) {
        Objects.requireNonNull(playerId, "playerId");
        Objects.requireNonNull(groupId, "groupId");
        if (!GROUPS.containsKey(groupId)) {
            return false;
        }
        return PLAYER_GROUPS.computeIfAbsent(playerId, ignored -> new LinkedHashSet<>()).add(groupId);
    }

    public static boolean revoke(UUID playerId, String groupId) {
        Set<String> groups = PLAYER_GROUPS.get(playerId);
        if (groups == null) {
            return false;
        }
        boolean removed = groups.remove(groupId);
        if (groups.isEmpty()) {
            PLAYER_GROUPS.remove(playerId);
        }
        return removed;
    }

    public static Set<String> groupIdsOf(UUID playerId) {
        Set<String> groups = PLAYER_GROUPS.get(playerId);
        return groups == null ? Set.of() : Collections.unmodifiableSet(new LinkedHashSet<>(groups));
    }

    public static List<IdentityGroupDefinition> groupsOf(UUID playerId) {
        Set<String> groupIds = PLAYER_GROUPS.get(playerId);
        if (groupIds == null || groupIds.isEmpty()) {
            return List.of();
        }
        List<IdentityGroupDefinition> result = new ArrayList<>();
        for (String groupId : groupIds) {
            IdentityGroupDefinition definition = GROUPS.get(groupId);
            if (definition != null) {
                result.add(definition);
            }
        }
        result.sort((a, b) -> Integer.compare(b.priority(), a.priority()));
        return List.copyOf(result);
    }

    public static void clear() {
        GROUPS.clear();
        PLAYER_GROUPS.clear();
    }
}


