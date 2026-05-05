# Game object overview
Game-object related types currently live under `com.myudog.myulib.api.object`.
## Current public types
- `GameObjectConfig`
- `GameObjectKind`
- `IGameEntity`
- `IObjectBehavior<T extends BaseGameObject>`
- `IBlockBehavior`
- `IEntityBehavior`
## Notes
- There is no standalone `com.myudog.myulib.api.object` package in the current source tree.
- Use the game docs for object configuration and runtime integration.
- Block objects now support composable behaviors and absolute world-space AABB via `bounding_box`.
- `bounding_box` format: `minX,minY,minZ,maxX,maxY,maxZ`.
