# Event API
Public event-layer Java types:
- `com.myudog.myulib.api.core.event.IEvent`
- `com.myudog.myulib.api.core.event.IFailableEvent`
- `com.myudog.myulib.api.core.event.EventPriority`
- `com.myudog.myulib.api.core.event.ProcessResult`
- `com.myudog.myulib.api.core.event.IEventListener`
- `com.myudog.myulib.api.core.event.EventBus`
- `com.myudog.myulib.api.core.event.ServerEventBus`
- `com.myudog.myulib.api.events.EntitySpawnEvent`
- `com.myudog.myulib.api.events.ServerTickEvent`
- `com.myudog.myulib.api.events.ComponentAddedEvent`
## Quick example
```java
EventDispatcherImpl dispatcher = new EventDispatcherImpl();
dispatcher.subscribe(ServerTickEvent.class, event -> ProcessResult.PASS);
```