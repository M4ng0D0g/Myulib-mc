# Game 系統 (GameManager & GameInstance)

## 設計目標
管理遊戲流程、玩家參與狀態以及遊戲階段切換。

## 預期功能
- [x] 遊戲實例化與註冊
- [x] 有限狀態機 (FSM) 驅動遊戲流程
- [x] 玩家加入/退出處理
- [ ] 多遊戲實例並行執行
- [ ] 遊戲數據持久化與排行榜

## 已測試功能
- [x] `GameManager` 實例管理
- [x] `GameInstance` 基礎狀態切換
- [x] 基於 `EventBus` 的狀態變更廣播
- [x] 與 `ObjectSystem` 的事件連結
