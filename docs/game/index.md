# Game System

![Game API Class Architecture](../../src/main/java/com/myudog/myulib/api/game/architecture.mmd)

[Open Mermaid source](../../src/main/java/com/myudog/myulib/api/game/architecture.mmd)

`com.myudog.myulib.api.game` is the public entry point for game definition registration and instance lifecycle.

## Core types
- `GameManager`
- `GameDefinition`
- `GameInstance`
- `GameConfig`
- `GameData`
- `GameState`
- `GameStateMachine`
- `GameStateChangeEvent`
- `GameObjectConfig`
- `GameObjectKind`
- `IGameEntity`

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
- Game objects are modeled in `com.myudog.myulib.api.game.object`.
- `GameManager` owns instance creation/destruction and id assignment strategy.

## In-game command interface
Command root: `/myulib:game`

- `create <definitionId>`
- `read <instanceId>`
- `update <instanceId>`
- `delete <instanceId>`
- `list`
