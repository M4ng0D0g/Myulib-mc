# RenderSystem
## Role
This page is the canonical reference for `RenderSystem` in the `ui` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
RenderSystem

Path: `src/client/java/com/myudog/myulib/client/internal/ui/system/RenderSystem.java`

Description

Traverses the UI hierarchy and invokes widget `draw` methods. Also renders top-layer elements such as drag-held ItemStack and Tooltip.

Public API

- `fun render(world: EcsContainer, context: DrawContext, mouseX: Int, mouseY: Int, delta: Float)`
  - Drives recursive rendering of all root widgets and the top layer.

Key behaviors

- Calls widget.draw(...) for visible widgets and recurses into child hierarchy.
- Handles top-layer rendering:
  - DragDropSystem held item (drawn following mouse)
  - InputSystem.hoveredTooltipEntity => draws TooltipComponent text via `context.drawTooltip`

Usage example

RenderSystem is normally invoked by a Screen's `render` override:

```java
RenderSystem.render(world, context, mouseX, mouseY, delta)
```

Notes

- Keep `draw` implementations in widgets lightweight; RenderSystem will call them every frame.
- If a widget manages its own child rendering (e.g., a special scissored container), its `draw` may handle children differently and you can skip recursion for that subtree if implemented.

