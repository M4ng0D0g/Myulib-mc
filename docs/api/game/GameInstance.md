# GameInstance API 參考

`GameInstance<S>` 位於 `com.myudog.myulib.api.game.instance`，代表一個已建立的遊戲實例；`team` / `timer` / `object` / `field` 都應視為獨立系統的 bridge 存取。

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
  - 主線文件請分別看 `docs/timer/`、`docs/object/`、`docs/field/`、`docs/team/`。

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
