package com.myudog.myulib.internal.game;

import com.myudog.myulib.api.framework.game.core.GameDefinition;
import com.myudog.myulib.api.framework.game.core.GameInstance;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameRegistry {
    // 遊戲藍圖註冊表 (採用 LinkedHashMap 維持註冊順序)
    private final Map<Identifier, GameDefinition<?, ?, ?>> definitions = new LinkedHashMap<>();

    // 運行中的實例 (執行緒安全)
    private final Map<UUID, GameInstance<?, ?, ?>> instances = new ConcurrentHashMap<>();

    // 實體與實例的反向索引 (UUID -> Instance ID)
    private final Map<UUID, UUID> playerToInstanceMap = new ConcurrentHashMap<>();
    private final Map<UUID, UUID> entityToInstanceMap = new ConcurrentHashMap<>();

    // --- Definition 管理 ---

    public void registerDefinition(@NotNull GameDefinition<?, ?, ?> definition) {
        definitions.put(definition.id(), definition);
    }

    @Nullable
    public GameDefinition<?, ?, ?> getDefinition(@NotNull UUID id) {
        return definitions.get(id);
    }
    // --- Instance 管理 ---

    public void addInstance(@NotNull GameInstance<?, ?, ?> instance) {
        instances.put(instance.getUuid(), instance);
    }

    @Nullable
    public GameInstance<?, ?, ?> removeInstance(@NotNull UUID uuid) {
        return instances.remove(uuid);
    }

    @Nullable
    public GameInstance<?, ?, ?> getInstance(@NotNull UUID uuid) {
        return instances.get(uuid);
    }

    public Collection<GameInstance<?, ?, ?>> getAllInstances() {
        return instances.values();
    }

    // --- Entity 映射管理 ---

    public void linkPlayer(@NotNull UUID uuid, @NotNull UUID instanceUuid) {
        playerToInstanceMap.put(uuid, instanceUuid);
    }


    public void linkEntity(@NotNull UUID entityUuid, @NotNull UUID instanceUuid) {
        entityToInstanceMap.put(entityUuid, instanceUuid);
    }

    public void unlinkPlayer(@NotNull UUID uuid) {
        playerToInstanceMap.remove(uuid);
    }

    public void unlinkEntity(@NotNull UUID entityUuid) {
        entityToInstanceMap.remove(entityUuid);
    }

    @Nullable
    public GameInstance<?, ?, ?> getInstanceByPlayer(UUID uuid) {
        UUID id = playerToInstanceMap.get(uuid);
        return id != null ? instances.get(id) : null;
    }

    @Nullable
    public GameInstance<?, ?, ?> getInstanceByEntity(@NotNull UUID entityUuid) {
        UUID instanceId = entityToInstanceMap.get(entityUuid);
        return instanceId != null ? instances.get(instanceId) : null;
    }

    public void unassignPlayersInInstance(@NotNull UUID instanceUuid) {
        playerToInstanceMap.entrySet().removeIf(entry -> entry.getValue().equals(instanceUuid));
    }

    public void unassignEntitiesInInstance(@NotNull UUID instanceUuid) {
        entityToInstanceMap.entrySet().removeIf(entry -> entry.getValue().equals(instanceUuid));
    }


    public void clearMappingsForInstance(@NotNull UUID instanceUuid) {
        unassignPlayersInInstance(instanceUuid);
        unassignEntitiesInInstance(instanceUuid);
    }

    public Collection<GameInstance<?, ?, ?>> instances() {
        return instances.values();
    }

    public Collection<GameDefinition<?, ?, ?>> definitions() {
        return definitions.values();
    }
}