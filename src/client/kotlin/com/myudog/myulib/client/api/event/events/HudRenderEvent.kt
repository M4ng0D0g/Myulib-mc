package com.myudog.myulib.client.api.event.events

import com.myudog.myulib.api.event.Event

/**
 * 遊戲畫面上層 UI (HUD) 渲染時觸發。
 * 這是你未來畫出自訂介面、Agent 狀態條的必經之路。
 * (註：這裡可依據你的 Fabric 版本傳入 DrawContext 或 MatrixStack，這裡先以 tickDelta 示意)
 */
data class HudRenderEvent(val tickDelta: Float) : Event