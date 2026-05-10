# Animation System

The Animation system provides manual playback, looping, easing curves, and UI target adapters.

## 類別架構關係
- `Easing` 定義曲線。
- `PlayMode` 定義播放方式。
- `AnimationSpec<T>` 描述動畫規格。
- `AnimatorComponent<T>` 保存 runtime 狀態。
- `AnimationTarget<T>` / `ValueInterpolator<T>` / `Interpolators` 負責把補間結果寫回 UI 或其他目標。
- `AnimationSystem` 是多個 animator 的集中 tick / 手動控制管理器。
- UI 系統的 `TransformComponent` / `WidgetStateComponent` 會用 animation target adapter 接上這一層。

## 目前進度
- ✅ 動畫核心已實作並通過 `runAnimationTests`。
- ✅ `AnimationSystem` 已完成註冊、tick、手動控制與基本管理。
- ✅ UI target adapter 已與 `TransformComponent` / `ComputedTransform` 設計對齊。
- ⏳ 未來若要做 sequence / parallel / keyframe，再在此補充。

## Public class navigation list
- [AnimationCore](AnimationCore.md)

## Large demo
```java
AnimationSpec<Double> spec = AnimationSpec.of(
    300L,
    0.0,
    1.0,
    Easing.EASE_OUT_QUAD,
    PlayMode.ONCE,
    Interpolators.DOUBLE
);
AnimatorComponent<Double> animator = new AnimatorComponent<>(spec, value::set);
AnimationSystem system = new AnimationSystem();
var handle = system.register("title-fade", animator);
handle.play();
system.tick(16L);
```

## Reading order
1. `AnimationCore.md`
