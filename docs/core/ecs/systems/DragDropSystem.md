# DragDropSystem

Path: `src/client/java/com/myudog/myulib/client/internal/ui/system/DragDropSystem.java`

Description

Manages currently-dragged item stack in UI (for item slot drag-and-drop interactions). Provides utility to start dragging, clear drag, and inspect current dragged stack.

Public API (observed)

- `fun startDragging(entityId: Int, stack: ItemStack)`
- `fun clear()`
- `fun isHoldingItem(): Boolean` (or similar)
- `val draggingStack: ItemStack`

Key behaviors

- When `InputSystem` detects an ItemSlot click and DragDropSystem isn't holding an item, it starts dragging and clears the slot.
- If already holding, clicking another slot will attempt to combine or swap stacks according to `handleItemInteraction` logic.

Usage example

Called by `InputSystem`; not normally invoked directly by player code, but you can call `DragDropSystem.clear()` to cancel drag from other systems.

