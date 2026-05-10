# Panel
## Role
This page is the canonical reference for `Panel` in the `ui nodes` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
Panel

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/Panel.java`

Description

A higher-level container based on `Box` with typical panel defaults (padding, background). Useful as a window or grouped area.

Public API

- Constructor:
  - `Panel()`
- Behavior:
  - Provides default padding and style; can host children like any container.

Usage example

```java
val p = Panel()
p.addChild(Label("Settings"))
```

