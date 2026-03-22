package com.myudog.myulib.api.ecs.event

import com.myudog.myulib.api.event.Event
import com.myudog.myulib.api.ecs.Component

/**
 * 當實體被成功添加某個 Component 時觸發的事件。
 * 這是一個單純的通知事件（通常不會失敗或被攔截），因此不需要實作 FailableEvent。
 */
data class ComponentAddedEvent(
    val entityId: Int,
    val component: Component
) : Event