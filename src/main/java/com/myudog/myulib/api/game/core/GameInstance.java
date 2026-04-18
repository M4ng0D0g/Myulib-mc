package com.myudog.myulib.api.game.core;

import com.myudog.myulib.api.debug.DebugFeature;
import com.myudog.myulib.api.debug.DebugLogManager;
import com.myudog.myulib.api.ecs.EcsContainer;
import com.myudog.myulib.api.game.object.IGameObject;
import com.myudog.myulib.api.game.state.GameState;
import com.myudog.myulib.api.game.state.GameStateMachine;
import com.myudog.myulib.api.game.event.GameStateChangeEvent;
import com.myudog.myulib.internal.event.EventDispatcherImpl;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 遊戲房間的執行上下文 (Session Context)。
 * [v0.3.2 改良] 採用 Integer ID 作為唯一互動碼，並與局部 ECS 深度整合。
 */
public class GameInstance<C extends GameConfig, D extends GameData, S extends GameState> {

    private final int instanceId; // 🌟 唯一整數 ID (玩家互動用，如 /game join 1)
    private final ServerLevel level;
    private final GameDefinition<C, D, S> definition;

    private final C config;
    private final D data;
    private final GameStateMachine<S> stateMachine;

    // 專屬事件派發器 (確保房間間的邏輯完全隔離)
    private final EventDispatcherImpl eventBus;
    private final List<GameBehavior<C, D, S>> boundBehaviors = new ArrayList<>();

    private boolean enabled = true;
    private boolean started = false;
    private long tickCount = 0;

    public GameInstance(
            int instanceId,
            ServerLevel level,
            GameDefinition<C, D, S> definition,
            C config,
            D data,
            GameStateMachine<S> stateMachine,
            EventDispatcherImpl eventBus
    ) {
        this.instanceId = instanceId;
        this.level = Objects.requireNonNull(level, "level 不得為空");
        this.definition = Objects.requireNonNull(definition, "definition 不得為空");
        this.config = Objects.requireNonNull(config, "config 不得為空");
        this.data = Objects.requireNonNull(data, "data 不得為空");
        this.stateMachine = Objects.requireNonNull(stateMachine, "stateMachine 不得為空");
        this.eventBus = Objects.requireNonNull(eventBus, "eventBus 不得為空");

        // 初始化狀態機
        S current = this.stateMachine.getCurrent();
        if (current != null) {
            current.onEnter(this);
        }
    }

    // --- 🌟 核心標識 API ---

    public int getInstanceId() { return instanceId; }

    /**
     * 獲取系統註冊用的完整 Identifier (例如 myulib:session_1)
     */
    public Identifier getSessionId() { return data.getId(); }

    /**
     * 🌟 改良：僅需注入系統 ID，不再需要額外的 shortId 字串
     */
    public void setupIdentity(Identifier fullId) {
        this.data.setupId(Objects.requireNonNull(fullId, "fullId 不得為空"));
    }

    // --- 🌟 局部資料存取橋接 (Bridge API) ---

    public D getData() { return data; }

    /**
     * 快速存取該局遊戲的局部 ECS 世界
     */
    public EcsContainer getEcsContainer() { return data.getEcsContainer(); }

    /**
     * 檢查玩家是否參與此局遊戲，並取得其在 ECS 中的實體 ID
     */
    public Optional<Integer> getParticipantEntity(ServerPlayer player) {
        return Optional.ofNullable(data.getParticipantEntity(player.getUUID()));
    }

    public boolean isParticipating(UUID uuid) {
        return data.getParticipantEntity(uuid) != null;
    }

    // --- 狀態管理 ---

    public S getCurrentState() { return stateMachine.getCurrent(); }

    public boolean transition(S to) {
        if (!enabled || !stateMachine.canTransition(to)) return false;

        S from = stateMachine.getCurrent();
        if (stateMachine.transitionTo(to)) {
            if (from != null) from.onExit(this);
            to.onEnter(this);
            eventBus.dispatch(new GameStateChangeEvent<>(this, from, to));
            DebugLogManager.log(DebugFeature.GAME, "instance=" + instanceId + " transition [" + (from == null ? "NONE" : from) + " -> " + to + "]");
            return true;
        }
        return false;
    }

    // --- 生命週期 ---

    public void tick() {
        if (!enabled) return;
        tickCount++;
        S current = stateMachine.getCurrent();
        if (current != null) {
            current.onTick(this, tickCount);
        }
    }

    /**
     * 🌟 改良：徹底清理資源
     */
    public void destroy() {
        if (!enabled) return;
        this.enabled = false;
        this.started = false;

        S current = stateMachine.getCurrent();
        if (current != null) {
            current.onExit(this);
        }

        for (int i = boundBehaviors.size() - 1; i >= 0; i--) {
            boundBehaviors.get(i).onUnbind(this);
        }
        boundBehaviors.clear();

        // 由 GameData 子類自行擴充跨系統清理（例如 field/team）
        this.eventBus.clear();

        // 重置資料載體（清空參與者與 runtime objects）
        this.data.reset(this);

        DebugLogManager.log(DebugFeature.GAME, "destroy instance=" + instanceId);
    }

    public boolean isEnabled() { return enabled; }
    public boolean isStarted() { return started; }
    public long getTickCount() { return tickCount; }

    // --- 配置存取 ---
    public C getConfig() { return config; }
    public GameDefinition<C, D, S> getDefinition() { return definition; }

    public Optional<com.myudog.myulib.api.game.object.IGameObject> getGameObject(Identifier id) {
        return Optional.ofNullable(config.gameObjects().get(id));
    }

    public void initializeRuntimeObjects() {
        Map<Identifier, IGameObject> templates = config.gameObjects();
        for (Map.Entry<Identifier, IGameObject> entry : templates.entrySet()) {
            Identifier objectId = entry.getKey();
            IGameObject template = entry.getValue();

            IGameObject runtime = template.copy();
            if (runtime == null || !runtime.validate()) {
                throw new IllegalStateException("無法建立有效遊戲物件副本: " + objectId);
            }

            data.addRuntimeObject(objectId, runtime);
            runtime.onInitialize(this);
            runtime.spawn(this);
        }
    }

    public void bindBehavior(GameBehavior<C, D, S> behavior) {
        behavior.onBind(this);
        boundBehaviors.add(behavior);
    }

    public boolean start() {
        if (!enabled || started) {
            return false;
        }

        definition.startInstance(this);
        started = true;

        DebugLogManager.log(DebugFeature.GAME, "start instance=" + instanceId + ",state=" + getCurrentState());
        return true;
    }

    public void resetState() {
        if (!enabled) return;

        S previous = stateMachine.getCurrent();
        if (previous != null) {
            previous.onExit(this);
        }

        stateMachine.reset();

        S current = stateMachine.getCurrent();
        if (current != null) {
            current.onEnter(this);
            eventBus.dispatch(new GameStateChangeEvent<>(this, previous, current));
        }
    }

    public ServerLevel getLevel() {
        return level;
    }

    public EventDispatcherImpl getEventBus() {
        return eventBus;
    }
}