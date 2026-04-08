# Object System

`Object` is the standalone system for runtime object definitions, hooks, and interactions.

## Public API
- [Object Core](../api/object/ObjectCore.md)
- `com.myudog.myulib.api.game.object.GameObjectKind`
- `com.myudog.myulib.api.game.object.GameObjectContext`
- `com.myudog.myulib.api.game.object.GameObjectRuntime`
- `com.myudog.myulib.api.game.object.GameObjectDefinition`
- `com.myudog.myulib.api.game.object.GameObjectHooks`

## Notes
- The implementation currently lives under the `game.object` package for compatibility.
- The docs treat object handling as an independent system so it can be separated from `game` over time.
- Use the game docs only for the flow-level integration points.


