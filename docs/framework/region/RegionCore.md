# RegionCore
## Role
This page is the canonical reference for `RegionCore` in the `region` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
Region 系統核心 API 參考

本頁集中說明 `RegionModels` 內的區域型別。

## RegionModels.RegionBounds
- 欄位：`minX`, `minY`, `minZ`, `maxX`, `maxY`, `maxZ`
- 方法：`isZeroSized()`, `contains(x, y, z)`, `intersects(other)`

## RegionModels.RegionRole
- `MAIN`
- `SUB`

## RegionModels.RegionDefinition
- 欄位：`id`, `ownerId`, `bounds`, `role`, `gameInstanceId`, `metadata`

## RegionModels.RegionSignal
- `RegionRegisteredSignal(region)`
- `RegionUnregisteredSignal(region)`
- `RegionEnteredSignal(region, entityId, position, previousRegionId)`
- `RegionExitedSignal(region, entityId, position, nextRegionId)`
- `RegionBoundarySignal(region, entityId, from, to, reason)`

## RegionModels.RegionContext
- 欄位：`region`, `signal`, `gameInstance`, `metadata`

## RegionModels.RegionCondition / RegionAction
- `test(context)` / `execute(context)`

## RegionModels.RegionRule
- 欄位：`id`, `signalType`, `conditions`, `actions`, `priority`
- `matches(signal)`

## 用法
```java
RegionModels.RegionDefinition region = new RegionModels.RegionDefinition(
    Identifier.of("myulib", "arena_main"),
    Identifier.of("myulib", "arena_game"),
    new RegionModels.RegionBounds(0.0, 0.0, 0.0, 100.0, 80.0, 100.0),
    RegionModels.RegionRole.MAIN,
    null,
    java.util.Map.of()
);
```

