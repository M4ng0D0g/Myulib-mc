# ECS System

ECS is the base data-flow layer of this project. It manages entities, components, queries, and lifecycle flow.

## 類別架構關係
- `EcsContainer` 是最上層容器，負責 entity 建立、component 存取與 query。
- `ComponentStorage` 是底層密集儲存結構，支援 component 的 add/get/remove/clear。
- `Component` 是所有資料元件的 marker interface。
- `ComponentAddedEvent` 會在 component 被掛載後自動派發到事件系統。
- `Resettable`、`DimensionAware`、`DimensionChangePolicy`、`ComponentLifecycle` 負責 component 的 reset / 維度變更生命週期。
- Game / Region / Logic / UI 系統都建立在 ECS 之上。

## 目前進度
- ✅ ECS 文件已遷移到 canonical `docs/ecs/`。
- ✅ 已具備完整 API 參考與讀法入口。
- ⏳ 後續若有新的 component 類型，會再補對應的 class 頁。

## Public class navigation list
- [EcsCore](EcsCore.md)

## Large demo
```java
EcsContainer container = new EcsContainer();
int entity = world.createEntity();
world.addComponent(entity, new PositionComponent(10, 20));
world.addComponent(entity, new HealthComponent(100));
for (int id : world.query(PositionComponent.class)) {
    // render or update matching entities
}
```

## Reading order
1. `EcsCore.md`
2. Then return to the concrete component/system docs
