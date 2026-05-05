# MyuLib-MC 架構圖生成總結

## 生成的架構文件清單

### 核心系統 (Core Systems)
1. **object_architecture.mmd** - 物件定義與運行時系統
   - Definition 層級：BaseObjDef、BlockObjDef、EntityObjDef、具體實現
   - Runtime 層級：BaseObjRt、BlockObjRt、EntityObjRt、具體運行時
   - Behavior 系統：IObjectBeh、IBlockBeh、IEntityBeh、具體行為
   - Event 系統：所有物件相關事件

2. **object_definition_detail.mmd** - 物件定義層詳細架構 ✨新增
   - Property 系統：Property<T>、Property 註冊表、值存儲
   - Block 定義：BlockObjDef、MineableObjDef、InteractableObjDef
   - Entity 定義：EntityObjDef、AttackableObjDef、DecorativeObjDef
   - Logic 定義：ProximityTriggerObjDef、RespawnPointObjDef
   - Token & Identifier 系統：token 存儲、標識符派生

3. **object_runtime_detail.mmd** - 物件運行時層詳細架構 ✨新增
   - 狀態管理：ObjectState、狀態轉換、生命週期鉤子
   - Block 運行時：BlockObjRt、MineableObjRt、InteractableObjRt
   - Entity 運行時：EntityObjRt、AttackableObjRt、DecorativeObjRt
   - Logic 運行時：ProximityTriggerObjRt、RespawnPointObjRt
   - Property 訪問層、Copy 機制、位置追蹤、行為管理

4. **object_behavior_detail.mmd** - 物件行為系統詳細架構 ✨新增
   - 行為核心：IObjectBeh、IBlockBeh、IEntityBeh 層級
   - Block 行為：InteractableBeh、MineableBeh
   - Entity 行為：AttackableBeh
   - 行為生命週期：Addition、onInitialize、onDestroy
   - 事件集成、運行時綁定、事件流

5. **object_event_detail.mmd** - 物件事件系統詳細架構 ✨新增
   - 事件核心：Event、FailableEvent 基類
   - Block 事件：BlockBreakEvent、BlockInteractEvent
   - Entity 事件：EntityDamageEvent、EntityDeathEvent、EntityInteractEvent
   - 物件事件：ObjectMineEvent、ObjectInteractEvent、ObjectDamageEvent、ObjectDeathEvent
   - 事件發射器映射、事件處理流程、監聽器模式、事件總線整合

6. **game_architecture.mmd** - 遊戲管理系統
   - 核心接口：GameConfig、GameDefinition、GameInstance、GameData
   - 狀態機：GameState、GameStateMachine、BasicGameStateMachine
   - 遊戲示例：TemplateArenaGameDefinition、TicTacToeGameDefinition
   - 生命週期管理

7. **event_architecture.mmd** - 事件系統（通用）
   - 核心事件：Event、FailableEvent、ProcessResult
   - 事件總線：ServerEventBus、EventListener
   - 物件事件：BlockBreakEvent、BlockInteractEvent、EntityDamageEvent 等
   - 監聽器系統

### 數據定義系統 (Data Definition Systems)

8. **team_architecture.mmd** - 隊伍系統
   - 定義：TeamDefinition、TeamColor、TeamFlag
   - 管理：TeamManager、TeamAdminService
   - 存儲：TeamStorage
   - 權限整合

9. **field_architecture.mmd** - 場域系統
   - 定義：FieldDefinition
   - 管理：FieldManager
   - 可視化：FieldVisualizationManager、FieldVisualizationMode
   - 網絡同步

10. **rolegroup_architecture.mmd** - 角色組系統
    - 定義：RoleGroupDefinition
    - 管理：RoleGroupManager
    - 成員管理：MembershipManager
    - 組織層級：GroupHierarchy

11. **hologram_architecture.mmd** - 全息圖系統
    - 定義：HologramDefinition、HologramStyle
    - 管理：HologramManager
    - 運行時：HologramObject、HologramDisplay
    - 網絡渲染

12. **timer_architecture.mmd** - 計時器系統
    - 定義：TimerDefinition、TimerMode、TimerTickBasis
    - 運行時：TimerInstance、TimerBinding
    - 快照：TimerSnapshot
    - 動作系統：TimerAction、DelayedAction、RepeatingAction

### 系統服務 (System Services)

13. **permission_architecture.mmd** - 權限系統
    - 核心：PermissionManager、PermissionAction、PermissionDecision
    - 作用域：PermissionScope、ScopeLayer、PermissionTable
    - 守門人：PermissionGate
    - 預定義權限

14. **command_architecture.mmd** - 指令系統
    - 核心：CommandRegistry、CommandAction、CommandContext
    - 訪問指令服務：AccessCommandService
    - 指令類型：FieldCommands、TeamCommands、GameCommands、PermissionCommands
    - 權限整合

15. **ui_architecture.mmd** - UI 系統
    - 橋接：ConfigurationUiBridge、NoopConfigurationUiBridge
    - 側邊欄：SidebarComponent、SidebarManager
    - 計分板：TablistComponent、TablistManager
    - 網絡同步：UINetworkHandler

### 引擎層系統 (Engine Layer Systems)

16. **ecs_architecture.mmd** - ECS 系統
    - 核心：EcsContainer、Entity、Component
    - 存儲：ComponentStorage、SparseSet
    - 查詢：Query、QueryBuilder、ComponentIterator
    - 生命週期：IComponentLifecycle

17. **animation_architecture.mmd** - 動畫系統
    - 動畫規格：AnimationSpec、Keyframe、PlayMode
    - 播放：AnimationPlayer、AnimationScheduler、PlaybackState
    - 插值：ValueInterpolator、Interpolators
    - 緩動：Easing、EaseInOut、BounceEasing 等

### 系統總覽

18. **system_overview.mmd** - 整體架構圖
    - 展示所有系統的整體關係
    - 數據流向
    - 事件傳播
    - 系統間的依賴

---

## 架構特點

### 模塊化設計
- 每個子系統都有獨立的 Definition（定義層）和 Runtime（運行時層）
- 清晰的層級結構便於維護和擴展

### Token-Based 標識
- 所有 Definition 使用 `token: String` 作為主鍵
- `toIdentifier()` 方法統一生成命名空間識別符
- 格式：`namespace:route/token`

### 權限系統
- 精細化的權限控制
- 支持多層作用域（全局、隊伍、場域、遊戲）
- 角色組自動授予權限

### 事件驅動
- 核心事件系統統一所有異步操作
- 行為模式用於物件交互
- 命令系統通過事件反射應用狀態變化

### 存儲持久化
- 每個系統配備獨立的 Storage 層
- NBT I/O 支持
- 自動序列化/反序列化

### ECS 架構
- 靈活的組件系統
- 高效的稀疏集存儲
- 支持複雜查詢

### 動畫支持
- 統一的動畫框架
- 多種緩動函數
- UI 和物件動畫一體化

---

## 使用方式

所有 `.mmd` 文件都可以用以下方式查看：

1. **GitHub/GitLab** - 直接在 Web 界面中渲染
2. **Mermaid Live Editor** - https://mermaid.live
3. **VSCode** - 安裝 Markdown Preview Mermaid Support 擴展
4. **其他工具** - 任何支持 Mermaid 的工具

---

## 文件位置

所有架構圖都存儲在：
```
docs/architecture/
├── 系統總覽
│   └── system_overview.mmd
│
├── 物件系統 (Object System) - 4個詳細子圖 ✨新增
│   ├── object_architecture.mmd (整體架構)
│   ├── object_definition_detail.mmd (定義層詳細)
│   ├── object_runtime_detail.mmd (運行時層詳細)
│   ├── object_behavior_detail.mmd (行為系統詳細)
│   └── object_event_detail.mmd (事件系統詳細)
│
├── 核心系統
│   ├── game_architecture.mmd
│   └── event_architecture.mmd
│
├── 數據系統
│   ├── team_architecture.mmd
│   ├── field_architecture.mmd
│   ├── rolegroup_architecture.mmd
│   ├── hologram_architecture.mmd
│   └── timer_architecture.mmd
│
├── 服務系統
│   ├── permission_architecture.mmd
│   ├── command_architecture.mmd
│   └── ui_architecture.mmd
│
└── 引擎系統
    ├── ecs_architecture.mmd
    └── animation_architecture.mmd
```

---

## 架構圖說明符號

- **實線箭頭** `-->` - 直接依賴或繼承
- **虛線箭頭** `-.->` - 弱依賴或實現
- **子圖** `subgraph` - 系統或模塊分組
- **顏色區分** - 不同系統層級的視覺區分

---

## 新增架構圖詳情

### object_definition_detail.mmd
**用途**：展示物件定義層的詳細構成和 Property 系統
**關鍵內容**：
- Property<T> 系統：POS、BLOCK_STATE、BOUNDING_BOX 等屬性
- 六種 Definition 類：Mineable、Interactable、Attackable、Decorative、ProximityTrigger、RespawnPoint
- Token 和 Identifier 的派生邏輯

### object_runtime_detail.mmd
**用途**：展示物件運行時層的狀態管理和生命週期
**關鍵內容**：
- ObjectState 狀態轉換
- Runtime 類的層級結構
- Copy 機制的反射實現
- 靜態/動態位置追蹤

### object_behavior_detail.mmd
**用途**：展示行為系統如何綁定到物件運行時
**關鍵內容**：
- IObjectBeh 層級結構
- 三種具體行為：InteractableBeh、MineableBeh、AttackableBeh
- 行為生命週期：Addition → onInitialize → onDestroy

### object_event_detail.mmd
**用途**：展示事件流從 Minecraft 事件到物件事件的轉換
**關鍵內容**：
- Minecraft 事件（BlockBreak、EntityDamage）
- MyuLib 攔截事件（BlockBreakEvent、EntityDamageEvent）
- 物件事件（ObjectMineEvent、ObjectDamageEvent）
- 事件總線的發布-訂閱模式

---

*生成時間：2026-04-25*
*新增詳細圖表數量：4 個（object.definition、object.runtime、object.behavior、object.event）*
*總架構圖數量：18 個*

