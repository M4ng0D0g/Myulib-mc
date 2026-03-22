package com.myudog.myulib.client.api.ui.component

import com.myudog.myulib.api.ecs.Component
import net.minecraft.text.Text

/**
 * [ECS Component] 標記元件具備提示框功能。
 * 支援簡單文字或自定義 UI 節點。
 */
class TooltipComponent(
    var text: List<Text> = emptyList(),
    var delayTicks: Int = 10 // 懸停多久後顯示
) : Component