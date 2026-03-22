package com.myudog.myulib.client.api.ui.component

import com.myudog.myulib.api.ecs.Component
import com.myudog.myulib.client.api.ui.Align
import com.myudog.myulib.client.api.ui.Spacing

data class FlexItemComponent(
    var margin: Spacing = Spacing.zero(),
    var padding: Spacing = Spacing.zero(),

    // 自身在交叉軸上的對齊方式 (例如：在 Row 裡決定自己要靠上還是靠下)
    var crossAlign: Align = Align.START,

    // 佔比權重
    var weight: Float = 0f
) : Component