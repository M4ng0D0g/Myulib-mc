# Object 系統 (ObjectSystem)

## 設計目標
提供一個高層次的抽象，讓開發者能快速建立「具有特殊行為」的實體或方塊，而不需要直接處理複雜的 Mixin 或 NMS。

## 預期功能
- [x] 物件生命週期管理 (Spawn/Remove)
- [x] 行為 (Behavior) 系統掛載
- [x] 原生事件自動攔截轉發
- [ ] 物件持久化 (Serialization)
- [ ] 跨伺服器同步優化

## 已測試功能
- [x] `IObjectDef` 定義註冊
- [x] `AttackableBeh` 傷害攔截
- [x] `InteractableBeh` 互動攔截
- [x] `MineableBeh` 挖掘攔截
- [x] 與 `GameInstance` 解耦驗證
