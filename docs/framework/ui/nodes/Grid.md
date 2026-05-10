# Grid
## Role
This page is the canonical reference for `Grid` in the `ui nodes` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
Grid

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

