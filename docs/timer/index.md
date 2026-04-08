# Timer System

The Timer system handles timer creation, triggering, and payload extensions.

## 類別架構關係
- `TimerCore` 是 timer 的核心 API 入口。
- `TimerManager` 是 timer 建立、更新、暫停與恢復的管理中心。
- `TimerEvents` / `TimerPayloads` 負責 timer 與事件 / payload 的對接。
- Game 系統可以透過橋接使用這一層，但 timer 本身是獨立系統。
- Logic 系統會讀取 timer 事件來驅動規則與動作。

## 目前進度
- ✅ Timer 文件已整理到 canonical `docs/timer/`。
- ✅ 與 Game / Logic 的依賴關係已在文件索引中註記。
- ⏳ 若之後補更多 timer mode / payload 型別，會再更新 class 頁。

## Public class navigation list
- [TimerCore](TimerCore.md)
- [TimerManager](TimerManager.md)
- [TimerEvents](TimerEvents.md)
- [TimerPayloads](TimerPayloads.md)
- [RespawnTimerExample](RespawnTimerExample.md)

## Large demo
```java
TimerManager timers = new TimerManager();
timers.schedule("cooldown", 20, () -> System.out.println("done"));
timers.tick();
```

## Reading order
1. `TimerCore.md`
2. `TimerManager.md`
3. `TimerEvents.md`
4. `TimerPayloads.md`
