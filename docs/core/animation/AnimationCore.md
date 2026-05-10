# AnimationCore
## Role
This page is the canonical reference for `AnimationCore` in the `animation` docs area.
## Unique responsibility
It documents the public API, intent, and practical usage of this class without mixing in unrelated systems.
## Practical writing
Use this page when you need the class-level contract, then follow the field and method sections below.
## Fields
- Fields are listed in the existing API content below.
## Methods
- Methods are listed in the existing API content below.
Animation 核心 API 參考

本頁說明 UI 動畫第一版的核心型別。這一版的設計目標是：**先預先定義動畫設定，再由呼叫端手動決定何時播放、暫停、恢復或停止**。

## 主要型別

### `Easing`
變化曲線列舉。

- `LINEAR`
- `EASE_IN_QUAD`
- `EASE_OUT_QUAD`
- `EASE_IN_OUT_QUAD`
- `EASE_IN_CUBIC`
- `EASE_OUT_CUBIC`
- `EASE_IN_OUT_CUBIC`
- `SMOOTH_STEP`

### `PlayMode`
播放模式列舉。

- `ONCE`：播放一次，結束後停在終點
- `LOOP`：到尾端後回到起點循環播放
- `PING_PONG`：正向播放到尾端後反向播放

### `PlaybackState`
動畫執行狀態。

- `IDLE`
- `PLAYING`
- `PAUSED`
- `STOPPED`
- `FINISHED`

### `ValueInterpolator<T>`
值型別補間函式。

```java
@FunctionalInterface
public interface ValueInterpolator<T> {
    T interpolate(T startValue, T endValue, double progress);
}
```

### `AnimationTarget<T>`
動畫輸出目標。你可以把它理解成「動畫值要寫到哪裡」的抽象。

- `apply(T value)`：把目前補間結果寫入目標
- `AnimationTarget.of(Consumer<T>)`：把任意 setter 包成 target

### `AnimationSpec<T>`
動畫定義本體，負責描述：

- `durationMillis`
- `startValue`
- `endValue`
- `easing`
- `playMode`
- `interpolator`
- `id`

`AnimationSpec` 是不可變設定物件；它**不負責播放**，只描述動畫長什麼樣。

### `AnimatorComponent<T>`
動畫播放中的 runtime 狀態。

它會保存：

- 目前 `PlaybackState`
- 已經走過的時間 `elapsedMillis`
- 已完成循環數 `completedCycles`
- `PING_PONG` 時的正反向方向
- 目前最後套用的值 `currentValue`

常用方法：

- `play()`
- `pause()`
- `resume()`
- `stop()`
- `seek(long elapsedMillis)`
- `tick(long deltaMillis)`

## 典型用法

```java
AnimationSpec<Double> fadeIn = AnimationSpec.of(
    300L,
    0.0,
    1.0,
    Easing.EASE_OUT_QUAD,
    PlayMode.ONCE,
    Interpolators.DOUBLE
);

AtomicReference<Double> opacity = new AtomicReference<>(0.0);
AnimatorComponent<Double> animator = new AnimatorComponent<>(fadeIn, opacity::set);

animator.play();
animator.tick(150L);
```

## 套用到 UI 類型的設計

這一版採用 **Target Adapter** 的方式處理不同 UI 類型：

1. 先決定要動畫的「值型別」：例如 `Float`、`Double`、`Integer`
2. 再把該值型別寫進 UI component 的某個欄位
3. UI component 本身只需要暴露一個 `AnimationTarget<T>`，動畫系統不需要知道它是哪種 widget

例如 `TransformComponent` 可以直接提供：

- `xTarget()`
- `yTarget()`
- `scaleXTarget()`
- `opacityTarget()`

未來若要支援 `WidgetStateComponent`、`ProgressBarComponent` 或自訂元件，也只要再補一個 adapter，不需要改動畫核心。

