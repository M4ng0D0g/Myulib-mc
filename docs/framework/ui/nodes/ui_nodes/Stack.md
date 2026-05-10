# Stack

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

