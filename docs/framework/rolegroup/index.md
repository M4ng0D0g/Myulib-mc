# RoleGroup System

`RoleGroup` is the canonical player grouping system used by MyuLib.
Data is persisted per-world in `myulib/rolegroups.dat`.

## Relationship to permission
- A player may have direct permission overrides.
- A player may inherit permission grants from one or more role groups.
- Role group order is part of permission resolution.

## Management entry points
- `RoleGroupManager.register/update/delete/get/groups`
- `RoleGroupManager.assign/revoke`
- `RoleGroupManager.getPlayersInGroup/getSortedGroupIdsOf`

## Public API
- `com.myudog.myulib.api.rolegroup.RoleGroupDefinition`
- `com.myudog.myulib.api.rolegroup.RoleGroupManager`
- `com.myudog.myulib.api.rolegroup.RoleGroupStorage`
- `com.myudog.myulib.api.rolegroup.NbtRoleGroupStorage`

## In-game command interface
Command root: `/myulib:rolegroup`

- `create <id> <priority>`
- `read <id-or-shortId>`
- `update <id-or-shortId> <priority>`
- `delete <id-or-shortId>`
- `assign <id-or-shortId> <player-selector>`
- `revoke <id-or-shortId> <player-selector>`
- `groups-of <player-selector>`
- `list`

### Completion and selector behavior
- rolegroup id arguments (`read`, `update`, `delete`, `assign`, `revoke`) provide suggestion completion for:
  - full `Identifier`
  - path-only form (no namespace input)
  - generated shortId
- command input is normalized to `myulib:<path>` internally.
- `<player-selector>` uses `EntityArgument.player()` and supports:
  - `@s`
  - `@p`
  - player name

### `everyone` visibility
- `RoleGroupManager` ensures `myulib:everyone` definition exists at server start.
- `groups-of` output always contains `everyone` as default fallback group even when player has no explicit assignments.

## Notes
- A player may belong to multiple role groups.
- Group data includes id, translation component, priority, metadata, and members.
- `identity/` remains a compatibility alias path only.
