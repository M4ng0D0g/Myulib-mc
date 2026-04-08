# RegionManager API 參考（legacy / transition）

新主線請改看 `docs/field/` 與 `FieldManager`。

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

