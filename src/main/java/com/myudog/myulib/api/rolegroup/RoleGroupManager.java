package com.myudog.myulib.api.rolegroup;

import com.myudog.myulib.api.debug.DebugFeature;
import com.myudog.myulib.api.debug.DebugLogManager;
import com.myudog.myulib.api.rolegroup.storage.NbtRoleGroupStorage;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;
import java.nio.charset.StandardCharsets;

public final class RoleGroupManager {

    public static final RoleGroupManager INSTANCE = new RoleGroupManager();

    // 🌟 記憶體狀態
    private final Map<UUID, RoleGroupDefinition> GROUPS = new ConcurrentHashMap<>();
    private final Map<UUID, Set<UUID>> PLAYER_GROUPS = new ConcurrentHashMap<>();

    // 🌟 注入你的專用 Storage 介面 (作為 DAO)
    private RoleGroupStorage storage;

    private RoleGroupManager() {}

    public void install() {
        install(new NbtRoleGroupStorage());
    }

    public void install(RoleGroupStorage storageProvider) {
        storage = storageProvider;

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            if (storage != null) {
                storage.initialize(server);
                GROUPS.clear();
                PLAYER_GROUPS.clear();

                // 將 DAO 的資料全部載入記憶體
                GROUPS.putAll(storage.loadGroups());
                PLAYER_GROUPS.putAll(storage.loadAssignments());
            }

            // 系統啟動時，確保預設的 everyone 身分組存在
            UUID everyoneUuid = UUID.nameUUIDFromBytes("everyone".getBytes());
            if (!GROUPS.containsKey(everyoneUuid)) {
                MutableComponent translationKey = Component.translatable("myulib.rolegroup.everyone");
                register(new RoleGroupDefinition(everyoneUuid, translationKey, -999, Map.of(), Set.of()));
            }
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> save());
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> clear());
    }

    public RoleGroupDefinition register(RoleGroupDefinition group) {
        if (!validate(group)) {
            throw new IllegalArgumentException("RoleGroupDefinition 驗證失敗: " + (group == null ? "null" : group.uuid()));
        }

        GROUPS.put(group.uuid(), group);
        DebugLogManager.INSTANCE.log(DebugFeature.ROLEGROUP,
                "register uuid=" + group.uuid() + ",priority=" + group.priority());
        if (storage != null) storage.saveGroup(group);
        return group;
    }

    public boolean validate(RoleGroupDefinition group) {
        return group != null && !GROUPS.containsKey(group.uuid());
    }

    public RoleGroupDefinition update(UUID groupUuid, UnaryOperator<RoleGroupDefinition> updater) {
        RoleGroupDefinition existing = GROUPS.get(groupUuid);
        if (existing == null) return null;
        RoleGroupDefinition updated = updater.apply(existing);
        GROUPS.put(groupUuid, updated);
        if (storage != null) storage.saveGroup(updated);
        return updated;
    }

    public RoleGroupDefinition update(net.minecraft.resources.Identifier groupUuid, UnaryOperator<RoleGroupDefinition> updater) {
        return update(stableUuid(groupUuid.toString()), updater);
    }

    public RoleGroupDefinition delete(UUID groupUuid) {
        DebugLogManager.INSTANCE.log(DebugFeature.ROLEGROUP, "delete uuid=" + groupUuid);
        if (storage != null) storage.deleteGroup(groupUuid);

        // 同時從所有玩家身上移除該身分組
        PLAYER_GROUPS.values().forEach(set -> set.remove(groupUuid));
        return GROUPS.remove(groupUuid);
    }

    public RoleGroupDefinition delete(net.minecraft.resources.Identifier groupUuid) {
        return delete(stableUuid(groupUuid.toString()));
    }

    public RoleGroupDefinition get(UUID groupUuid) { return GROUPS.get(groupUuid); }
    public RoleGroupDefinition get(net.minecraft.resources.Identifier groupUuid) { return get(stableUuid(groupUuid.toString())); }
    public List<RoleGroupDefinition> groups() { return List.copyOf(GROUPS.values()); }

    public boolean assign(UUID playerId, UUID groupUuid) {
        boolean added = PLAYER_GROUPS.computeIfAbsent(playerId, ignored -> new LinkedHashSet<>()).add(groupUuid);
        if (added) {
            DebugLogManager.INSTANCE.log(DebugFeature.ROLEGROUP, "assign player=" + playerId + " -> group=" + groupUuid);
        }
        if (added && storage != null) storage.saveAssignments(playerId, PLAYER_GROUPS.get(playerId));
        return added;
    }

    public boolean assign(UUID playerId, net.minecraft.resources.Identifier groupUuid) {
        return assign(playerId, stableUuid(groupUuid.toString()));
    }

    public boolean revoke(UUID playerId, UUID groupUuid) {
        Set<UUID> groups = PLAYER_GROUPS.get(playerId);
        if (groups != null && groups.remove(groupUuid)) {
            DebugLogManager.INSTANCE.log(DebugFeature.ROLEGROUP, "revoke player=" + playerId + " -> group=" + groupUuid);
            if (storage != null) storage.saveAssignments(playerId, groups);
            return true;
        }
        return false;
    }

    public boolean revoke(UUID playerId, net.minecraft.resources.Identifier groupUuid) {
        return revoke(playerId, stableUuid(groupUuid.toString()));
    }

    // --- 🎯 雙向查詢系統 (全記憶體運算，瞬間完成) ---

    public Set<UUID> getPlayersInGroup(UUID groupUuid) {
        Set<UUID> result = new HashSet<>();
        for (Map.Entry<UUID, Set<UUID>> entry : PLAYER_GROUPS.entrySet()) {
            if (entry.getValue().contains(groupUuid)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    public Set<UUID> getPlayersInGroup(net.minecraft.resources.Identifier groupUuid) {
        return getPlayersInGroup(stableUuid(groupUuid.toString()));
    }

    public List<RoleGroupDefinition> getSortedGroupsOf(UUID playerId) {
        Set<UUID> assignedUuids = PLAYER_GROUPS.getOrDefault(playerId, Set.of());

        List<RoleGroupDefinition> assignedGroups = new ArrayList<>();
        for (UUID uuid : assignedUuids) {
            RoleGroupDefinition def = GROUPS.get(uuid);
            if (def != null) assignedGroups.add(def);
        }

        // 依據 priority 排序
        assignedGroups.sort((a, b) -> Integer.compare(b.priority(), a.priority()));

        return assignedGroups;
    }

    public List<String> getSortedGroupIdsOf(net.minecraft.resources.Identifier playerId) {
        return getSortedGroupIdsOf(stableUuid(playerId.toString()));
    }

    public List<String> getSortedGroupIdsOf(UUID playerId) {
        return getSortedGroupsOf(playerId).stream().map(group -> group.uuid().toString()).toList();
    }

    public void save() {
        // 如果有批次存檔需求可實作
    }

    public void clear() {
        GROUPS.clear();
        PLAYER_GROUPS.clear();
    }

    private static UUID stableUuid(String token) {
        return UUID.nameUUIDFromBytes(token.getBytes(StandardCharsets.UTF_8));
    }
}