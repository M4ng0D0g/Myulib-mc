# Grid

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/Grid.java`

Description

A grid container node supporting columns/rows layout. See `LayoutSystem` for distribution logic.

Public API

- Constructor:
  - `Grid()`
- Behavior:
  - Designed to work with `FlexContainerComponent` set to GRID direction; provides grid layout semantics.

Usage example

```java
val grid = Grid()
grid.addChild(ItemSlot())
grid.addChild(ItemSlot())
```

