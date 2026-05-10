# FloatingCore
## Role
This page is the canonical reference for `FloatingCore` in the `floating` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
Floating API

This section covers the Java floating-object helpers used by the VFX system.

## Public types

- `com.myudog.myulib.api.floating.IFloatingObject`
- `com.myudog.myulib.api.MyuVFX`

## Main entry point

```java
IFloatingObject obj = MyuVFX.createItemObject(ServerLevel, itemStack);
obj.spawn(new Vec3(0.0, 64.0, 0.0));
```

## Notes

- `IFloatingObject` is the Java contract for floating visuals.
- `ItemDisplayObject` is the default server-side implementation for item display entities, but it remains an internal class.
- `MyuVFX.createItemObject(...)` returns the interface type so callers can swap implementations later.