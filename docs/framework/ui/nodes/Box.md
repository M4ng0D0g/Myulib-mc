# Box
## Role
This page is the canonical reference for `Box` in the `ui nodes` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
Box

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/Box.java`

Description

The general-purpose container widget, supports textured backgrounds via `TextureRegistry` and optimized 9-slice rendering.

Public API

- Constructor:
  - `Box()`
- Properties:
  - `var textureKey: String?`
  - `var renderMode: Box.RenderMode` (`PRODUCTION`, `OUTLINE`, `COLOR_BLOCKS`)
- Methods:
  - `fun withTexture(key: String): Box`

Usage example

```java
val panel = Box().withTexture("ui/panel_main")
panel.addChild(Label("Title"))
```

