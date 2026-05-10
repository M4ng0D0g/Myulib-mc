# Lifecycle Walkthrough

This page explains lifecycle sequencing only. Full class examples are split into each class doc.

## Stage overview

1. `create`: `GameManager.createInstance(...)`
2. `init`: `GameManager.initInstance(...)`
3. `join`: `GameManager.joinPlayer(...)`
4. `start`: `GameManager.startInstance(...)`
5. `shutdown`: `GameManager.shutdownInstance(...)` -> `GameDefinition.onShutDown(...)`
6. `clean`: internal `GameInstance.clean()`
7. next round: run `init` again before any `join/start`

## Runtime usage sequence

```java
GameManager.register(new ChessDefinition());

GameInstance<?, ?, ?> room = GameManager.createInstance(ChessDefinition.ID, "room_a", chessConfig, level);

GameManager.initInstance(room.getInstanceId());
GameManager.joinPlayer(room.getInstanceId(), playerWhite, null);
GameManager.joinPlayer(room.getInstanceId(), playerBlack, null);
GameManager.startInstance(room.getInstanceId());

GameManager.shutdownInstance(room.getInstanceId()); // mapped to instance.shutdown()

// next round must init again
GameManager.initInstance(room.getInstanceId());
```

## Where to define listeners and timers?

- Event bus listeners: define in `GameDefinition.bindBehavior(...)` and remove in `unbindBehavior(...)`.
- Timer definitions: define static timer blueprints in `GameConfig.timerDefinitions()`.
- Timer runtime start/stop: do it in `bindBehavior(...)` (or in events registered there), because it is round-scoped.

## Full examples by class

- `GameDefinition` full example: [`docs/game/GameDefinition.md`](./GameDefinition.md)
- `GameData` full example: [`docs/game/GameData.md`](./GameData.md)
- `GameConfig` full example: [`docs/game/GameConfig.md`](./GameConfig.md)
- `GameInstance` usage example: [`docs/game/GameInstance.md`](./GameInstance.md)
