# DataBoundGrid
## Role
This page is the canonical reference for `DataBoundGrid` in the `ui nodes` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
DataBoundGrid

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/DataBoundGrid.java`

Description

A grid variant that is data-bound: it uses a `ListProvider` to produce children dynamically.

Public API

- Constructor:
  - `DataBoundGrid(provider: ListProvider<T>, spacing: Float = 2f, mapper: (T) -> Box)`
- Behavior:
  - Automatically populates children from the provider and updates on changes if provider is observable.

Usage example

```java
val grid = DataBoundGrid(myProvider) { item -> ItemSlot(item.stack) }
```

