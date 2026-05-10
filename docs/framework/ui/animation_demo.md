# UI 動畫與宣告式組合大示範

這份示範的目的，是把 UI 的核心流程一次串起來：

1. 在單一檔案中組合高階 UI 元件
2. 用宣告式方式描述狀態與畫面
3. 透過動畫規格預先定義 `from -> to`
4. 由 screen 端手動決定何時播放動畫
5. 在實際 render 流程中先做 layout，再做 render

> 注意：以下示範偏向**設計樣板**，用來表達本專案預計的使用方式。真正完整的 widget builder / screen API 之後會再持續補齊。

## 一個檔案完成組合、動畫與渲染

```java
package com.myudog.myulib.client.gui.test;

import java.util.concurrent.atomic.AtomicReference;

import com.myudog.myulib.api.animation.AnimationSpec;
import com.myudog.myulib.api.animation.AnimatorComponent;
import com.myudog.myulib.api.animation.Easing;
import com.myudog.myulib.api.animation.Interpolators;
import com.myudog.myulib.api.animation.PlayMode;
import com.myudog.myulib.client.api.ui.component.TransformComponent;
import com.myudog.myulib.client.api.ui.component.WidgetStateComponent;

public final class DemoUIScreen {
    private final AtomicReference<Double> titleOpacity = new AtomicReference<>(0.0);
    private final TransformComponent titleTransform = new TransformComponent();
    private final WidgetStateComponent titleState = new WidgetStateComponent();

    // 1. 預先定義動畫設定：只描述規格，不直接播放
    private final AnimationSpec<Double> titleFadeIn = AnimationSpec.of(
        420L,
        0.0,
        1.0,
        Easing.EASE_OUT_QUAD,
        PlayMode.ONCE,
        Interpolators.DOUBLE
    );

    private final AnimatorComponent<Double> titleAnimator = new AnimatorComponent<>(
        titleFadeIn,
        titleOpacity::set
    );

    public DemoUIScreen() {
        // 2. 宣告式地組裝狀態
        titleTransform.opacity = titleOpacity.get().floatValue();
        titleTransform.y = 18.0f;
        titleState.visible = true;
    }

    public void init() {
        // 3. 手動決定播放時機
        playIntroAnimation();
    }

    public void playIntroAnimation() {
        titleAnimator.play();
    }

    public void update(long deltaMillis) {
        // 4. 每幀更新動畫，再把結果寫回 UI component
        titleAnimator.tick(deltaMillis);
        titleTransform.opacity = titleOpacity.get().floatValue();
    }

    public void renderScreen(int mouseX, int mouseY, float delta) {
        // 5. 實際 render 流程：先 layout，再 render
        // LayoutSystem.update(world, screenWidth, screenHeight);
        // RenderSystem.render(world, drawContext, mouseX, mouseY, delta);
    }

    // 6. 以一個檔案內的高階元件方法，表達宣告式 UI 的組合方式
    private Object buildHeader() {
        // return Box.of(...)
        //     .child(Label.of("Mango UI"))
        //     .child(Label.of("Declarative + Animated"));
        return new Object();
    }

    private Object buildActions() {
        // return Row.of(
        //     Button.of("Play", this::playIntroAnimation),
        //     Button.of("Stop", titleAnimator::stop)
        // );
        return new Object();
    }

    private Object buildRoot() {
        // return Panel.of(
        //     buildHeader(),
        //     buildActions()
        // );
        return new Object();
    }
}
```

## 這個示範刻意展示的設計重點

### 1. 動畫先定義、播放後控制
`AnimationSpec` 先把時間、曲線、播放模式都定義好；真正何時播放由 screen 或事件回呼決定。

### 2. UI 類型不直接耦合動畫引擎
動畫引擎只認得 `AnimationTarget<T>`，不知道對方是 `TransformComponent`、`WidgetStateComponent` 還是未來的自訂元件。

### 3. 宣告式 UI 的核心是「重建描述」
高階元件在一個檔案內被拆成 `buildHeader()`、`buildActions()`、`buildRoot()` 這類方法，讓畫面結構和互動邏輯保持清楚。

### 4. 渲染流程固定化
screen 的更新順序建議固定成：

1. 更新資料 / state
2. 更新動畫
3. 跑 layout
4. 跑 render

這樣動畫與版面才能穩定協作。

## UI 類型套用動畫的建議方式

本專案推薦使用「**Target Adapter**」設計：

- `TransformComponent`：最常拿來做位置、大小、縮放、透明度的動畫
- `WidgetStateComponent`：可用來控制顯示、啟用、互動狀態
- `ComputedTransform`：layout system 算出的結果，通常作為 render 的輸出而不是直接播放目標

如果你之後新增更多 UI 元件，只要讓它暴露對應欄位的 target adapter，就能直接接進動畫系統。

