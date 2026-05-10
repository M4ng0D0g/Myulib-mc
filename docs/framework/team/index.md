# Team System

`Team` is an independent system.
Team IDs are `Identifier` and can be game-scoped (for example `myulib:<gameId>_<teamId>`).

## Public API
- `com.myudog.myulib.api.team.TeamDefinition`
- `com.myudog.myulib.api.team.TeamManager`
- `com.myudog.myulib.api.team.TeamAdminService`

## Notes
- Use `TeamManager.register(gameId, team)` for game-scoped teams.
- Use `TeamManager.all(gameId)` and `TeamManager.snapshot(gameId)` for inspection.
- Use `TeamManager.forEachMember(teamId, action)` for batch operations.
- Use `TeamManager.unregisterGame(gameId)` or `TeamAdminService.deleteGameTeams(gameId)` for cleanup.
- `GameDefinition` no longer owns team construction.

## In-game command interface
Command root: `/myulib:team`

- `create <id> <color>`
- `read <id-or-shortId>`
- `update <id-or-shortId> <color>`
- `delete <id-or-shortId>`
- `list`
