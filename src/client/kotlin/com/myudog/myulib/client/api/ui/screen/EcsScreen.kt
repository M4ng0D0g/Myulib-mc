package com.myudog.myulib.client.api.ui.screen

import com.myudog.myulib.api.ecs.EcsWorld
import com.myudog.myulib.client.api.ui.node.Box
import com.myudog.myulib.client.internal.ui.system.*
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

/**
 * [API] 通用 ECS 介面容器。
 * 你不再需要繼承它，只需在建立時傳入 build 邏輯。
 */
open class EcsScreen(
    title: Text,
    private val builder: Box.() -> Unit // 傳入 DSL 區塊
) : Screen(title) {

    protected val world = EcsWorld()
    protected var rootNodeId: Int = -1

    override fun init() {
        // 1. 建立根節點 (全螢幕透明容器)
        val root = Box().apply {
            // 設定全螢幕尺寸邏輯可在 LayoutSystem 處理
            builder()
        }
        rootNodeId = root.entityId

        // 2. 啟動診斷 (只在第一次渲染或資源重載時執行)
        // NineSliceDiagnostics.diagnoseOncePerSession(client!!.textureManager)
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        // A. 驅動佈局系統
        LayoutSystem.update(world, width.toFloat(), height.toFloat())

        // B. 驅動渲染系統 (包含 Tooltip 與手持物品)
        RenderSystem.render(world, context, mouseX, mouseY, delta)
    }

    // --- 轉發事件至 InputSystem ---

    // Non-overriding ECS-specific input entry points. These deliberately avoid
    // matching names/signatures from different mappings to prevent override
    // signature problems across Minecraft mappings. Call these from InputSystem
    // adapters or wherever the integration with the Screen input layer occurs.
    fun ecsMouseMoved(x: Double, y: Double) {
        InputSystem.onMouseMove(world, rootNodeId, x, y)
    }

    fun ecsMouseClicked(x: Double, y: Double, button: Int): Boolean {
        return InputSystem.onMouseDown(world, rootNodeId, x, y, button)
    }

    fun ecsMouseReleased(x: Double, y: Double, button: Int): Boolean {
        InputSystem.onMouseReleased(world)
        return true
    }

    fun ecsMouseScrolled(x: Double, y: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
        return InputSystem.onMouseScrolled(world, x, y, verticalAmount)
    }

    // ... 鍵盤事件同理轉發至 InputSystem.onKeyPressed ...
}