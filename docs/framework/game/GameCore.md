# Game Core

This page summarizes current core runtime responsibilities.

## Lifecycle entrypoints
- `GameManager.createInstance(...)`
- `GameManager.initInstance(...)`
- `GameManager.joinPlayer(...)`
- `GameManager.startInstance(...)`
- `GameManager.shutdownInstance(...)`
- `GameManager.destroyInstance(...)`

## Core extension points
- `GameDefinition`: assembly + hooks (`createInitialData`, `createStateMachine`, `bindBehavior`, `unbindBehavior`, `onStart`, `onShutDown`)
- `GameData`: mutable runtime state + participant/ECS index
- `GameConfig`: static setup + validation + subsystem definitions

## Read next
- [`docs/game/LifecycleWalkthrough.md`](./LifecycleWalkthrough.md)
- [`docs/game/GameDefinition.md`](./GameDefinition.md)
- [`docs/game/GameData.md`](./GameData.md)
- [`docs/game/GameConfig.md`](./GameConfig.md)
