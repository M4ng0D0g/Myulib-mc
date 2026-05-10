# DataBoundList

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

