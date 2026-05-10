# LayoutNodes
## Role
This page is the canonical reference for `LayoutNodes` in the `ui nodes` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
LayoutNodes

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/LayoutNodes.java`

Description

Collection of helper nodes (Column, Row, etc.) used by the layout system. Check `LayoutSystem` for measurement & distribution algorithm.

Public API

- Includes helper node types such as `Column`, `Row`, etc., that are used to structure layouts.

Usage example

```java
val col = Column()
col.addChild(Label("A"))
col.addChild(Label("B"))
```

