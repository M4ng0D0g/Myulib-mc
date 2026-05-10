# Access Systems

This page tracks systems initialized in `MyulibApi.init()` and the current in-game command interface.

## Enabled systems
- [Camera](../camera/index.md)
- [Control](../control/index.md)
- [Field](../field/index.md)
- [Game](../game/index.md)
- [Permission](../permission/index.md)
- [RoleGroup](../rolegroup/index.md)
- [Team](../team/index.md)
- [Timer](../timer/index.md)

## Command naming rule
- Canonical runtime commands use `/myulib:<feature> ...`.
- Legacy alias `/myulib ...` remains for `save` and `status` only.

## Global commands
- `/myulib:save`
- `/myulib:status`

## Specialized feature commands

### `/myulib:field`
- `create <id> <x1> <y1> <z1> <x2> <y2> <z2>`
- `read <id-or-shortId>`
- `update <id-or-shortId>`
- `delete <id-or-shortId>`
- `list`
- `visualize on|off|status` (per-player toggle)
- `visualize radius <8..256>`
- `visualize mode <edges-only|full|labels-only>`
- field id arguments provide full-id and shortId suggestions.

### `/myulib:permission`
- `create <group> <action> <decision>`
- `set global <group> <action> <decision>`
- `set <scope> <scopeId> <group> <action> <decision>`
- `read <group> <action>`
- `player <player-selector>` (flattened effective permission states)
- `delete <group> <action>`
- `list <group>`
- `list <group> <scope> <scopeId> <mode>`
- `action` and `decision` support Brigadier enum suggestions.
- `scope` supports `global`, `dimension`, `field`.
- `mode` supports `scope` (single scope) and `merged` (field->dimension->global compression).
- `scopeId` supports auto-completion for field/dimension full id and shortId.
- `group` supports auto-completion from rolegroup ids and known permission table groups.

### `/myulib:rolegroup`
- `create <id> <priority>`
- `read <id-or-shortId>`
- `update <id-or-shortId> <priority>`
- `delete <id-or-shortId>`
- `assign <id-or-shortId> <player-selector>`
- `revoke <id-or-shortId> <player-selector>`
- `groups-of <player-selector>`
- `list`
- rolegroup id arguments provide full-id and shortId suggestions.
- rolegroup id suggestions also include plain path form (no prefix input).
- `groups-of` always includes `everyone` in output as default fallback group.

### `/myulib:team`
- `create <id> <color>`
- `read <id-or-shortId>`
- `update <id-or-shortId> <color>`
- `delete <id-or-shortId>`
- `list`

### `/myulib:game`
- `create <definitionId>`
- `read <instanceId>`
- `update <instanceId>`
- `delete <instanceId>`
- `list`

### `/myulib:timer`
- `create <id> <ticks>`
- `read <id>`
- `update <id> <ticks>`
- `delete <id>`
- `list`

### `/myulib:camera`
- `status`

### `/myulib:control`
- `status`

### `/myulib:debug`
- `on|off|status`
- `all <on|off>`
- `feature <name> <on|off>`
- `trace on|off|status`
- only players who enable debug receive log messages
- feature filters are per-player
- trace mode emits single-message interaction chain summaries (action -> permission decision -> final allow/deny)

## Notes
- `identity` docs are compatibility-only; `rolegroup` is the canonical grouping API.
- UI routing remains provided by `ConfigurationUiRegistry` and `ConfigurationUiBridge`.
