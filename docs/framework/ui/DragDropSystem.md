# DragDropSystem
## Role
This page is the canonical reference for `DragDropSystem` in the `ui` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
DragDropSystem

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

