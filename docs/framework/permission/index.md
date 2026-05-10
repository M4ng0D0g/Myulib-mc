# Permission System

`Permission` evaluates access with three decision states:

- `ALLOW`
- `UNSET`
- `DENY`

## Resolution order
1. Field scope
2. Dimension scope
3. Global scope

Inside each scope, resolution order is:
1. Player-level overrides
2. RoleGroup grants / overrides
3. `everyone`

`UNSET` means the current scope has no rule and evaluation should continue.
If all scopes are `UNSET`, current default is `ALLOW`.

## Runtime flow
- Rules are stored in `PermissionScope` and `PermissionTable`.
- `PermissionManager.evaluate(...)` resolves the final decision.
- Runtime interceptors use `PermissionGate` to map player + action + target position into evaluate calls.

## Action set (latest `PermissionAction`)
- `BLOCK_PLACE`
- `BLOCK_BREAK`
- `INTERACT_BLOCK`
- `USE_BUCKET`
- `IGNITE_BLOCK`
- `TRAMPLE_FARMLAND`
- `ATTACK_PLAYER`
- `ATTACK_FRIENDLY_MOB`
- `ATTACK_HOSTILE_MOB`
- `INTERACT_ENTITY`
- `RIDE_ENTITY`
- `USE_SPAWN_EGG`
- `ARMOR_STAND_MANIPULATE`
- `USE_PROJECTILE`
- `USE_ITEM`
- `DROP_ITEM`
- `PICKUP_ITEM`
- `OPEN_CONTAINER`
- `TRIGGER_REDSTONE`
- `USE_PORTAL`
- `SEND_MESSAGE`

## Public API
- `PermissionDecision`
- `PermissionAction`
- `PermissionScope`
- `PermissionTable`
- `PermissionManager`
- `PermissionGate`
- `PermissionStorage`
- `NbtPermissionStorage`

## In-game command interface
Command root: `/myulib:permission`

- `create <group> <action> <decision>` (global scope)
- `set global <group> <action> <decision>`
- `set <scope> <scopeId> <group> <action> <decision>`
- `read <group> <action>`
- `player <player-selector>`
- `delete <group> <action>`
- `list <group>`
- `list <group> <scope> <scopeId> <mode>`

### Scope settings
- `<scope>`: `global` | `dimension` | `field`
- `set` supports direct global form without duplicated `global` token.
- `<scopeId>`:
  - `dimension`: dimension `Identifier` or dimension shortId
  - `field`: field `Identifier` or field shortId
  - `global`: placeholder token is accepted but ignored internally

### List modes
- `scope`: list direct decisions from a single scope only
- `merged`: list compressed result with fallback chain `field -> dimension -> global`
- list output is newline formatted for readability (`action=decision` per line)

### Input completion
- `<action>`: auto-suggested from all `PermissionAction` enum names.
- `<decision>`: auto-suggested from all `PermissionDecision` enum names.
- `<scopeId>`: when `scope=field|dimension`, auto-suggests both full id and shortId.
- `<group>`: auto-suggests merged candidates from `RoleGroup` ids and known permission-table groups.

### Group id normalization
- Namespaced groups under `myulib` are normalized to path form for storage/evaluation (`myulib:everyone` => `everyone`).
- This keeps command input and runtime resolution aligned.
