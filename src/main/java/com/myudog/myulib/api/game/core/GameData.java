package com.myudog.myulib.api.game.core;

import com.myudog.myulib.api.ecs.EcsContainer;
import com.myudog.myulib.api.game.object.IGameObject;
import net.minecraft.resources.Identifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class GameData {

    private Identifier id; // 由 GameManager 分配的 session ID

    private final Map<UUID, Integer> participantToEntity = new ConcurrentHashMap<>();
    private final Map<Identifier, IGameObject> runtimeObjects = new ConcurrentHashMap<>();
    private final EcsContainer ecsContainer;

    protected GameData() {
        this.ecsContainer = new EcsContainer();
    }

    public void setupId(Identifier id) { this.id = id; }
    public Identifier getId() { return this.id; }

    // --- 參與者管理 (會話隔離) ---

    public Integer getParticipantEntity(UUID uuid) { return participantToEntity.get(uuid); }
    public void addParticipant(UUID uuid, int entityId) { participantToEntity.put(uuid, entityId); }
    public void removeParticipant(UUID uuid) { participantToEntity.remove(uuid); }

    public void addRuntimeObject(Identifier id, IGameObject obj) {
        this.runtimeObjects.put(id, obj);
    }

    public Optional<IGameObject> getObject(Identifier id) {
        return Optional.ofNullable(runtimeObjects.get(id));
    }

    public Collection<IGameObject> getRuntimeObjects() {
        return Collections.unmodifiableCollection(runtimeObjects.values());
    }

    public void reset(GameInstance<?, ?, ?> instance) {
        participantToEntity.clear();

        runtimeObjects.values().forEach(obj -> obj.destroy(instance));
        runtimeObjects.clear();
    }

    public EcsContainer getEcsContainer() {
        return ecsContainer;
    }
}