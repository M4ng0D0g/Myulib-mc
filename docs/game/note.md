# Myulib 遊戲引擎架構設計提案 (Myulib Game Engine Architecture Proposal)

## 1. 核心設計理念
本架構採用 **Facade (門面模式)** 與 **Stateless Blueprint (無狀態藍圖)** 設計。`GameInstance` 作為對外的唯一入口，協調底層多個 Feature 系統。所有執行期資料封裝於 `GameData` 內，並透過系統注入 (Injection) 實作功能隔離，確保高度的可維護性與並發安全性。

---

## 2. 核心類別方法與職責說明

### 2.1 GameInstance<C, D, S> (中樞指揮官)
負責管理單局遊戲的完整生命週期與資源協調。

| 方法名稱 | 作用說明 |
| :--- | :--- |
| `initialize()` | 核心初始化入口。負責驗證 Config、呼叫 `GameDefinition` 建立 `GameData`、並執行 `bindBehavior` 綁定邏輯。[cite: 1] |
| `start()` | 正式啟動遊戲，標記 `started = true` 並觸發定義好的啟動行為。 |
| `tick()` | 驅動狀態機 (FSM) 運轉，遞增全域 `tickCount`。 |
| `joinPlayer()` | **[核心門面方法]** 同時處理隊伍分配與 ECS 實體綁定，確保各系統玩家狀態同步。 |
| `leavePlayer()` | 玩家離開時的連鎖清理，包含從隊伍中移除與銷毀 ECS 實體。 |
| `clean()` | 資源重置與清理。負責重置狀態機、解除行為綁定、並觸發 `GameData` 的全系統清理。[cite: 1, 4] |

### 2.2 GameDefinition<C, D, S> (遊戲模式藍圖)
定義遊戲規則與行為的抽象類別。

| 待覆寫方法 | 覆寫職責 (Responsibility) |
| :--- | :--- |
| `createInitialData(config)` | 負責根據當前配置，`new` 出具體的 `GameData` 子類別並注入所需的 Feature。[cite: 4] |
| `bindBehavior(instance)` | **[最關鍵覆寫]** 在此註冊遊戲規則。例如：監聽玩家擊殺事件、設定方塊破壞邏輯等。 |
| `unbindBehavior(instance)` | 對應 `bindBehavior` 的反向操作，確保房間銷毀後監聽器不會殘留導致記憶體洩漏。 |
| `onStart(instance)` | 處理遊戲按下開始鈕後的一次性邏輯，如：生成初始怪物、傳送玩家。 |

### 2.3 GameData (執行期資料容器)
採用組合模式 (Composition) 持有所有功能組件。[cite: 4]

| 功能組件 | 作用說明 |
| :--- | :--- |
| `ECS_FEATURE` | 提供組件存取，管理玩家 UUID 與 ECS EntityID 的映射。[cite: 2] |
| `TEAM_FEATURE` | 負責房間內的隊伍邏輯，確保查詢範圍不超出當前房間。 |
| `OBJECT_FEATURE` | 管理房間內動態生成的 Runtime 物件 (如怪物、裝飾)。 |
| `FIELD_FEATURE` | 負責房間區域判定，並在關閉時與全域 `FieldManager` 同步銷毀。[cite: 3] |

---

## 3. 具體實作規範

### 3.1 跨系統玩家加入 (joinPlayer) 實作
為了達成「最大架構」的自動化，`GameInstance` 的 `joinPlayer` 必須採用連鎖反應寫法。

```java
public boolean joinPlayer(UUID playerId, @Nullable UUID requestedTeam) {
    // 1. 狀態預檢
    if (data == null) return false;

    // 2. 隊伍分配 (Team System)
    UUID targetTeam = resolveJoinTeam(requestedTeam);
    boolean teamSuccess = data.TEAM_FEATURE.moveParticipantToTeam(targetTeam, playerId);
    if (!teamSuccess) return false; // 若隊伍滿了或其他原因失敗，則停止後續動作

    // 3. ECS 實體同步創建 (ECS System)
    // 採用 computeIfAbsent 邏輯，若已存在則不重複創建
    int entityId = data.ECS_FEATURE.getOrCreateParticipant(playerId);

    // 4. 事件廣播
    this.eventBus.dispatch(new PlayerJoinedEvent(playerId, targetTeam, entityId));
    return true;
}
```
**採用此寫法的原因**：此為 **Facade 門面設計**。外部邏輯無需關心 ECS 或隊伍的實作細節，只需呼叫 `joinPlayer`，系統即保證資料的一致性 (Consistency)。

### 3.2 可回滾操作 (TransactionChain) 使用規範
在 `bindBehavior` 進行涉及多個步驟的初始化時 (例如連續放置 5 個生怪磚)，必須使用 `TransactionChain`。

```java
TransactionChain chain = new TransactionChain();
chain.add(() -> spawnA(), () -> removeA())
     .add(() -> spawnB(), () -> removeB());

if (!chain.commit()) {
    // 若 B 失敗，會自動觸發 A 的 rollback，保證地圖不會有殘留物
}
```

---

## 4. 類別關係圖 (Class Relationships)

```mermaid
classDiagram
    class GameInstance {
        -UUID uuid
        -D data
        -StateMachine fsm
        +joinPlayer(UUID)
        +leavePlayer(UUID)
        +clean()
    }

    class GameDefinition {
        <<Abstract>>
        +createInitialData(C)*
        +bindBehavior(Instance)*
    }

    class GameData {
        <<Abstract>>
        +EcsFeature ecs
        +TeamFeature team
        +ObjectFeature obj
        +clean()
    }

    class EcsContainer {
        -BitSet aliveEntities
        +forAll(Class, Action)
    }

    %% 關聯關係
    GameInstance "1" *-- "1" GameData : 持有並驅動
    GameInstance "1" o-- "1" GameDefinition : 根據藍圖運行
    GameData "1" *-- "1" EcsFeature : 組合
    EcsFeature "1" *-- "1" EcsContainer : 封裝存儲
    
    %% 功能連結
    GameInstance ..> TeamManager : 查詢全域隊伍[cite: 1]
    GameData ..> FieldManager : 聯動區域註銷[cite: 3]
```

---

## 5. 待辦與擴充建議 (Roadmap)
1.  **EventBus 管理移轉**：計畫將原本位於 `GameInstance` 的 `eventBus` 移交給全域 `ObjectManager` 或系統層級，以減少單一實例的物件負擔。
2.  **無狀態規則優化**：建議所有在 `bindBehavior` 註冊的 Lambda 均不直接捕獲 `instance` 變數，而是從事件的 `context` 中動態獲取，以利未來可能的平行化 Tick。</C,></C,>