# Field System

`Field` is the renamed, independent successor to the old `region` system.

## Public API
- `com.myudog.myulib.api.field.FieldBounds`
- `com.myudog.myulib.api.field.FieldDefinition`
- `com.myudog.myulib.api.field.FieldRole`
- `com.myudog.myulib.api.field.FieldManager`
- `com.myudog.myulib.api.field.FieldAdminService`

## Typical usage
- create a field definition
- register it with `FieldAdminService.create(...)`
- edit it with `FieldAdminService.update(...)`
- delete it with `FieldAdminService.delete(...)`
- open the editor with `FieldAdminService.openEditor(...)`

