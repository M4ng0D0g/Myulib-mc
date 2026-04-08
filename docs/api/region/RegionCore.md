# Region 系統核心 API 參考（legacy / transition）

本頁保留舊 `RegionModels` 的文件；新主線請看 `docs/field/`。

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

