# Panel

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

