package com.myudog.myulib.client.api.ui.hud

import com.myudog.myulib.client.api.ui.screen.EcsScreen
import com.myudog.myulib.client.internal.ui.system.InputSystem
import com.myudog.myulib.client.internal.ui.system.LayoutSystem
import com.myudog.myulib.client.internal.ui.system.RenderSystem
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text

/**
 * [Internal] HUD 互動專用的透明 Screen。
 * 當玩家按下熱鍵時開啟，讓滑鼠可以操作常駐在畫面上的 HUD 元件。
 */
class HudInteractionScreen : EcsScreen(Text.literal("HUD Interaction"), {}) {

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        // 核心邏輯：不使用 super.render，改為驅動全域的 HudManager.world
        val w = width.toFloat()
        val h = height.toFloat()

        // 1. 驅動佈局 (確保縮放與平移位置正確)
        LayoutSystem.update(HudManager.world, w, h)

        // 2. 驅動渲染 (傳入真實滑鼠座標，讓 Hover 與 Tooltip 生效)
        RenderSystem.render(HudManager.world, context, mouseX, mouseY, delta)
    }

    /**
     * Input event handlers were intentionally removed from this class to avoid
     * mapping-specific Screen overrides. HUD input should be routed via the
     * central InputSystem adapters (e.g. call InputSystem.onMouseDown/onMouseScrolled
     * with HudManager.world) from the place where HUD interaction is wired up.
     */

    // HUD 互動時不應暫停遊戲，讓 Agent 繼續行動
    override fun shouldPause(): Boolean = false
}