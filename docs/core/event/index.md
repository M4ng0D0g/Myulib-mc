# Event System

The Event system provides event creation, dispatch, and subscription for cross-system communication.

## 類別架構關係
- `Event` 是所有事件的 marker interface。
- `FailableEvent` 提供錯誤訊息與失敗回傳能力。
- `ProcessResult` 定義 listener 對事件的處理結果。
- `EventPriority` 決定 listener 的執行順序。
- `EventListener<T>` 是事件監聽器函式型別。
- `EventDispatcherImpl` 是核心同步匯流排。
- `ServerEventBus` 是 server/common 的共用入口。
- ECS、Timer、Game 的狀態變更與動作通知都會透過這套事件層傳遞。

## 目前進度
- ✅ 事件核心 API 已整理到 canonical `docs/event/`。
- ✅ `EcsContainer.eventBus` / game logic 的事件流已在文件中標示。
- ⏳ 若之後新增更多事件型別，會再補對應的 class 頁與進度註記。

## Public class navigation list
- [EventCore](EventCore.md)

## Large demo
```java
EventBus bus = new EventBus();
bus.on("ui.clicked", event -> System.out.println(event.payload()));
bus.emit("ui.clicked", Map.of("button", "start"));
```

## Reading order
1. `EventCore.md`
