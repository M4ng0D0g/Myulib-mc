# EcsCore
## Role
This page is the canonical reference for `EcsCore` in the `ecs` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
ECS API
Public ECS Java types:
- `com.myudog.myulib.api.core.ecs.IComponent`
- `com.myudog.myulib.api.core.ecs.EcsContainer`
- `com.myudog.myulib.internal.core.ecs.ComponentStorage`
- `com.myudog.myulib.api.events.ComponentAddedEvent`
- `com.myudog.myulib.api.core.ecs.lifecycle.Resettable`
- `com.myudog.myulib.api.core.ecs.lifecycle.DimensionAware`
- `com.myudog.myulib.api.core.ecs.lifecycle.DimensionChangePolicy`
- `com.myudog.myulib.api.core.ecs.lifecycle.ComponentLifecycle`
## Quick example
```java
EcsContainer container = new EcsContainer();
int entity = world.createEntity();
world.addComponent(entity, new TransformComponent());
```