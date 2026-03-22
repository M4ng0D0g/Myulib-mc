# InputSystem

Path: `src/client/kotlin/com/myudog/myulib/client/internal/ui/system/InputSystem.kt`

Description

Centralized UI input routing: receives mouse movement, clicks, drags, releases and scrolls and forwards them to UI world entities. Performs hit-testing, hover updates, and triggers component-level interactions (Clickable/Slider/Draggable/PanCanvas).

Public API

- `fun onMouseMove(world: EcsWorld, rootId: Int, mouseX: Double, mouseY: Double)`
- `fun onMouseDown(world: EcsWorld, rootId: Int, mx: Double, my: Double, button: Int): Boolean`
- `fun onMouseDragged(world: EcsWorld, mx: Double, my: Double)`
- `fun onMouseReleased(world: EcsWorld)`
- `fun onMouseScrolled(world: EcsWorld, mx: Double, my: Double, amount: Double): Boolean`

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

```kotlin
override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
    return InputSystem.onMouseDown(world, rootNodeId, mouseX, mouseY, button)
}
```

Notes

- InputSystem operates on an `EcsWorld` instance populated with UI components. Ensure the event originates from the correct Screen/world.
- For HUD interactions, route events into `HudManager.world` instead of the screen world.

