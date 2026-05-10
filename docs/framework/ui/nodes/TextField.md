# TextField
## Role
This page is the canonical reference for `TextField` in the `ui nodes` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
TextField

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

