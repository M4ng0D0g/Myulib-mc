# DraggableBox
## Role
This page is the canonical reference for `DraggableBox` in the `ui nodes` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
DraggableBox

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

