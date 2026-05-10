# Button
## Role
This page is the canonical reference for `Button` in the `ui nodes` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
Button

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/Button.java`

Description

A restricted Button widget that extends `Box`. It only allows content widgets such as `Label`, `Image`, or plain `Box` as children (no nested buttons).

Public API

- Constructor:
  - `Button(onClick: () -> Unit = {}) : Box`
- Properties:
  - `var label: String` — a simple label backing field (not used for rendering in this class; kept for API compatibility)
- Overridden methods:
  - `override fun addChild(child: BaseWidget)` — filters children to allowed types and throws on invalid child.

Usage example

```java
val btn = Button({ println("clicked") }).apply {
  // add a label as content
  addChild(Label("Click me"))
}
```

Notes

- `Button` sets a default textureKey of `button_default` on init. Use `withTexture` on the `Box` base class to change it.

