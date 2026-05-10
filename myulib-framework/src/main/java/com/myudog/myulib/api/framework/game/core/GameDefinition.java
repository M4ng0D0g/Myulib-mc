package com.myudog.myulib.api.framework.game.core;

import com.myudog.myulib.api.core.BehaviorChain;
import com.myudog.myulib.api.core.event.EventBus;
import com.myudog.myulib.api.core.state.IState;
import com.myudog.myulib.api.core.state.StateMachine;
import com.myudog.myulib.api.framework.team.TeamColor;
import com.myudog.myulib.api.framework.team.TeamDefinition;
import com.myudog.myulib.api.framework.team.TeamManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

public abstract class GameDefinition<C extends GameConfig, D extends GameData, S extends IState<IGameContext>> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());
    private final Identifier defId;

    protected GameDefinition(@NotNull Identifier defId) {
        this.defId = defId;
    }

    public final Identifier id() {
        return defId;
    }


    // --- 抽象工廠方法 (供開發者實作) ---

    /**
     * 定義如何建立這場遊戲的初始動態資料
     */
    public abstract D createInitialData(C config);

    /**
     * 定義這場遊戲的狀態機與狀態流轉規則
     */
    public abstract StateMachine<S, IGameContext> createStateMachine(C config);

    /**
     * 建立房間專屬的事件匯流排。
     */
    protected abstract EventBus createEventBus();

    // ----------------------------------------------------------------------------------------------------

    protected void bindBehavior(@NotNull GameInstance<C, D, S> instance) throws Exception {
        TeamDefinition spectatorTeam = new TeamDefinition(
                instance.getConfig().SPECTATOR_TEAM,
                Component.translatable("team_spectator"),
                TeamColor.DARK_GRAY,
                Map.of(),
                0
        );
        TeamManager.INSTANCE.register(spectatorTeam);
    }

    protected void unbindBehavior(@NotNull GameInstance<C, D, S> instance) throws Exception {
        var config = instance.getConfig();
        TeamManager.INSTANCE.unregister(config.SPECTATOR_TEAM);
    }

    // ----------------------------------------------------------------------------------------------------

    /**
     * 由外部呼叫，並非由狀態機操控事件
     */
    public abstract void onStart(@NotNull GameInstance<C, D, S> instance);

    /**
     * 由外部呼叫，並非由狀態機操控事件
     */
    public abstract void onShutdown(@NotNull GameInstance<C, D, S> instance);

    // ----------------------------------------------------------------------------------------------------
    // --- 主建構流程 (不可被覆寫) ---

    public final GameInstance<C, D, S> createInstance(@NotNull C config, ServerLevel level) {
        try {
            StateMachine<S, IGameContext> stateMachine = Objects.requireNonNull(createStateMachine(config), "createStateMachine() 不得回傳 null");

            // 🌟 這裡呼叫 createEventBus() 時，回傳型別與變數宣告現在 100% 吻合了！
            EventBus eventBus = Objects.requireNonNull(createEventBus(), "createEventBus() 不得回傳 null");

            // 將 eventBus 注入 GameInstance 建構子
            return new GameInstance<>(level, this, config, stateMachine, eventBus);
        }
        catch (Exception e) {
            throw new RuntimeException("創建遊戲實例失敗: " + e.getMessage(), e);
        }
    }

    public final void initInstance(GameInstance<C, D, S> instance) {
        Objects.requireNonNull(instance, "instance 不得為空");
        instance.initialize();
    }


}