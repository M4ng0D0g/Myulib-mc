# ECS API
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