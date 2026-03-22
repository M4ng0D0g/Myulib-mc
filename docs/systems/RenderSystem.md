# RenderSystem

Path: `src/client/kotlin/com/myudog/myulib/client/internal/ui/system/RenderSystem.kt`

Description

Traverses the UI hierarchy and invokes widget `draw` methods. Also renders top-layer elements such as drag-held ItemStack and Tooltip.

Public API

- `fun render(world: EcsWorld, context: DrawContext, mouseX: Int, mouseY: Int, delta: Float)`
  - Drives recursive rendering of all root widgets and the top layer.

Key behaviors

- Calls widget.draw(...) for visible widgets and recurses into child hierarchy.
- Handles top-layer rendering:
  - DragDropSystem held item (drawn following mouse)
  - InputSystem.hoveredTooltipEntity => draws TooltipComponent text via `context.drawTooltip`

Usage example

RenderSystem is normally invoked by a Screen's `render` override:

```kotlin
RenderSystem.render(world, context, mouseX, mouseY, delta)
```

Notes

- Keep `draw` implementations in widgets lightweight; RenderSystem will call them every frame.
- If a widget manages its own child rendering (e.g., a special scissored container), its `draw` may handle children differently and you can skip recursion for that subtree if implemented.

