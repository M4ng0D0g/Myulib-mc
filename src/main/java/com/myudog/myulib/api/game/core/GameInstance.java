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

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 遊戲房間的執行上下文 (Session Context)。
 * [v0.3.2 改良] 採用 Integer ID 作為唯一互動碼，並與局部 ECS 深度整合。
 */
public class GameInstance<C extends GameConfig, D extends GameData, S extends GameState> {

    private final int instanceId; // 🌟 唯一整數 ID (玩家互動用，如 /game join 1)
    private final ServerLevel level;
    private final GameDefinition<C, D, S> definition;

    private Identifier sessionId;
    private C config;
    private D data;
    private final GameStateMachine<S> stateMachine;
    private UUID hostUuid;

    // 專屬事件派發器 (確保房間間的邏輯完全隔離)
    private final EventDispatcherImpl eventBus;
    private final List<GameBehavior<C, D, S>> boundBehaviors = new ArrayList<>();

    private boolean enabled = true;
    private boolean initialized = false;
    private boolean started = false;
    private long tickCount = 0;

    public GameInstance(
            int instanceId,
            ServerLevel level,
            GameDefinition<C, D, S> definition,
            C config,
            GameStateMachine<S> stateMachine,
            EventDispatcherImpl eventBus
    ) {
        this.instanceId = instanceId;
        this.level = Objects.requireNonNull(level, "level 不得為空");
        this.definition = Objects.requireNonNull(definition, "definition 不得為空");
        this.config = Objects.requireNonNull(config, "config 不得為空");
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
    public Identifier getSessionId() { return sessionId; }

    /**
     * 🌟 改良：僅需注入系統 ID，不再需要額外的 shortId 字串
     */
    public void setupIdentity(Identifier fullId) {
        this.sessionId = Objects.requireNonNull(fullId, "fullId 不得為空");
        if (this.data != null) {
            this.data.setupId(fullId);
        }
    }

    // --- 🌟 局部資料存取橋接 (Bridge API) ---

    public D getData() {
        if (data == null) {
            throw new IllegalStateException("GameData 尚未建立，請先 start() 成功後再取得");
        }
        return data;
    }

    /**
     * 快速存取該局遊戲的局部 ECS 世界
     */
    public EcsContainer getEcsContainer() { return getData().getEcsContainer(); }

    /**
     * 檢查玩家是否參與此局遊戲，並取得其在 ECS 中的實體 ID
     */
    public Optional<Integer> getParticipantEntity(ServerPlayer player) {
        if (data == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(data.getParticipantEntity(player.getUUID()));
    }

    public boolean isParticipating(UUID uuid) {
        if (data == null) {
            return false;
        }
        return data.getParticipantEntity(uuid) != null;
    }

    public Optional<UUID> getHostUuid() {
        return Optional.ofNullable(hostUuid);
    }

    public void setHostUuid(UUID hostUuid) {
        this.hostUuid = hostUuid;
    }

    public void clearHostUuid() {
        this.hostUuid = null;
    }

    public Map<Identifier, Set<UUID>> getTeamMembersSnapshot() {
        if (data == null) {
            return Collections.emptyMap();
        }
        return data.teamMembersSnapshot();
    }

    public Set<UUID> getTeamMembers(Identifier teamId) {
        if (teamId == null || data == null) {
            return Set.of();
        }
        return data.membersOf(teamId);
    }

    public Optional<Identifier> getPlayerTeam(UUID playerUuid) {
        if (playerUuid == null || data == null) {
            return Optional.empty();
        }
        return data.teamOf(playerUuid);
    }

    public boolean joinPlayer(UUID playerUuid) {
        return joinPlayer(playerUuid, null);
    }

    public boolean joinPlayer(UUID playerUuid, Identifier requestedTeamId) {
        if (playerUuid == null || data == null) {
            return false;
        }

        if (started && !config.allowSpectating() && !data.containsPlayer(playerUuid)) {
            throw new IllegalStateException("遊戲已開始，不允許觀戰加入");
        }

        Identifier decidedTeam = definition.resolveTeamForJoin(this, playerUuid, requestedTeamId);
        Identifier targetTeam = resolveJoinTeam(decidedTeam, requestedTeamId);

        if (!data.containsPlayer(playerUuid) && !GameConfig.SPECTATOR_TEAM_ID.equals(targetTeam) && data.countActivePlayers() >= config.maxPlayer()) {
            throw new IllegalStateException("房間已滿: maxPlayer=" + config.maxPlayer());
        }

        return data.movePlayerToTeam(playerUuid, targetTeam, config);
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
        this.initialized = false;
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
        if (this.data != null) {
            this.data.reset(this);
            this.data = null;
        }
        this.hostUuid = null;

        DebugLogManager.log(DebugFeature.GAME, "destroy instance=" + instanceId);
    }

    public boolean isEnabled() { return enabled; }
    public boolean isInitialized() { return initialized; }
    public boolean isStarted() { return started; }
    public long getTickCount() { return tickCount; }

    // --- 配置存取 ---
    public C getConfig() { return config; }

    public void setConfig(C config) {
        if (initialized) {
            throw new IllegalStateException("遊戲已初始化，無法再次設定 config");
        }
        this.config = Objects.requireNonNull(config, "config 不得為空");
    }
    public GameDefinition<C, D, S> getDefinition() { return definition; }

    public Optional<com.myudog.myulib.api.game.object.IGameObject> getGameObject(Identifier id) {
        if (config == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(config.gameObjects().get(id));
    }

    public void initializeRuntimeObjects() {
        if (config == null) {
            throw new IllegalStateException("尚未設定 config，無法初始化 runtime objects");
        }
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

    public boolean init() {
        if (!enabled || initialized) {
            return false;
        }

        if (config == null) {
            throw new IllegalStateException("尚未設定 config");
        }
        if (!config.validate()) {
            throw new IllegalArgumentException("config.validate() 回傳 false");
        }

        D createdData = Objects.requireNonNull(definition.createInitialData(config), "createInitialData() 不得回傳 null");
        if (sessionId != null) {
            createdData.setupId(sessionId);
        }

        try {
            this.data = createdData;
            createdData.init(config);
            initializeRuntimeObjects();

            for (GameBehavior<C, D, S> behavior : definition.gameBehaviors()) {
                bindBehavior(behavior);
            }
            definition.bindBehaviors(this);

            initialized = true;
            DebugLogManager.log(DebugFeature.GAME, "init instance=" + instanceId + ",state=" + getCurrentState());
            return true;
        } catch (Exception e) {
            initialized = false;
            started = false;
            for (int i = boundBehaviors.size() - 1; i >= 0; i--) {
                boundBehaviors.get(i).onUnbind(this);
            }
            boundBehaviors.clear();
            if (this.data != null) {
                this.data.reset(this);
                this.data = null;
            }
            throw new RuntimeException("初始化遊戲實例失敗: " + e.getMessage(), e);
        }
    }

    public boolean start() {
        if (!enabled || started) {
            return false;
        }

        if (!initialized && !init()) {
            return false;
        }

        try {
            definition.onStart(this);
            started = true;

            DebugLogManager.log(DebugFeature.GAME, "start instance=" + instanceId + ",state=" + getCurrentState());
            return true;
        } catch (Exception e) {
            started = false;
            throw new RuntimeException("啟動遊戲實例失敗: " + e.getMessage(), e);
        }
    }

    public boolean end() {
        if (!enabled) {
            return false;
        }

        try {
            definition.onEnd(this);
        } catch (Exception e) {
            throw new RuntimeException("強制結束遊戲失敗: " + e.getMessage(), e);
        }

        destroy();
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

    private Identifier resolveJoinTeam(Identifier decidedTeam, Identifier requestedTeamId) {
        if (started) {
            return GameConfig.SPECTATOR_TEAM_ID;
        }
        if (decidedTeam != null && data.isTeamRegistered(decidedTeam)) {
            return decidedTeam;
        }
        if (requestedTeamId != null && data.isTeamRegistered(requestedTeamId)) {
            return requestedTeamId;
        }
        return GameConfig.SPECTATOR_TEAM_ID;
    }
}