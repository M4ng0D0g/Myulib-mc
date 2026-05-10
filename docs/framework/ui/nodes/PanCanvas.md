# PanCanvas
## Role
This page is the canonical reference for `PanCanvas` in the `ui nodes` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
PanCanvas

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/PanCanvas.java`

Description

A canvas specialized for panning and zooming content. Works with `PanCanvasComponent` to store pan/zoom state.

Public API

- Constructor:
  - `PanCanvas()`
- Usage: Add children and the `LayoutSystem`/`InputSystem` will respect `PanCanvasComponent` for zoom/pan operations.

Usage example

```java
val pan = PanCanvas()
// configure panComponent if needed via MyulibApiClient.getComponent<PanCanvasComponent>(pan.entityId)
```

