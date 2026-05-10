# ScrollBox
## Role
This page is the canonical reference for `ScrollBox` in the `ui nodes` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
ScrollBox

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

