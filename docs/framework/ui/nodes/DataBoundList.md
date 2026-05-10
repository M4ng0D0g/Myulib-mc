# DataBoundList
## Role
This page is the canonical reference for `DataBoundList` in the `ui nodes` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
DataBoundList

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/DataBoundList.java`

Description

A vertically scrolling, data-bound list that automatically refreshes based on `ListProvider` / `ObservableListProvider`.

Public API

- Constructor:
  - `DataBoundList(provider: ListProvider<T>, spacing: Float = 2f, mapper: (T) -> Box)`
- Behavior:
  - Adds children to an internal `Column` container and refreshes when provider notifies changes.

Usage example

```java
val list = DataBoundList(myProvider) { item -> Label(item.name) }
```

