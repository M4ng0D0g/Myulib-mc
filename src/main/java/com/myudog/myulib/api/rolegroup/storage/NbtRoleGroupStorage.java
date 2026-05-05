package com.myudog.myulib.api.rolegroup.storage;

import com.myudog.myulib.Myulib;
import com.myudog.myulib.api.rolegroup.RoleGroupDefinition;
import com.myudog.myulib.api.rolegroup.RoleGroupStorage;
import com.myudog.myulib.api.util.NbtIoHelper;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 實作 DataStorage 的 NBT 身分組儲存庫。
 * 包含完整的 NBT 讀寫反射，並支援舊版分離式 Memberships 結構的自動遷移。
 */
public class NbtRoleGroupStorage implements RoleGroupStorage {

    private static final String FILE_NAME = "rolegroups.dat";
    private static final String GROUPS_KEY = "groups";

    private Path storageFile;
    private final Map<UUID, RoleGroupDefinition> fileMirror = new ConcurrentHashMap<>();

    // =====================================================================
    // 1. RoleGroupStorage 介面實作
    // =====================================================================

    @Override
    public void initialize(MinecraftServer server) {
        bindRoot(resolveRootPath(server));
    }

    public void bindRoot(Path root) {
        Path rootPath = root == null ? Paths.get(".") : root.toAbsolutePath().normalize();
        this.storageFile = rootPath.resolve(Myulib.MOD_ID).resolve(FILE_NAME);

        try {
            if (this.storageFile != null && !Files.exists(this.storageFile.getParent())) {
                Files.createDirectories(this.storageFile.getParent());
            }
        } catch (Exception e) {
            System.err.println("[Myulib] 無法建立 RoleGroup 儲存目錄: " + e.getMessage());
        }
    }

    private Map<UUID, RoleGroupDefinition> loadAll() {
        fileMirror.clear();
        if (storageFile == null || !Files.exists(storageFile)) {
            return new HashMap<>();
        }

        try {
            CompoundTag root = readRoot(storageFile);
            Tag groupsElement = root.get(GROUPS_KEY);

            if (groupsElement instanceof ListTag groupList) {
                for (int i = 0; i < groupList.size(); i++) {
                    RoleGroupDefinition group = readGroup(groupList.getCompound(i).orElseThrow());
                    fileMirror.put(group.id(), group);
                }
            }

            // 🔄 舊版資料遷移 (Legacy Migration)：如果發現舊版的 memberships 結構，將其整併進對應的 Group 中
            if (root.contains("memberships")) {
                CompoundTag membershipsCompound = root.getCompound("memberships").orElseThrow();
                boolean migrated = false;
                for (String playerKey : keysOf(membershipsCompound)) {
                    Tag entry = membershipsCompound.get(playerKey);
                    if (entry instanceof ListTag list) {
                        for (int i = 0; i < list.size(); i++) {
                            UUID groupId = parseGroupIdCompat(list.getString(i).orElseThrow());
                            RoleGroupDefinition def = fileMirror.get(groupId);
                            if (def != null) {
                                // 將舊版玩家 UUID 塞入新版的 Group Members 中
                                Set<UUID> updatedMembers = new LinkedHashSet<>(def.members());
                                updatedMembers.add(UUID.fromString(playerKey));

                                // 重建 Record
                                RoleGroupDefinition updatedDef = new RoleGroupDefinition(
                                        def.id(), def.translationKey(), def.priority(), def.metadata(), updatedMembers
                                );
                                fileMirror.put(groupId, updatedDef);
                                migrated = true;
                            }
                        }
                    }
                }
                // 如果有遷移發生，立刻存檔以清除舊結構
                if (migrated) saveToFile();
            }

        } catch (Exception e) {
            throw new IllegalStateException("無法讀取 RoleGroup NBT: " + storageFile, e);
        }

        return new HashMap<>(fileMirror);
    }

    private void save(UUID id, RoleGroupDefinition data) {
        fileMirror.put(id, data);
        saveToFile();
    }

    private void delete(UUID id) {
        if (fileMirror.remove(id) != null) {
            saveToFile();
        }
    }

    @Override
    public Map<UUID, RoleGroupDefinition> loadGroups() {
        return loadAll();
    }

    @Override
    public Map<UUID, Set<UUID>> loadAssignments() {
        Map<UUID, Set<UUID>> assignments = new LinkedHashMap<>();
        for (RoleGroupDefinition group : loadAll().values()) {
            for (UUID member : group.members()) {
                assignments.computeIfAbsent(member, ignored -> new LinkedHashSet<>()).add(group.id());
            }
        }
        return assignments;
    }

    @Override
    public void saveGroup(RoleGroupDefinition group) {
        save(group.id(), group);
    }

    @Override
    public void deleteGroup(UUID groupId) {
        delete(groupId);
    }

    @Override
    public void saveAssignments(UUID playerId, Set<UUID> groupIds) {
        if (playerId == null) {
            return;
        }
        for (Map.Entry<UUID, RoleGroupDefinition> entry : new ArrayList<>(fileMirror.entrySet())) {
            RoleGroupDefinition current = entry.getValue();
            Set<UUID> members = new LinkedHashSet<>(current.members());
            boolean shouldContain = groupIds != null && groupIds.contains(entry.getKey());
            if (shouldContain) {
                members.add(playerId);
            } else {
                members.remove(playerId);
            }
            fileMirror.put(entry.getKey(), new RoleGroupDefinition(
                    current.id(),
                    current.translationKey(),
                    current.priority(),
                    current.metadata(),
                    members
            ));
        }
        saveToFile();
    }

    // =====================================================================
    // 2. 內部寫入邏輯與 NBT 序列化轉換
    // =====================================================================

    private synchronized void saveToFile() {
        if (storageFile == null) return;
        try {
            CompoundTag root = new CompoundTag();
            ListTag groupList = new ListTag();

            for (RoleGroupDefinition group : fileMirror.values()) {
                groupList.add(writeGroup(group));
            }

            root.put(GROUPS_KEY, groupList);
            // 💡 不再寫入 MEMBERSHIPS_KEY，資料已完美整合

            writeRoot(storageFile, root);
        } catch (Exception e) {
            throw new IllegalStateException("無法儲存 RoleGroup NBT: " + storageFile, e);
        }
    }

    private static CompoundTag writeGroup(RoleGroupDefinition group) {
        CompoundTag compound = new CompoundTag();
        compound.putString("id", group.id().toString());
        compound.putString("displayName", group.translationKey().getString());
        compound.putInt("priority", group.priority());

        CompoundTag metadataCompound = new CompoundTag();
        for (Map.Entry<String, String> entry : group.metadata().entrySet()) {
            metadataCompound.putString(entry.getKey(), entry.getValue());
        }
        compound.put("metadata", metadataCompound);

        // 🌟 新版核心：直接將玩家名單存在 Group 底下
        CompoundTag membersCompound = new CompoundTag();
        for (UUID member : group.members()) {
            membersCompound.putBoolean(member.toString(), true);
        }
        compound.put("members", membersCompound);

        return compound;
    }

    private static RoleGroupDefinition readGroup(CompoundTag compound) {
        String id = compound.getString("id").orElse("");
        String displayName = compound.getString("displayName").orElse(id);
        int priority = compound.getInt("priority").orElse(0);

        Map<String, String> metadata = new LinkedHashMap<>();
        Tag metadataElement = compound.get("metadata");
        if (metadataElement instanceof CompoundTag metadataCompound) {
            for (String key : keysOf(metadataCompound)) {
                metadata.put(key, metadataCompound.getString(key).orElse(""));
            }
        }

        Set<UUID> members = new LinkedHashSet<>();
        Tag membersElement = compound.get("members");
        if (membersElement instanceof CompoundTag membersCompound) {
            for (String playerKey : keysOf(membersCompound)) {
                members.add(UUID.fromString(playerKey));
            }
        }

        return new RoleGroupDefinition(parseGroupIdCompat(id), Component.literal(displayName), priority, metadata, members);
    }

    private static UUID parseGroupIdCompat(String rawId) {
        if (rawId == null || rawId.isBlank()) {
            return UUID.nameUUIDFromBytes(("myulib:everyone").getBytes(java.nio.charset.StandardCharsets.UTF_8));
        }
        try {
            // Try parsing as UUID directly
            return UUID.fromString(rawId);
        } catch (IllegalArgumentException e) {
            // Fall back to stable UUID generation from string
            return UUID.nameUUIDFromBytes(rawId.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        }
    }

    // =====================================================================
    // 3. NBT 反射底層工具 (安全實作)
    // =====================================================================

    private static CompoundTag readRoot(Path path) throws Exception {
        return NbtIoHelper.readRoot(path);
    }

    private static void writeRoot(Path path, CompoundTag root) throws Exception {
        NbtIoHelper.writeRoot(path, root);
    }

    private static Method findNbtIoMethod(String name, Path path) {
        for (Method method : NbtIo.class.getMethods()) {
            if (!method.getName().equals(name)) continue;
            Class<?>[] params = method.getParameterTypes();
            if (params.length == 1 && params[0].isAssignableFrom(Path.class)) return method;
            if (params.length == 2 && (params[0].isAssignableFrom(Path.class) || params[0].isAssignableFrom(java.io.InputStream.class) || params[0].isAssignableFrom(java.io.OutputStream.class) || params[0].isAssignableFrom(CompoundTag.class))) return method;
        }
        return null;
    }

    private static Object[] buildNbtIoArguments(Method method, Path path, boolean reading) throws Exception {
        Class<?>[] params = method.getParameterTypes();
        if (params.length == 1) return new Object[]{path};
        Object helper = createHelperArgument(params[1]);
        if (helper == null && !params[1].isPrimitive()) helper = null;
        if (params[0].isAssignableFrom(Path.class)) return new Object[]{path, helper};
        if (params[0].isAssignableFrom(java.io.InputStream.class)) return new Object[]{Files.newInputStream(path), helper};
        if (params[0].isAssignableFrom(java.io.OutputStream.class)) return new Object[]{Files.newOutputStream(path), helper};
        return new Object[]{path, helper};
    }

    private static Object createHelperArgument(Class<?> type) {
        try {
            for (Method method : type.getMethods()) {
                if (!java.lang.reflect.Modifier.isStatic(method.getModifiers()) || !type.isAssignableFrom(method.getReturnType())) continue;
                if (method.getParameterCount() == 0) return method.invoke(null);
            }
            try { return type.getDeclaredConstructor().newInstance(); } catch (ReflectiveOperationException ignored) {}
        } catch (Exception ignored) {}
        return null;
    }

    private static List<String> keysOf(CompoundTag compound) {
        return NbtIoHelper.keysOf(compound);
    }

    private static Path resolveRootPath(MinecraftServer server) {
        return NbtIoHelper.resolveRootPath(server);
    }
}