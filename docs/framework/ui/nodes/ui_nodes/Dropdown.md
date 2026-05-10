# Dropdown

Path: `src/client/java/com/myudog/myulib/client/api/ui/node/Dropdown.java`

Description

Single-select dropdown with a fixed height and expandable list. Stores options and selection in `DropdownComponent` and registers a `ClickableComponent` for expansion toggling.

Public API

- Constructor:
  - `Dropdown(width: Float = 100f)`
- Fluent methods:
  - `fun addOption(option: String): Dropdown`
  - `fun onSelect(handler: (Int) -> Unit): Dropdown`

Usage example

```java
val dd = Dropdown(120f).addOption("Easy").addOption("Normal").addOption("Hard").onSelect { idx -> println("selected $idx") }
```

