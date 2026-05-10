# PanCanvas

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

