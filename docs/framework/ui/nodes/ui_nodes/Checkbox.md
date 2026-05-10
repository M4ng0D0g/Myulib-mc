# Checkbox

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/Checkbox.java`

Description

A checkbox widget with a label. Stores checked state in `CheckboxComponent` and uses `ClickableComponent` to toggle state. Supports label color and texture keys.

Public API

- Constructor:
  - `Checkbox(label: String, initialChecked: Boolean = false)`
- Properties:
  - `var label: String`
  - `var labelColor: Int`
  - `var boxTextureKey: String?`
  - `var checkTextureKey: String?`
- Methods:
  - `fun onToggle(handler: (Boolean) -> Unit): Checkbox`
  - `fun withLabelColor(color: Int): Checkbox`
  - `fun setChecked(checked: Boolean)`
  - `fun isChecked(): Boolean`

Usage example

```java
val cb = Checkbox("Show hints", false).onToggle { checked -> println("checked=$checked") }
```

