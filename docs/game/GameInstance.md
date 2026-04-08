# GameInstance
## Role
This page is the canonical reference for `GameInstance` in the `game` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
GameInstance API 參考

`GameInstance<S>` 位於 `com.myudog.myulib.api.game.instance`，代表一個已建立的遊戲實例；`team` / `timer` / `object` / `field` 皆是透過獨立系統 bridge 進來的能力。

## 主要欄位/存取
- `getInstanceId()`：實例 id
- `getDefinition()`：對應的 `GameDefinition`
- `getBootstrapConfig()`：啟動配置快照
- `getSpecialObjects()`：特殊物件快照
- `getFeatures()`：feature 容器
- `isEnabled()`：是否可運作
- `getCurrentState()`：目前狀態
- `getTickCount()`：累積 tick

## feature / bridge 操作
- `feature(type)` / `requireFeature(type)`
- `putFeature(feature)` / `removeFeature(type)` / `getFeatureOrCreate(type)`
- `timers()` / `scoreboard()` / `objectBindings()` / `regions()` / `components()` / `logicOrNull()` / `logic()`
  - 這些多屬於過渡或橋接存取；對應的主線文件請看 `docs/timer/`、`docs/object/`、`docs/field/`、`docs/team/`。

## state / lifecycle
- `canTransition(to)`
- `transition(to)` / `transitionUnsafe(to)`
- `resetState()`
- `tick()` / `destroy()`

## special objects
- `hasSpecialObject(id)`
- `requireSpecialObject(id)`

## 用法
```java
GameInstance<?> instance = GameManager.createInstance(
	Identifier.of("myulib", "respawn_game"),
	new GameBootstrapConfig()
);
instance.timers().add(42, "respawn"); // bridge to the standalone timer system
instance.transition(RespawnGameExample.RespawnGameState.ACTIVE);
```
