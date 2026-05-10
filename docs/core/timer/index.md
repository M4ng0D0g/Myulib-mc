# Timer System

The timer subsystem is provided by standalone runtime classes.

## Current entry points
- `TimerManager`
- `TimerDefinition`
- `TimerInstance`
- `TimerSnapshot`
- `TimerBinding`
- `TimerPayload`
- `TimerEvents`
- `TimerMode`
- `TimerStatus`

## Notes
- Timer definitions are registered by `Identifier`.
- Instances are created with `TimerManager.createInstance(...)`.
- Runtime updates are driven by `TimerManager.update(...)` via server tick.

## In-game command interface
Command root: `/myulib:timer`

- `create <id> <ticks>`
- `read <id>`
- `update <id> <ticks>`
- `delete <id>`
- `list`
