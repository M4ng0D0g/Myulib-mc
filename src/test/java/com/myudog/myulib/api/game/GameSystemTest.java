package com.myudog.myulib.api.game;
import com.myudog.myulib.api.game.core.*;
import com.myudog.myulib.api.game.object.GameObjectKind;
import com.myudog.myulib.api.game.state.BasicGameStateMachine;
import com.myudog.myulib.api.game.state.GameState;
import com.myudog.myulib.api.game.state.GameStateMachine;
import com.myudog.myulib.internal.event.EventDispatcherImpl;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
final class GameSystemTest {
    private static final AtomicInteger ENTER_CALLS = new AtomicInteger();
    private static final AtomicInteger TICK_CALLS = new AtomicInteger();
    private static final AtomicInteger EXIT_CALLS = new AtomicInteger();

    private enum TestState implements GameState {
        PREPARE,
        ACTIVE,
        FINISHED;

        @Override
        public void onEnter(GameInstance<?, ?, ?> instance) {
            ENTER_CALLS.incrementAndGet();
        }

        @Override
        public void onTick(GameInstance<?, ?, ?> instance, long tickCount) {
            TICK_CALLS.incrementAndGet();
        }

        @Override
        public void onExit(GameInstance<?, ?, ?> instance) {
            EXIT_CALLS.incrementAndGet();
        }
    }
    private static final class TestGameData extends GameData {
    }
    private static final class TestGameDefinition extends GameDefinition<GameConfig, TestGameData, TestState> {
        private final boolean failOnEnd;
        private int onEndCalls;

        private TestGameDefinition(Identifier id) {
            this(id, false);
        }

        private TestGameDefinition(Identifier id, boolean failOnEnd) {
            super(id);
            this.failOnEnd = failOnEnd;
        }
        @Override
        public TestGameData createInitialData(GameConfig config) {
            return new TestGameData();
        }
        @Override
        public GameStateMachine<TestState> createStateMachine(GameConfig config) {
            return new BasicGameStateMachine<>(
                    TestState.PREPARE,
                    Map.of(
                            TestState.PREPARE, Set.of(TestState.ACTIVE),
                            TestState.ACTIVE, Set.of(TestState.FINISHED)
                    )
            );
        }
        @Override
        protected EventDispatcherImpl createEventBus() {
            return new EventDispatcherImpl();
        }
        @Override
        public void bindBehaviors(GameInstance<GameConfig, TestGameData, TestState> instance) {
        }

        @Override
        protected void onEnd(GameInstance<GameConfig, TestGameData, TestState> instance) throws Exception {
            onEndCalls++;
            if (failOnEnd) {
                throw new Exception("forced_end_failure");
            }
        }

        private int onEndCalls() {
            return onEndCalls;
        }

        private void invokeOnEnd(GameInstance<GameConfig, TestGameData, TestState> instance) throws Exception {
            onEnd(instance);
        }
    }

    private static final class StubGameInstance extends GameInstance<GameConfig, TestGameData, TestState> {
        private int instanceId;
        private boolean enabled;
        private final TestGameDefinition definition;

        private StubGameInstance(TestGameDefinition definition) {
            super(
                    0,
                    null,
                    null,
                    GameConfig.empty(),
                    new BasicGameStateMachine<>(TestState.PREPARE, Map.of()),
                    new EventDispatcherImpl()
            );
            this.definition = definition;
        }

        private static StubGameInstance create(int instanceId, TestGameDefinition definition) {
            try {
                Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
                Field theUnsafe = unsafeClass.getDeclaredField("theUnsafe");
                theUnsafe.setAccessible(true);
                Object unsafe = theUnsafe.get(null);
                StubGameInstance instance = (StubGameInstance) unsafeClass.getMethod("allocateInstance", Class.class)
                        .invoke(unsafe, StubGameInstance.class);
                Field definitionField = StubGameInstance.class.getDeclaredField("definition");
                definitionField.setAccessible(true);
                definitionField.set(instance, definition);
                instance.instanceId = instanceId;
                instance.enabled = true;
                return instance;
            } catch (ReflectiveOperationException ex) {
                throw new RuntimeException("Failed to create StubGameInstance", ex);
            }
        }

        @Override
        public int getInstanceId() {
            return instanceId;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        @Override
        public boolean end() {
            if (!enabled) {
                return false;
            }
            try {
                definition.invokeOnEnd(this);
            } catch (Exception e) {
                throw new RuntimeException("強制結束遊戲失敗: " + e.getMessage(), e);
            }
            enabled = false;
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<Integer, GameInstance<?, ?, ?>> instanceMap() {
        try {
            Field field = GameManager.class.getDeclaredField("INSTANCES");
            field.setAccessible(true);
            return (Map<Integer, GameInstance<?, ?, ?>>) field.get(null);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Integer> tokenMap() {
        try {
            Field field = GameManager.class.getDeclaredField("INSTANCE_TOKENS");
            field.setAccessible(true);
            return (Map<String, Integer>) field.get(null);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<UUID, Integer> playerInstanceMap() {
        try {
            Field field = GameManager.class.getDeclaredField("playerToInstanceMap");
            field.setAccessible(true);
            return (Map<UUID, Integer>) field.get(null);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }
    @Test
    void coreGameTypesAndStateMachineBehaveAsExpected() {
        GameConfig config = GameConfig.empty();
        assertDoesNotThrow(config::teams, "Empty game config should expose default teams");
        assertTrue(config.gameObjects().isEmpty(), "Empty game config should not contain game objects");
        assertEquals(16, config.maxPlayer(), "Empty game config should use default maxPlayer");
        assertTrue(config.teams().containsKey(GameConfig.SPECTATOR_TEAM_KEY), "Default config should include spectator alias");
        assertEquals(GameConfig.SPECTATOR_TEAM_ID, config.teams().get(GameConfig.SPECTATOR_TEAM_KEY), "Default spectator id should be stable");
        TestGameData data = new TestGameData();
        assertDoesNotThrow(() -> data.init(config), "GameData should initialize with empty config without throwing");
        assertEquals(List.of(
                GameObjectKind.LOGIC,
                GameObjectKind.MINEABLE,
                GameObjectKind.ATTACKABLE,
                GameObjectKind.INTERACTABLE,
                GameObjectKind.PROXIMITY_TRIGGER,
                GameObjectKind.DECORATIVE,
                GameObjectKind.CUSTOM
        ), List.of(GameObjectKind.values()), "GameObjectKind should expose the current kinds in declaration order");
        BasicGameStateMachine<TestState> machine = new BasicGameStateMachine<>(
                TestState.PREPARE,
                Map.of(
                        TestState.PREPARE, Set.of(TestState.ACTIVE),
                        TestState.ACTIVE, Set.of(TestState.FINISHED)
                )
        );
        assertEquals(TestState.PREPARE, machine.getCurrent(), "State machine should start at the initial state");
        assertTrue(machine.canTransition(TestState.ACTIVE), "State machine should allow the configured transition");
        assertFalse(machine.canTransition(TestState.FINISHED), "State machine should reject an unconfigured transition");
        assertTrue(machine.transitionTo(TestState.ACTIVE), "Configured transition should succeed");
        assertEquals(TestState.ACTIVE, machine.getCurrent(), "Current state should update after a successful transition");
        machine.reset();
        assertEquals(TestState.PREPARE, machine.getCurrent(), "Reset should return the state machine to the initial state");
    }
    @Test
    void gameDefinitionRegistryTracksDefinitions() {

        Identifier gameId = Identifier.fromNamespaceAndPath("tests", "arena");
        TestGameDefinition definition = new TestGameDefinition(gameId);
        try {
            GameManager.register(definition);
            assertSame(definition, GameManager.definition(gameId), "Registered definition should be retrievable");
            assertTrue(GameManager.hasDefinition(gameId), "GameManager should report the registered definition");
        } finally {
            GameManager.unregister(gameId);
        }
    }

    @Test
    void endInstanceRemovesInstanceAndMappingsWhenOnEndSucceeds() {
        String token = "end_success_room";
        int instanceId = 31_001;
        UUID playerId = UUID.randomUUID();
        TestGameDefinition definition = new TestGameDefinition(Identifier.fromNamespaceAndPath("tests", "end_success"));
        StubGameInstance instance = StubGameInstance.create(instanceId, definition);
        Map<Integer, GameInstance<?, ?, ?>> instances = instanceMap();
        Map<String, Integer> tokens = tokenMap();
        Map<UUID, Integer> players = playerInstanceMap();

        try {
            instances.put(instanceId, instance);
            tokens.put(token, instanceId);
            players.put(playerId, instanceId);
            assertTrue(GameManager.instanceOf(playerId).isPresent(), "Player mapping should exist before end");

            assertTrue(GameManager.endInstance(token), "endInstance(token) should succeed for a valid enabled instance");
            assertEquals(1, definition.onEndCalls(), "Definition onEnd should be invoked exactly once");
            assertNull(GameManager.getInstance(instanceId), "Ended instance should be removed from manager");
            assertTrue(GameManager.resolveInstanceId(token).isEmpty(), "Ended token should be detached from manager");
            assertTrue(GameManager.instanceOf(playerId).isEmpty(), "Player-to-instance mapping should be cleaned when ending");
        } finally {
            instances.remove(instanceId);
            tokens.remove(token);
            players.remove(playerId);
        }
    }

    @Test
    void endInstanceKeepsInstanceWhenOnEndThrows() {
        String token = "end_failure_room";
        int instanceId = 31_002;
        TestGameDefinition definition = new TestGameDefinition(Identifier.fromNamespaceAndPath("tests", "end_failure"), true);
        StubGameInstance instance = StubGameInstance.create(instanceId, definition);
        Map<Integer, GameInstance<?, ?, ?>> instances = instanceMap();
        Map<String, Integer> tokens = tokenMap();

        try {
            instances.put(instanceId, instance);
            tokens.put(token, instanceId);

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> GameManager.endInstance(instanceId),
                    "endInstance should propagate onEnd failure as RuntimeException");
            assertTrue(ex.getMessage().contains("強制結束遊戲失敗"), "Exception message should indicate force-end failure");
            assertEquals(1, definition.onEndCalls(), "Definition onEnd should still be attempted once");
            assertNotNull(GameManager.getInstance(instanceId), "Failed end should keep the instance registered");
            assertTrue(GameManager.resolveInstanceId(token).isPresent(), "Failed end should keep instance token mapping");
        } finally {
            instances.remove(instanceId);
            tokens.remove(token);
        }
    }
}
