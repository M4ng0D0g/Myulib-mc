# ScrollBox

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/ScrollBox.java`

Description

A scrollable container that uses scissoring to clip child rendering and manages a `ScrollComponent` for scroll state. Extends `Box`.

Public API

- Constructor:
  - `ScrollBox()`
- Properties:
  - `val scrollData: ScrollComponent`
- Behavior:
  - Adds `ScrollComponent` to the entity on init.
  - Renders children within a scissored region and optionally draws a scrollbar when content overflows.

Usage example

```java
val listBox = ScrollBox().apply {
    // add children (rows) to this container
}
```

