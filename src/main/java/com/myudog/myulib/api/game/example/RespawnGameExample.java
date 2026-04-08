package com.myudog.myulib.api.game.example;

import com.myudog.myulib.api.game.bootstrap.GameBootstrapConfig;
import com.myudog.myulib.api.game.feature.GameFeature;
import com.myudog.myulib.api.game.feature.GameScoreboardFeature;
import com.myudog.myulib.api.game.feature.GameTimerFeature;
import com.myudog.myulib.api.game.instance.GameInstance;
import com.myudog.myulib.api.game.state.GameDefinition;
import com.myudog.myulib.api.game.state.GameStateContext;
import net.minecraft.resources.Identifier;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RespawnGameExample extends GameDefinition<RespawnGameExample.RespawnGameState> {
    public enum RespawnGameState { WAITING, COUNTDOWN, ACTIVE, FINISHED }

    public RespawnGameExample() {
        super(Identifier.fromNamespaceAndPath("myulib", "respawn_game"));
    }

    @Override
    public RespawnGameState getInitialState() {
        return RespawnGameState.WAITING;
    }

    @Override
    public Map<RespawnGameState, Set<RespawnGameState>> getAllowedTransitions() {
        Map<RespawnGameState, Set<RespawnGameState>> map = new EnumMap<>(RespawnGameState.class);
        map.put(RespawnGameState.WAITING, Set.of(RespawnGameState.COUNTDOWN, RespawnGameState.ACTIVE));
        map.put(RespawnGameState.COUNTDOWN, Set.of(RespawnGameState.ACTIVE, RespawnGameState.FINISHED));
        map.put(RespawnGameState.ACTIVE, Set.of(RespawnGameState.FINISHED));
        map.put(RespawnGameState.FINISHED, Set.of(RespawnGameState.WAITING));
        return map;
    }

    @Override
    public Set<Identifier> getRequiredSpecialObjectIds() {
        return Set.of(Identifier.fromNamespaceAndPath("myulib", "respawn_anchor"));
    }

    @Override
    public List<GameFeature> createFeatures(GameBootstrapConfig config) {
        GameScoreboardFeature scoreboard = new GameScoreboardFeature();
        scoreboard.objectiveId = "respawn";
        scoreboard.displayName = "Respawn";
        GameTimerFeature timers = new GameTimerFeature();
        return List.of(scoreboard, timers);
    }

    @Override
    public void onCreate(GameInstance<RespawnGameState> instance) {
        instance.scoreboard().setLine(0, "Respawn game ready");
        instance.scoreboard().setValue("players", 0);
    }

    @Override
    public void onEnterState(GameInstance<RespawnGameState> instance, GameStateContext<RespawnGameState> context) {
        instance.scoreboard().setLine(1, "State: " + context.to());
    }

    @Override
    public void onTick(GameInstance<RespawnGameState> instance) {
        instance.scoreboard().setValue("ticks", (int) instance.getTickCount());
    }

    @Override
    public void onDestroy(GameInstance<RespawnGameState> instance) {
        instance.scoreboard().clear();
        instance.timers().clear();
    }
}


