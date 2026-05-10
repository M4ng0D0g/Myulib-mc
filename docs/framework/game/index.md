# Game System

![Game API Class Architecture](./architecture.mmd)

[Open Mermaid source](./architecture.mmd)

`com.myudog.myulib.api.game` is the public entry point for game definition registration and instance lifecycle.

## Core types
- `GameManager`
- `GameDefinition`
- `GameInstance`
- `GameConfig`
- `GameData`
- `GameScopeTokens`
- `GameState`
- `GameStateMachine`
- `GameStateChangeEvent`
- `GameObjectKind`
- `IGameObject`

## Runtime lifecycle
- Contract: `create -> init -> start -> end -> clean`
- Every round must `init` successfully before `join` or `start`
- `end` maps to `shutdown` and keeps instance reusable while resetting runtime resources
- `delete` is final removal (`destroyInstance`)

```java
Identifier gameId = Identifier.fromNamespaceAndPath(Myulib.MOD_ID, "chess");
String roomToken = "room_a";

// create
GameInstance<?, ?, ?> instance = GameManager.createInstance(gameId, roomToken, config, level);

// init (required before join/start)
boolean initialized = GameManager.initInstance(instance.getInstanceId());
if (!initialized) {
	throw new IllegalStateException("init failed");
}

// join and start
boolean joined = GameManager.joinPlayer(instance.getInstanceId(), playerUuid, null);
boolean started = GameManager.startInstance(instance.getInstanceId());

// end current round (instance remains reusable, but must init again next round)
boolean ended = GameManager.shutdownInstance(instance.getInstanceId());

// final deletion when no longer needed
boolean deleted = GameManager.destroyInstance(instance.getInstanceId());
```

## Full lifecycle guide (with subclass implementation)
- Stage-by-stage lifecycle and hook timing: [`docs/game/LifecycleWalkthrough.md`](./LifecycleWalkthrough.md)
- Base class responsibilities (`GameConfig` / `GameData`): [`docs/game/GameConfig.md`](./GameConfig.md), [`docs/game/GameData.md`](./GameData.md)
- Definition and event bus binding points: [`docs/game/GameDefinition.md`](./GameDefinition.md)

## Related systems
- `timer`
- `event`
- `ecs`
- `field`
- `permission`
- `team`
- `rolegroup`
- `camera`
- `control`

## Notes
- Game objects are modeled in `com.myudog.myulib.api.object`.
- `GameManager` owns instance lifecycle orchestration and instance/player indexing.
- Deprecated/removed types are intentionally excluded from this index.
- Spectator is the base fallback team; playable teams must be explicitly defined by each config.

## In-game command interface
Command root: `/myulib:game`

- `create <definitionId>`
- `read <instanceId>`
- `update <instanceId>`
- `delete <instanceId>`
- `list`
- `init <instanceId>`
- `join <player> <instanceId> [teamId]`
- `start <instanceId>`
- `end <instanceId>`

## Architecture
- Runtime contract and exact call order: [`docs/game/architecture.md`](./architecture.md)
- Visual lifecycle diagram: [`docs/game/architecture.mmd`](./architecture.mmd)
