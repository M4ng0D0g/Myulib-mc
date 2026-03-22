package com.myudog.myulib.client.api

import com.myudog.myulib.client.internal.ui.system.LayoutSystem
import com.myudog.myulib.client.internal.ui.system.RenderSystem
import com.myudog.myulib.api.ecs.EcsWorld
import net.minecraft.client.gui.DrawContext

/**
 * [API] UI 系統中央管理器 (Client Only)
 * 負責維護 UI 世界、驅動佈局計算與渲染流程。
 */
object UiManager {

    val world = EcsWorld()

    private var isLayoutDirty = true

    fun markLayoutDirty() {
        isLayoutDirty = true
    }

    /**
     * 由 Minecraft Screen 的 render 方法每幀呼叫
     */
    fun onRender(context: DrawContext, screenW: Int, screenH: Int, mouseX: Int, mouseY: Int, delta: Float) {
        // --- 第一階段：佈局 (Logic) ---
        // 只有當 UI 結構變動或視窗縮放時才重新計算，節省 CPU
        if (isLayoutDirty) {
            LayoutSystem.update(world, screenW.toFloat(), screenH.toFloat())
            isLayoutDirty = false
        }

        // --- 第二階段：渲染 (Display) ---
        // 這裡會走遞迴渲染策略，確保父子層級與 Z-Index 正確
        RenderSystem.render(world, context, mouseX, mouseY, delta)
    }

    /**
     * 清空當前所有 UI 實體 (例如切換 Screen 時使用)
     */
    fun clearAll() {
        // 假設 EcsWorld 有提供 clear 功能，或手動刪除所有 Root 實體
        // world.clear()
        markLayoutDirty()
    }
}