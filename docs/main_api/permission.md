# main.api.permission

Primary classes:
- [`PermissionManager`](permission/PermissionManager.md)
- [`PermissionAction`](permission/PermissionAction.md)
- [`PermissionDecision`](permission/PermissionDecision.md)
- [`PermissionScope`](permission/PermissionScope.md)
- [`PermissionTable`](permission/PermissionTable.md)
- [`PermissionGate`](permission/PermissionGate.md)
- [`ScopeLayer`](permission/ScopeLayer.md)
- [`NbtPermissionStorage`](permission/NbtPermissionStorage.md)
- [`SqlPermissionStorage`](permission/SqlPermissionStorage.md)

Responsibilities:
- multi-scope permission storage and evaluation
- global/dimension/field scoped group rule management
- merged and scoped permission listing support in commands

## Architecture
- [Architecture diagram (Mermaid)](permission/architecture.mmd)
