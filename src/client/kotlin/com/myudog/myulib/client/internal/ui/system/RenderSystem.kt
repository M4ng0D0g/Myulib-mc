package com.myudog.myulib.client.internal.ui.system

import com.myudog.myulib.api.ecs.EcsWorld
import com.myudog.myulib.client.api.ui.component.*
import com.myudog.myulib.client.api.ui.node.ScrollBox
import com.myudog.myulib.client.internal.ui.system.DragDropSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import com.myudog.myulib.client.util.drawItemInGuiWithOverridesCompat

internal object RenderSystem {

    fun render(world: EcsWorld, context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        // 1. 找出所有 Root 元件 (通常是當前開啟的 Screen)
        val rootEntities = world.query(HierarchyComponent::class)
            .filter { world.getComponent<HierarchyComponent>(it)?.parent == null }

        // 2. 第一階段：渲染主 UI 階層 (遞迴)
        for (rootId in rootEntities) {
            renderRecursive(world, rootId, context, mouseX, mouseY, delta)
        }

        // 3. 第二階段：渲染頂層覆蓋 (Top Layer)
        // 確保 Tooltip 與手持物品永遠蓋在所有 UI 之上
        renderTopLayer(world, context, mouseX, mouseY)
    }

    private fun renderRecursive(
        world: EcsWorld,
        entityId: Int,
        context: DrawContext,
        mouseX: Int,
        mouseY: Int,
        delta: Float
    ) {
        val state = world.getComponent<WidgetStateComponent>(entityId) ?: return
        if (!state.isVisible) return

        val widgetComp = world.getComponent<WidgetInstanceComponent>(entityId) ?: return
        val hierarchy = world.getComponent<HierarchyComponent>(entityId) ?: return
        val widget = widgetComp.instance

        // --- 核心：處理 Scissor (針對 ScrollBox 等容器) ---
        // 如果是 ScrollBox，繪製邏輯會在其內部的 draw() 處理裁剪開關
        widget.draw(context, mouseX, mouseY, delta)

        // --- 遞迴子元件 ---
        // 若元件本身會自行管理子元件渲染 (如特殊裁剪容器)，這裡可跳過
        // 但目前架構統一由 System 驅動
        for (childId in hierarchy.children) {
            renderRecursive(world, childId, context, mouseX, mouseY, delta)
        }

        // ...existing code...
    }

    /**
     * 專門渲染全域最頂層的內容
     */
    private fun renderTopLayer(world: EcsWorld, context: DrawContext, mouseX: Int, mouseY: Int) {
        val client = MinecraftClient.getInstance()
        val textRenderer = client.textRenderer

        // A. 渲染手持物品 (Drag & Drop)
        if (DragDropSystem.isHoldingItem()) {
            val stack = DragDropSystem.draggingStack
            // 讓物品跟隨滑鼠移動
            context.drawItem(stack, mouseX - 8, mouseY - 8)
            context.drawItemInGuiWithOverridesCompat(textRenderer, stack, mouseX - 8, mouseY - 8)
        }

        // B. 渲染 Tooltip (提示框)
        // 讀取 InputSystem 偵測到的懸停實體 ID
        val tooltipId = InputSystem.hoveredTooltipEntity
        if (tooltipId != -1) {
            world.getComponent<TooltipComponent>(tooltipId)?.let { tooltip ->
                if (tooltip.text.isNotEmpty()) {
                    // 使用 Minecraft 內建的高品質 Tooltip 渲染
                    context.drawTooltip(textRenderer, tooltip.text, mouseX, mouseY)
                }
            }
        }
    }
}