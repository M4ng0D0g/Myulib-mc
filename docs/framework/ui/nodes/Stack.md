# Stack
## Role
This page is the canonical reference for `Stack` in the `ui nodes` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
Stack

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/Stack.java`

Description

A simple stacking container which overlays children on top of each other (Z-order follows child order).

Public API

- Constructor:
  - `Stack()`

Usage example

```java
val s = Stack()
s.addChild(Image(...))
s.addChild(Label("On top"))
```

