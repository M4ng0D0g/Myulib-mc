# Label

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/Label.java`

Description

Simple text label widget that supports `net.minecraft.text.Text` for localization. Inherits from `LeafWidget` and defaults to wrap-content sizing.

Public API

- Constructors:
  - `Label(content: Text)`
  - `Label(text: String)` — convenience constructor
- Enums:
  - `Label.Alignment { LEFT, CENTER, RIGHT }`
- Properties:
  - `var content: Text`
  - `var color: Int` — ARGB color
  - `var alignment: Alignment`
  - `var shadow: Boolean`
- Methods (fluent / mutators):
  - `fun withColor(color: Int): Label`
  - `fun withShadow(enabled: Boolean): Label`
  - `fun centered(): Label`
  - `fun rightAligned(): Label`
  - `fun setText(newText: String)`
  - `fun setText(newText: Text)`

Usage example

```java
val lbl = Label("Hello World").centered().withColor(0xFFFFFF.toInt())
lbl.setText("Updated")
```

