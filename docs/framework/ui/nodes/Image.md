# Image
## Role
This page is the canonical reference for `Image` in the `ui nodes` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
Image

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/Image.java`

Description

A simple image widget that draws a texture region. Supports debug mode which draws a placeholder color if the texture is missing.

Public API

- Constructor with properties:
  - `Image(texture: Identifier? = null, u: Int = 0, v: Int = 0, regionW: Int = 16, regionH: Int = 16, textureW: Int = 256, textureH: Int = 256)`
- Properties:
  - `var texture: Identifier?`
  - `var debugMode: Boolean`
- Methods (fluent):
  - `fun withUV(u: Int, v: Int, w: Int, h: Int): Image`
  - `fun withDebug(enabled: Boolean): Image`

Usage example

```java
val img = Image(Identifier("mymod", "textures/gui/icon.png")).withUV(0,0,16,16)
```

