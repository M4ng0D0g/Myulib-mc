# Placeholder
## Role
This page is the canonical reference for `Placeholder` in the `ui nodes` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
Placeholder

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/Placeholder.java`

Description

A debug placeholder widget that draws a colored box and optional border; useful during layout prototyping.

Public API

- Constructor:
  - `Placeholder(width: Float = 20f, height: Float = 20f, isDebug: Boolean = true)`
- Behavior:
  - Chooses a color from a debug palette and renders a color block.

Usage example

```java
val ph = Placeholder(40f, 20f)
```

