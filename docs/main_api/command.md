# main.api.command

Primary classes:
- [`AccessCommandService`](command/AccessCommandService.md)
- [`CommandRegistry`](command/CommandRegistry.md)
- [`CommandContext`](command/CommandContext.md)
- [`CommandResult`](command/CommandResult.md)
- [`CommandAction`](command/CommandAction.md)

Responsibilities:
- register /myulib:<feature> command interfaces
- expose local mirrored command execution for tests
- provide permission/rolegroup/team/field/game/timer command entrypoints

## Architecture
- [Architecture diagram (Mermaid)](command/architecture.mmd)
