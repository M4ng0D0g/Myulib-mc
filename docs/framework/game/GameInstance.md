# GameInstance

`GameInstance<C, D, S>` is one room runtime container.
It owns lifecycle state, room event bus, state machine execution, and runtime resource cleanup.

## What it owns
- Identity: `instanceId`, `sessionId`, optional `hostUuid`
- Runtime state: `enabled`, `initialized`, `started`, `tickCount`
- Runtime services: `eventBus`, `stateMachine`
- Runtime data: `config`, `data`

## Lifecycle methods
- `init()`
  - validates config
  - creates `GameData`
  - initializes subsystem definitions (team/field/timer/...)
  - initializes runtime objects
  - calls `GameDefinition.init(instance)` (default -> `bindBehavior(instance)`)
  - sets `initialized=true`
- `start()`
  - requires `initialized=true`
  - calls `GameDefinition.onStart(instance)`
  - sets `started=true`
- `shutdown()`
  - calls `GameDefinition.onShutDown(instance)`
  - then `clean()`
- `clean()`
  - delegates to internal runtime cleanup
  - calls `GameDefinition.clean(instance)` (default -> `unbindBehavior(instance)`)
  - clears event bus, resets data, resets state-machine runtime flags
  - ends at `initialized=false`, `started=false`
- `destroy()`
  - runs clean and finally marks `enabled=false`

## State control
- `transition(to)` enforces state machine transition rules.
- `forceTransition(to)` bypasses transition checks.
- `resetState()` returns to initial state.

## Join behavior
`joinPlayer(...)` is instance-local team assignment logic.
Manager-level gate (`GameManager.joinPlayer`) prevents join when `initialized=false`.

## Full example

```java
GameInstance<?, ?, ?> room = GameManager.createInstance(ChessDefinition.ID, "room_a", chessConfig, level);

GameManager.initInstance(room.getInstanceId());
GameManager.joinPlayer(room.getInstanceId(), playerWhite, null);
GameManager.startInstance(room.getInstanceId());

GameManager.shutdownInstance(room.getInstanceId()); // internally calls room.shutdown()

// next round requires init again
GameManager.initInstance(room.getInstanceId());
```
