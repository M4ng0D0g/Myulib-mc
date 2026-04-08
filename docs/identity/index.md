# Identity System

`Identity` manages player identity groups and their permissions.

## Public API
- `com.myudog.myulib.api.identity.IdentityGroupDefinition`
- `com.myudog.myulib.api.identity.IdentityManager`
- `com.myudog.myulib.api.identity.IdentityAdminService`

## Notes
- A player may belong to multiple identity groups.
- Groups can carry permission grants.
- Use `IdentityAdminService.openEditor(...)` to request a configuration UI.

