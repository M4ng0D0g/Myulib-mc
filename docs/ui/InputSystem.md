# InputSystem
## Role
This page is the canonical reference for `InputSystem` in the `ui` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
InputSystem

Path: `src/client/java/com/myudog/myulib/client/internal/ui/system/InputSystem.java`

Description

Centralized UI input routing: receives mouse movement, clicks, drags, releases and scrolls and forwards them to UI world entities. Performs hit-testing, hover updates, and triggers component-level interactions (Clickable/Slider/Draggable/PanCanvas).

Public API

- `fun onMouseMove(world: EcsContainer, rootId: Int, mouseX: Double, mouseY: Double)`
- `fun onMouseDown(world: EcsContainer, rootId: Int, mx: Double, my: Double, button: Int): Boolean`
- `fun onMouseDragged(world: EcsContainer, mx: Double, my: Double)`
- `fun onMouseReleased(world: EcsContainer)`
- `fun onMouseScrolled(world: EcsContainer, mx: Double, my: Double, amount: Double): Boolean`

Key behaviors

- Hover handling: `updateHoverRecursive` sets `WidgetStateComponent.isHovered` and manages `hoveredTooltipEntity`.
- Hit-testing: `hitTestRecursive` checks children in reverse order (topmost first) and invokes `ClickableComponent.onClick` for hits; handles special Dropdown expansions.
- Interaction handling:
  - Slider: sets `isDragging` and updates values when dragged
  - Draggable: sets `isDragging` and updates offsets during `onMouseDragged`
  - PanCanvas: handles zoom & pan on scroll/drag when applicable
  - ScrollComponent: scroll wheel handling
- Drag/drop: if `ItemSlotComponent` exists on a hit, `handleItemInteraction` manages DragDropSystem start/transfer/clear

Return values

- `onMouseDown` returns `true` if a UI element was hit and consumed the event.
- `onMouseScrolled` returns `true` if scroll was handled (e.g., in pan canvas or scrollbox)

Usage example

Call from mapping layer (Screen override or adapter):

```java
override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
    return InputSystem.onMouseDown(world, rootNodeId, mouseX, mouseY, button)
}
```

Notes

- InputSystem operates on an `EcsContainer` instance populated with UI components. Ensure the event originates from the correct Screen/world.
- For HUD interactions, route events into `HudManager.world` instead of the screen world.

