# AnimationSystem
## Role
This page is the canonical reference for `AnimationSystem` in the `ui` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
AnimationSystem

Path: `src/client/java/com/myudog/myulib/client/internal/ui/system/AnimationSystem.java`

Description

負責集中註冊、管理與 tick 所有動畫執行個體。它不負責定義動畫規格；動畫規格由 `AnimationSpec` 與 `AnimatorComponent` 持有。

Public API

- `long totalTickedMillis()`
- `int size()`
- `boolean contains(String id)`
- `<T> Handle<T> register(String id, AnimatorComponent<T> animator)`
- `<T> AnimatorComponent<T> animator(String id)`
- `void unregister(String id)`
- `void clear()`
- `boolean play(String id)`
- `boolean pause(String id)`
- `boolean resume(String id)`
- `boolean stop(String id)`
- `void tick(long deltaMillis)`

Nested Handle API

- `String id()`
- `AnimatorComponent<T> animator()`
- `void play()`
- `void pause()`
- `void resume()`
- `void stop()`
- `void unregister()`

Key behaviors

- `tick(...)` 會依序更新所有已註冊動畫。
- `register(...)` 以 id 作為唯一鍵，不允許重複註冊。
- `Handle<T>` 提供手動控制單一動畫的便利入口。
- 本系統不直接依賴特定 UI 類型，UI 元件只要提供 `AnimationTarget<T>` 即可接入。

Usage example

```java
AnimationSystem system = new AnimationSystem();
AnimationSpec<Double> spec = AnimationSpec.of(
    300L,
    0.0,
    1.0,
    Easing.EASE_OUT_QUAD,
    PlayMode.ONCE,
    Interpolators.DOUBLE
);
AnimatorComponent<Double> animator = new AnimatorComponent<>(spec, value::set);
AnimationSystem.Handle<Double> handle = system.register("title-fade", animator);

handle.play();
system.tick(16L);
```

Screen integration recommendation

建議在 screen 更新流程中採用：

1. 更新狀態資料
2. `AnimationSystem.tick(deltaMillis)`
3. `LayoutSystem.update(...)`
4. `RenderSystem.render(...)`

這樣動畫補間值會先更新，再交由 layout / render 消費。
