# TextField

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/TextField.java`

Description

Editable single-line text field with focus management, cursor, and placeholder support. It stores state in `TextFieldComponent` and registers a `ClickableComponent` to focus on click.

Public API

- Constructor:
  - `TextField(width: Float = 120f)`
- Fluent / configuration methods:
  - `fun withPlaceholder(text: String): TextField`
  - `fun withFilter(filter: (Char) -> Boolean): TextField`
  - `fun onTextChanged(handler: (String) -> Unit): TextField`
- Notes:
  - The actual text content, cursor position, and change callbacks live in `TextFieldComponent` added to the entity.

Usage example

```java
val tf = TextField(200f).withPlaceholder("Enter name").onTextChanged { text -> println(text) }
```

