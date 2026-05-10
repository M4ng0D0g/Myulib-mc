# RegionManager
## Role
This page is the canonical reference for `RegionManager` in the `region` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
RegionManager API 參考

## 類別
`class RegionManager`

## 公開方法
- `install()`
- `register(region)` / `registerAll(regions)` / `unregister(regionId)`
- `get(regionId)` / `getByOwner(ownerId)` / `getByGameInstance(instanceId)`
- `findAt(x, y, z)`
- `validate(regions)`
- `publish(signal)`
- `registerRule(rule)` / `clearRules()`
- `bindInstance(instance, regions)` / `unbindInstance(instanceId)`

## 用法
```java
RegionManager.register(mainRegion);
RegionManager.bindInstance(gameInstance, java.util.List.of(mainRegion, subRegion));
```

