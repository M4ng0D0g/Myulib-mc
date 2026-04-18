# LayoutSystem

Path: `src/client/java/com/myudog/myulib/client/internal/ui/system/LayoutSystem.java`

Description

Responsible for measuring and calculating layout (positions and sizes) for all UI nodes. It produces `ComputedTransform` for each widget which `RenderSystem` uses to draw.

Public API

- `fun update(world: EcsContainer, screenW: Float, screenH: Float)`
  - Starts layout pass: finds root entities and runs `measureRecursive` and `calculateRecursive`.

Key behaviors

- Measures intrinsic sizes of widgets (Labels, Buttons, Image, etc.) and resolves `SizeUnit` (Fixed, Relative, WrapContent, FillContainer).
- For containers with `FlexContainerComponent`, handles different directions: HORIZONTAL, VERTICAL, FLOW, GRID, STACK, ABSOLUTE.
- Supports scroll content measurement (for `ScrollBox`) and updates `ScrollComponent.contentHeight`.
- Distributes space using weights, spacing, margins and alignment (main/cross axis rules).

Usage example

Layout is normally driven by the Screen or higher-level systems; to invoke manually:

```java
// inside a screen render/init hook
LayoutSystem.update(world, width.toFloat(), height.toFloat())
```

Notes

- `LayoutSystem` is internal and meant to be called each frame before rendering.
- If creating custom node types, ensure you provide intrinsic size hints (via widget instance behavior) or adjust `TransformComponent` to correct SizeUnit.

