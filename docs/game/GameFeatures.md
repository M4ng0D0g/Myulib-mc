# GameFeatures
## Role
This page is the canonical reference for `GameFeatures` in the `game` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
Game Features API 參考

本頁整理 `game` 既有的 feature / bridge 型別；`team`、`timer`、`object` 的主線文件請優先看各自的獨立系統頁面。

## GameFeature
- `interface GameFeature`
- 只作為 marker interface

## GameFeatureStore
- `put(feature)` / `get(type)` / `require(type)` / `remove(type)` / `clear()` / `snapshot()`

## GameTimerFeature
- 欄位：`timerInstanceIds`, `tags`
- 方法：`add(timerInstanceId)`, `add(timerInstanceId, tag)`, `remove(timerInstanceId)`, `clear()`
- 備註：這是過渡相容的 runtime feature，timer 主線文件請看 `docs/timer/`。

## GameScoreboardFeature
- 欄位：`objectiveId`, `displayName`, `lines`, `values`
- 方法：`setLine(index, value)`, `setValue(key, value)`, `clear()`

## GameRegionFeature
- 欄位：`regionIds`, `mainRegionId`
- 方法：`add(regionId, isMain)`, `remove(regionId)`, `clear()`

## GameComponentFeature
- 欄位：`bindingIds`
- 方法：`add(bindingId)`, `remove(bindingId)`, `clear()`

## GameObjectBindingFeature
- 欄位：`requiredConfigs`, `runtimeBindings`
- 方法：`bind(config, runtimeObject)`, `attachRuntime(id, runtimeObject)`, `getRuntime(id)`, `clear()`
- 備註：這是過渡相容的 runtime feature，object 主線文件請看 `docs/object/`。

## GameLogicFeature<S>
- 欄位：`engine`
- 方法：`bind(instance)`, `publish(signal)`, `publishGameCreated(instance)`, `publishGameDestroyed(instance)`, `publishGameCreatedRaw(instance)`, `publishGameDestroyedRaw(instance)`, `register(rule)`, `clearRules()`, `isBound()`

## 用法
```java
GameTimerFeature timers = instance.timers();
timers.add(42, "respawn");

GameScoreboardFeature scoreboard = instance.scoreboard();
scoreboard.setLine(0, "Hello");
```

