package com.myudog.myulib.client.api.ui.hud

import com.myudog.myulib.api.ecs.EcsWorld
import com.myudog.myulib.client.api.ui.node.Box
import com.myudog.myulib.client.internal.ui.system.*
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext

object HudManager {
    val world = EcsWorld()
    var rootNodeId: Int = -1

    /**
     * 初始化 HUD 配置 (例如你的 PanCanvas)
     */
    fun init(builder: Box.() -> Unit) {
        val root = Box().apply { builder() }
        rootNodeId = root.entityId
    }

    /**
     * 在 Minecraft 每幀渲染 HUD 的階段呼叫
     */
    fun render(context: DrawContext, delta: Float) {
        val client = MinecraftClient.getInstance()
        val w = client.window.scaledWidth.toFloat()
        val h = client.window.scaledHeight.toFloat()

        // 驅動 Myulib 系統
        LayoutSystem.update(world, w, h)
        // HUD 模式下滑鼠座標傳入 -1, -1 代表不處理 Hover
        RenderSystem.render(world, context, -1, -1, delta)
    }
}