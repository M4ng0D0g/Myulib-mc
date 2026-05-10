# Box

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

