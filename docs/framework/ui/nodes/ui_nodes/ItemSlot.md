# ItemSlot

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

