# InteractiveSlot
## Role
This page is the canonical reference for `InteractiveSlot` in the `ui nodes` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
InteractiveSlot

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/InteractiveSlot.java`

Description

An item-slot variant that exposes higher-level interactive behavior: right-click handlers and double-click support. Extends `ItemSlot`.

Public API

- Constructor:
  - `InteractiveSlot(stack: ItemStack = ItemStack.EMPTY, var onRightClick: ((ItemStack) -> Unit)? = null, var onDoubleClick: ((ItemStack) -> Unit)? = null)`
- Behavior:
  - Registers a `ClickableComponent` (via `MyulibApiClient.getComponent`) and assigns `onClick` to forward button presses to the provided handlers.

Usage example

```java
val islot = InteractiveSlot().apply {
    onRightClick = { stack -> println("right clicked: ${stack.item}") }
    onDoubleClick = { stack -> println("double clicked: ${stack.item}") }
}
```

Notes

- The actual click wiring uses `ClickableComponent.onClick` and checks button codes (`0` for left, `1` for right).
- Double-click detection is implemented with an internal timestamp check (`handleDoubleClick`).

