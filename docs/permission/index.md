# Permission System

`Permission` evaluates access with four layers:

1. Global
2. Dimension
3. Field
4. User

## Decisions
- `ALLOW`
- `DENY`
- `PASS`

## Public API
- `PermissionDecision`
- `PermissionLayer`
- `PermissionGrant`
- `PermissionSeed`
- `PermissionContext`
- `WorldInteractionPermissionContext`
- `PermissionManager`
- `PermissionAdminService`
- `WorldInteractionPermissionHooks`

## Notes
- A `DENY` should short-circuit the world-interaction flow.
- `PASS` delegates to the next layer.

