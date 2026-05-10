# Field System

`Field` is the independent successor to the legacy `region` system.

## Public API
- `com.myudog.myulib.api.field.FieldDefinition`
- `com.myudog.myulib.api.field.FieldManager`

## Typical usage
- create a `FieldDefinition`
- register with `FieldManager.register(...)`
- query with `FieldManager.get(...)` or `FieldManager.findAt(...)`
- remove with `FieldManager.unregister(...)`
- persist with `FieldManager.save()`

## In-game command interface
Command root: `/myulib:field`

- `create <id> <x1> <y1> <z1> <x2> <y2> <z2>`
- `read <id-or-shortId>`
- `update <id-or-shortId>`
- `delete <id-or-shortId>`
- `list`
- `visualize on|off|status`
- `visualize radius <8..256>`
- `visualize mode <edges-only|full|labels-only>`

## Notes
- `read/update/delete` accepts both full `Identifier` and generated shortId.
- Command-side create uses two points and normalizes min/max to produce a valid cuboid AABB.
- Visualization mode supports per-player render styles:
  - `edges-only`: boundary edges + corners + axis highlights
  - `full`: edges/corners/axes + id label message
  - `labels-only`: min-corner marker + id label message
- Visualization toggle is player-local (each player can enable/disable independently).
