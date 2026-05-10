# Button

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

