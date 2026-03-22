package com.myudog.myulib.client.api.event.events

import com.myudog.myulib.api.event.FailableEvent

/**
 * 當玩家操作滑鼠按鍵時觸發的事件。
 * 實作 FailableEvent，允許攔截者提供取消點擊的原因（例如：UI 阻擋、冷卻時間未到）。
 */
data class MouseButtonEvent(
    val button: Int,
    val action: Int,
    val mods: Int,
    // 提供預設值 null，Listener 攔截時可將其覆寫
    override var errorMessage: String? = null
) : FailableEvent