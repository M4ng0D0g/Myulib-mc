# Tooltip
## Role
This page is the canonical reference for `Tooltip` in the `ui nodes` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
Tooltip

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/Tooltip.java`

Description

Tooltip node representation used by UI to render hover tooltips. Typically attached via `TooltipComponent` to other widgets.

Public API

- Constructor:
  - `Tooltip()` (implementation details in component & render system)

Usage example

```java
// Attach TooltipComponent to an entity via MyulibApiClient.addComponent(entityId, TooltipComponent("Info"))
```

