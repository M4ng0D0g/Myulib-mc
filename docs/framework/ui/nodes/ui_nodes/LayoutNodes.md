# LayoutNodes

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

