# ThemeLoader

Path: `src/client/java/com/myudog/myulib/client/internal/ui/theme/ThemeLoader.java`

Description

Loads JSON theme files from resources and wraps them into `UiTheme` instances. Validates nine-slice data and registers slot styles via `ThemeManager`.

Public API (internal)

- `fun load(themeId: Identifier): UiTheme?` — loads `myulib/themes/{path}.json` and returns a `UiTheme` or `null` on error.

Key behaviors

- Uses `Gson` to parse `JsonThemeData` and validates nine-slice metadata.
- Registers slot styles to `ThemeManager`.
- Wraps JSON into `UiTheme` providing color and style accessors.

Notes

- ThemeLoader is internal; theme authors should place JSON under `assets/<modid>/myulib/themes/<name>.json`.
- Missing or invalid theme results in `null` return and logs exception stack trace.

