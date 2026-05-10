# Region System (legacy / transition)

`Region` is the old name for the independent `Field` system. Prefer `docs/field/index.md` for the current architecture.

## 類別架構關係
- `RegionCore` / `RegionManager` 仍保留作過渡相容；新架構請以 `Field` 系統為準。
- `Field` 會被用來把 instance 內的地圖區域、邏輯區段或玩法區間分開管理。
- 與 ECS / Game / Logic / Component 的聯動主要發生在 instance 更新與事件處理流程中。

## 目前進度
- ✅ 舊 `region` 文件保留作過渡參考。
- ✅ 新主線請改看 `docs/field/` 與 `docs/access/`。
- ⏳ 若需要舊名相容層，會在此補充。

## Public class navigation list
- [RegionCore](RegionCore.md)
- [RegionManager](RegionManager.md)

## Large demo
```java
RegionManager regions = new RegionManager();
var region = regions.create("spawn");
region.addEntity(entityId);
regions.tickAll();
```

## Reading order
1. `RegionCore.md`
2. `RegionManager.md`
