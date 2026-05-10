# DraggableBox

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/DraggableBox.java`

Description

A box that supports dragging via `DraggableComponent` and `ClickableComponent` hooks. Useful for movable panels.

Public API

- Constructor:
  - `DraggableBox()`
- Behavior:
  - Adds necessary components or relies on user to configure `DraggableComponent` on the entity.

Usage example

```java
val db = DraggableBox()
// configure draggable component via MyulibApiClient if needed
```

