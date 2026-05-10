# Canvas
## Role
This page is the canonical reference for `Canvas` in the `ui nodes` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
Canvas

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/Canvas.java`

Description

Canvas is a `Box` configured for absolute positioning (anchors/offsets) and is typically used for HUD or overlay elements that need precise placement.

Public API

- Constructor:
  - `Canvas()`
- Behavior:
  - Sets the container's `flexContainer.direction` to `FlexDirection.ABSOLUTE` in init.

Usage example

```java
val hudCanvas = Canvas()
val badge = Label("HP").apply { transform.offsetX = 10f; transform.offsetY = 20f }
hudCanvas.addChild(badge)
```

