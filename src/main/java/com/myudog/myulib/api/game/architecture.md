
## GameManager

註冊 `GameDefinition`，管理 `GameInstance`

提供建立/刪除 `GameInstance` 的方法， `id` 統一使用指令輸入並保證唯一性，而非採用自動生成

提供取得 `GameInstance` 的方法
提供列出所有 `GameInstance` 的方法

## GameDefinition

職責是定義遊戲的「靈魂」。它不儲存資料，而是負責將 資料 (Data)、行為 (Behaviors) 與 狀態 (States) 串接在一起。

createInitialData(C config): 初始化該局遊戲的資料容器（例如：建立房間專屬的 GameData 實例）。

createStateMachine(C config): 定義該遊戲的狀態流轉邏輯（例如：準備中 -> 遊戲中 -> 結算）。

bindBehaviors(GameInstance instance): 這是最重要的部分。
此方法在實例建立時呼叫一次，負責將遊戲邏輯訂閱到房間專屬的事件匯流排或計時器上。

```java
// 在 bindBehaviors 中
@Override
public void bindBehaviors(GameInstance<C, D, S> instance) {

    var bus = instance.getEventBus();
    bus.subscribe(GameStartEvent.class, e -> {
        TimerManager.start(200, timerId -> {
            instance.transition(GameStates.END);
        });
    });

    bus.subscribe(PlayerKillEntityEvent.class, event -> {
        instance.getParticipantEntity(event.getPlayer()).ifPresent(entityId -> {
            instance.getEcsContainer().modify(entityId, ScoreComponent.class, s -> s.add(10));
        });
    });
}
```

createEventBus(): 建立房間隔離的事件分發器。

## GameInstance

房間專屬 EventBus：每個 GameInstance 應該持有一個專屬的 eventBus。

事件橋接：全域事件（如伺服器端的玩家擊殺）應由」派一個全域監聽器捕捉，然後根據玩家的 UUID 查詢其所屬的 GameInstance，最後將事件「窄化發給該實例的區域 eventBus。

## GameConfig

要定義一組 ObjectConfig 列表，建立時需要確保已經設定有效數值，並且經過驗證

```java


```

## GameData

避免過度設計
我目前打算定義各個子系統的definition，在config驗證完畢後讓manager根據definition去建立instance
但要有保證不會建立失敗的方法，之後透過data內部各個definition的數值去manager查詢資料做編輯


## GameState

```java

public interface GameState {
    /** 進入狀態時執行 (例如：發放初始道具) */
    default void onEnter(GameInstance<?, ?, ?> instance) {}
    
    /** 狀態持續期間每 Tick 執行一次 */
    default void onTick(GameInstance<?, ?, ?> instance, long tick) {}
    
    /** 離開狀態時執行 (例如：清理暫時性方塊) */
    default void onExit(GameInstance<?, ?, ?> instance) {}
}

```

## GameObject

GameObject 是遊戲中的預設物件，具有基本的生命週期和事件處理能力。它們可以是NPC、方塊、道具等任何在遊戲中具有行為的實體。
在 GameConfig 中定義 GameObject所需行為和屬性(例如座標)，並在 GameInstance 中根據這些定義來創建和管理它們。
這個應該要改成介面，讓自訂生物實作介面並注入生物類別，建立instance時在目標處生成生物，並且自動將該物件所需的事件自動註冊

```java
```

## 指令介面

處理方式寫在本 package ，註冊到 `MyuLibCommand` 或類似管理中

### 建立/刪除遊戲


- `/myulib:game create <GameDefinitionName> <GameInstanceId>`

`GameDefinition` 需要具有 `NAME` 欄位
自動將 `GameManager` 中的 `GameDefinition.NAME` 註冊到指令集中，並提供補全
若 `GameManager.get(GameInstanceId)` 已存在，則建立失敗、回傳錯誤訊息
建立完畢的 `config` 處於未完成狀態，必須透過 `config set` 指令設定完成後才能啟動遊戲

- `/myulib:game delete <GameInstanceId>`

刪除遊戲實例，若 `GameManager.get(GameInstanceId)` 不存在，則刪除失敗、回傳錯誤訊息


- `/myulib:game list` (列出所有遊戲實例)

列出所有遊戲實例的 `GameInstanceId` 和對應的 `GameDefinitionName`，以及是否已完成設定

### 設定遊戲 config

- `/myulib:game config set <GameInstanceId> <ConfigKey> <ConfigValue>`

設定遊戲實例的 config，`ConfigKey` 和 `ConfigValue`
格式由 `GameConfig<T>` 定義，會根據定義提供 `ConfigKey` 補全
指令執行後會呼叫 `GameInstance.setConfig(ConfigKey, ConfigValue)` 來設定對應的 config 值
不要用 map 實現，而是採用靜態型別判斷，確保 `ConfigKey` 和 `ConfigValue` 的類型安全

- `/myulib:game config get <GameInstanceId> <ConfigKey>`

取得遊戲實例的 config 值，會呼叫 `GameInstance.getConfig(ConfigKey)` 來取得對應的 config 值
不要用 map 實現，而是採用靜態型別判斷，確保 `ConfigKey` 的類型安全

### 啟動遊戲

- `/myulib:game start <GameInstanceId>`

先驗證 `GameInstance` 是否存在，若不存在則回傳錯誤訊息
再驗證 `GameInstance` 的 config 是否已完成設定，若未完成則回傳錯誤訊息
最後呼叫 `GameInstance.start()` 來啟動遊戲，並回傳成功訊息

### 遊戲偵錯/測試 
還沒想到