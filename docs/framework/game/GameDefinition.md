# GameDefinition

`GameDefinition<C, D, S>` is the assembly and lifecycle extension point for each game mode.

## Methods you must implement

- `createInitialData(C config)`
  - Called by `GameInstance.init()`.
  - Return your `GameData` subclass for this room.
- `createStateMachine(C config)`
  - Define legal state transitions.
- `createEventBus()`
  - Return the room-local event bus implementation.

## Lifecycle hooks (new design)

- `init(instance)`
  - public lifecycle interface (called by runtime)
  - Called during instance init after data/runtime objects are ready.
  - Built-in flow calls `bindBehavior(instance)`.
- `clean(instance)`
  - public lifecycle interface (called by runtime)
  - Called during instance clean/shutdown/destroy.
  - Built-in flow calls `unbindBehavior(instance)`.
- `bindBehavior(instance)`
  - required subclass implementation
  - Bind game event listeners / timers / logic wiring for the current round.
- `unbindBehavior(instance)`
  - required subclass implementation
  - Unsubscribe listeners and release round-scoped bindings.

## Start and shutdown hooks

- `onStart(instance)`
  - Optional start callback.
  - You can also trigger start by events registered in `bindBehavior(...)`.
- `onShutDown(instance)`
  - Forced stop callback (shutdown semantics).
  - Called by `GameInstance.shutdown()` before clean.
  - You can bypass transition checks by using `instance.forceTransition(...)` or `instance.resetState()`.

## Full example

```java
public final class ChessDefinition extends GameDefinition<ChessConfig, ChessData, ChessState> {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(Myulib.MOD_ID, "chess/core");
    private EventListener<GameObjectInteractEvent> startListener;

    public ChessDefinition() {
        super(ID);
    }

    @Override
    public ChessData createInitialData(ChessConfig config) {
        return new ChessData();
    }

    @Override
    public GameStateMachine<ChessState> createStateMachine(ChessConfig config) {
        return new BasicGameStateMachine<>(
                ChessState.WAITING,
                Map.of(
                        ChessState.WAITING, Set.of(ChessState.RUNNING),
                        ChessState.RUNNING, Set.of(ChessState.FINISHED)
                )
        );
    }

    @Override
    protected EventDispatcherImpl createEventBus() {
        return new EventDispatcherImpl();
    }

    @Override
    protected void bindBehavior(GameInstance<ChessConfig, ChessData, ChessState> instance) {
        // Example: bind one eventBus listener and drive state machine.
        startListener = event -> {
            if (instance.getCurrentState() == ChessState.WAITING) {
                instance.transition(ChessState.RUNNING);
                return ProcessResult.CONSUME;
            }
            return ProcessResult.PASS;
        };
        instance.getEventBus().subscribe(GameObjectInteractEvent.class, startListener);
    }

    @Override
    protected void unbindBehavior(GameInstance<ChessConfig, ChessData, ChessState> instance) {
        if (startListener != null) {
            instance.getEventBus().unsubscribe(GameObjectInteractEvent.class, startListener);
            startListener = null;
        }
    }

    @Override
    protected void onShutDown(GameInstance<ChessConfig, ChessData, ChessState> instance) {
        // Skip transition guard and force state update for emergency shutdown.
        instance.forceTransition(ChessState.FINISHED);
    }
}
```

## Migration note

- Removed: `gameBehaviors()` and `GameBehavior` interface.
- Replaced by: `init/clean` + `bindBehavior/unbindBehavior` in `GameDefinition`.
