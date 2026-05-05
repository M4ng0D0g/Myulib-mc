# Component / ECS overview
Current component behavior is modeled through ECS.
A thin compatibility layer also exists under `com.myudog.myulib.api.game.components` for internal bridge use.
## Current entry points
- `docs/ecs/index.md`
- `com.myudog.myulib.api.core.ecs.EcsContainer`
- `com.myudog.myulib.api.core.ecs.IComponent`
- `com.myudog.myulib.api.events.ComponentAddedEvent`
- `com.myudog.myulib.api.core.ecs.lifecycle.ComponentLifecycle`
- `com.myudog.myulib.api.core.ecs.lifecycle.Resettable`
- `com.myudog.myulib.api.core.ecs.lifecycle.DimensionAware`
- `com.myudog.myulib.api.core.ecs.lifecycle.DimensionChangePolicy`
## Notes
- Prefer the ECS docs for the actual public model.
- The `game.components` compatibility package is intentionally minimal.
