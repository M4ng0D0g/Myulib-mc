package com.myudog.myulib.api.game;

import com.myudog.myulib.api.animation.TestSupport;
import com.myudog.myulib.api.game.bootstrap.GameBootstrapConfig;
import com.myudog.myulib.api.game.bootstrap.GameObjectConfig;
import com.myudog.myulib.api.game.feature.GameObjectBindingFeature;
import com.myudog.myulib.api.game.feature.GameTeamFeature;
import com.myudog.myulib.api.game.instance.GameInstance;
import com.myudog.myulib.api.game.object.GameObjectKind;
import com.myudog.myulib.api.game.object.GameObjectHooks;
import com.myudog.myulib.api.game.object.GameObjectRuntime;
import com.myudog.myulib.api.game.object.GameObjectContext;
import com.myudog.myulib.api.game.state.GameDefinition;
import com.myudog.myulib.api.game.team.GameTeamColor;
import com.myudog.myulib.api.game.team.GameTeamDefinition;
import net.minecraft.resources.Identifier;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public final class GameFeatureTestSuite {
    private GameFeatureTestSuite() {
    }

    @Test
    void objectBindingFeatureSupportsRuntimeHooks() {
        GameObjectBindingFeature feature = new GameObjectBindingFeature();
        Identifier objectId = Identifier.fromNamespaceAndPath("myulib", "respawn_anchor");
        GameObjectConfig config = new GameObjectConfig(
            objectId,
            GameObjectKind.RESPAWN_POINT,
            Identifier.fromNamespaceAndPath("minecraft", "block"),
            "Respawn Anchor",
            true,
            Map.of("hook", "spawn")
        );

        AtomicBoolean attached = new AtomicBoolean(false);
        AtomicBoolean interacted = new AtomicBoolean(false);
        GameObjectRuntime runtime = new GameObjectRuntime() {
            @Override
            public void onAttach(GameObjectContext context) {
                attached.set(context.objectId().equals(objectId));
            }

            @Override
            public boolean onInteract(GameObjectContext context) {
                interacted.set(context.interactionKind() == GameObjectKind.USABLE);
                return true;
            }
        };

        feature.register(config, runtime);
        GameObjectHooks.attach(null, objectId, runtime);
        TestSupport.assertTrue(attached.get());
        TestSupport.assertTrue(feature.getDefinition(objectId).isPresent());
        TestSupport.assertTrue(feature.getRuntime(objectId).isPresent());
        TestSupport.assertTrue(feature.interact(null, objectId, Identifier.fromNamespaceAndPath("myulib", "player"), GameObjectKind.USABLE, Map.of("action", "use")));
        TestSupport.assertTrue(interacted.get());
    }

    @Test
    void teamFeatureTracksMembersAndColors() {
        GameTeamFeature feature = new GameTeamFeature();
        Identifier teamId = Identifier.fromNamespaceAndPath("myulib", "red_team");
        Identifier playerId = Identifier.fromNamespaceAndPath("myulib", "player_one");
        GameTeamDefinition team = new GameTeamDefinition(teamId, "Red Team", GameTeamColor.RED, true, true, Map.of("arena", "default"));

        feature.register(team);
        TestSupport.assertTrue(feature.addPlayer(teamId, playerId));
        TestSupport.assertTrue(feature.isOnTeam(playerId, teamId));
        TestSupport.assertEquals(1, feature.playerCount(teamId));
        TestSupport.assertEquals(teamId, feature.teamOf(playerId));
        TestSupport.assertTrue(feature.getDefinition(teamId).isPresent());
        TestSupport.assertTrue(GameTeamColor.RED.isRed());
    }

    @Test
    void gameDefinitionCanDeclareObjectsAndTeamsThroughInstanceCreation() {
        Game.init();
        Identifier gameId = Identifier.fromNamespaceAndPath("myulib", "object_team_test");
        Identifier objectId = Identifier.fromNamespaceAndPath("myulib", "spawn_anchor");
        Identifier teamId = Identifier.fromNamespaceAndPath("myulib", "alpha_team");
        GameDefinition<TestState> definition = new GameDefinition<>(gameId) {
            @Override
            public TestState getInitialState() {
                return TestState.IDLE;
            }

            @Override
            public Map<TestState, Set<TestState>> getAllowedTransitions() {
                Map<TestState, Set<TestState>> map = new EnumMap<>(TestState.class);
                map.put(TestState.IDLE, Set.of(TestState.RUNNING));
                map.put(TestState.RUNNING, Set.of(TestState.FINISHED));
                map.put(TestState.FINISHED, Set.of(TestState.IDLE));
                return map;
            }

            @Override
            public java.util.List<GameObjectConfig> createGameObjects(GameBootstrapConfig config) {
                return java.util.List.of(new GameObjectConfig(objectId, GameObjectKind.RESPAWN_POINT, Identifier.fromNamespaceAndPath("minecraft", "block"), "Spawn Anchor", true));
            }

            @Override
            public java.util.List<GameTeamDefinition> createTeams(GameBootstrapConfig config) {
                return java.util.List.of(new GameTeamDefinition(teamId, "Alpha", GameTeamColor.BLUE, true, true, Map.of()));
            }
        };

        GameManager.register(definition);
        try {
            GameInstance<TestState> instance = GameManager.createInstance(gameId, new GameBootstrapConfig());
            TestSupport.assertTrue(instance.objectBindings().getDefinition(objectId).isPresent());
            TestSupport.assertTrue(instance.teams().getDefinition(teamId).isPresent());
            TestSupport.assertEquals(0, instance.teams().playerCount(teamId));
            TestSupport.assertTrue(instance.hasSpecialObject(objectId));
            GameObjectHooks.tick(instance);
            GameManager.destroyInstance(instance.getInstanceId());
        } finally {
            GameManager.unregister(gameId);
        }
    }

    private enum TestState {
        IDLE,
        RUNNING,
        FINISHED
    }
}



