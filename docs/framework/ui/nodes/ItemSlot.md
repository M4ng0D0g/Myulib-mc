# ItemSlot
## Role
This page is the canonical reference for `ItemSlot` in the `ui nodes` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
ItemSlot

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/ItemSlot.java`

Description

Renders an item slot (18x18) with themeable background and optional placeholder icon. Draws ItemStack and overlay (count/durability) using compatibility helpers.

Public API

- Constructor:
  - `ItemSlot(stack: ItemStack = ItemStack.EMPTY)`
- Properties:
  - `var styleKey: String` — theme key for slot style (default "default")
  - `var placeholderIcon: Identifier?`
  - `var stack: ItemStack` (mutable)
- Fluent methods:
  - `fun withStyle(key: String): ItemSlot`
  - `fun withPlaceholder(icon: Identifier): ItemSlot`

Usage example

```java
val slot = ItemSlot().withStyle("inventory_slot").withPlaceholder(Identifier("mymod","textures/gui/empty_slot.png"))
slot.stack = someItemStack
```

