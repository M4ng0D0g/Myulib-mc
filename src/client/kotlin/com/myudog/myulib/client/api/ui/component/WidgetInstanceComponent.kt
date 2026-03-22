package com.myudog.myulib.client.api.ui.component

import com.myudog.myulib.api.ecs.Component
import com.myudog.myulib.client.api.ui.BaseWidget

/**
 * [Internal] 讓 System 能夠回頭找到 Widget 實例以呼叫 draw()
 */
class WidgetInstanceComponent(val instance: BaseWidget) : Component