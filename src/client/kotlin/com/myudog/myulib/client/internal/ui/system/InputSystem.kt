package com.myudog.myulib.client.internal.ui.system

import com.myudog.myulib.api.ecs.EcsWorld
import com.myudog.myulib.client.api.ui.component.*
import com.myudog.myulib.client.internal.ui.system.DragDropSystem
import kotlin.math.round

internal object InputSystem {

    var hoveredTooltipEntity: Int = -1
    private var lastMouseX: Float = 0f
    private var lastMouseY: Float = 0f

    // ── 公開事件接口 (Public Events) ──────────────────────────────────────────

    fun onMouseMove(world: EcsWorld, rootId: Int, mouseX: Double, mouseY: Double) {
        hoveredTooltipEntity = -1
        updateHoverRecursive(world, rootId, mouseX, mouseY)
        lastMouseX = mouseX.toFloat()
        lastMouseY = mouseY.toFloat()
    }

    fun onMouseDown(world: EcsWorld, rootId: Int, mx: Double, my: Double, button: Int): Boolean {
        world.query(TextFieldComponent::class).forEach {
            world.getComponent<WidgetStateComponent>(it)?.isFocused = false
        }

        val hitId = hitTestRecursive(world, rootId, mx, my, button)

        if (hitId != -1 && button == 0) {
            val slot = world.getComponent<ItemSlotComponent>(hitId)
            if (slot != null && !slot.isLocked) {
                handleItemInteraction(hitId, slot)
                return true
            }

            world.getComponent<SliderComponent>(hitId)?.let { it.isDragging = true }
            world.getComponent<PanCanvasComponent>(hitId)?.let { it.isPanning = true }

            val drag = world.getComponent<DraggableComponent>(hitId)
            val trans = world.getComponent<TransformComponent>(hitId)
            if (drag != null && trans != null) {
                drag.isDragging = true
                drag.dragStartMouseX = mx.toFloat()
                drag.dragStartMouseY = my.toFloat()
                drag.initialOffsetX = trans.offsetX
                drag.initialOffsetY = trans.offsetY
            }

            world.getComponent<WidgetStateComponent>(hitId)?.let {
                if (world.hasComponent<TextFieldComponent>(hitId)) it.isFocused = true
            }
        }

        lastMouseX = mx.toFloat()
        lastMouseY = my.toFloat()
        return hitId != -1
    }

    fun onMouseDragged(world: EcsWorld, mx: Double, my: Double) {
        val mouseX = mx.toFloat()
        val mouseY = my.toFloat()
        val deltaX = mouseX - lastMouseX
        val deltaY = mouseY - lastMouseY

        world.query(SliderComponent::class).forEach { id ->
            val data = world.getComponent<SliderComponent>(id) ?: return@forEach
            val comp = world.getComponent<ComputedTransform>(id) ?: return@forEach
            if (data.isDragging) updateSliderValue(data, comp, mx)
        }

        world.query(PanCanvasComponent::class).forEach { id ->
            val pan = world.getComponent<PanCanvasComponent>(id) ?: return@forEach
            if (pan.isPanning) {
                pan.panX -= deltaX / pan.zoom
                pan.panY -= deltaY / pan.zoom
            }
        }

        world.query(DraggableComponent::class).forEach { id ->
            val drag = world.getComponent<DraggableComponent>(id) ?: return@forEach
            val trans = world.getComponent<TransformComponent>(id) ?: return@forEach
            if (drag.isDragging) {
                trans.offsetX = drag.initialOffsetX + (mouseX - drag.dragStartMouseX)
                trans.offsetY = drag.initialOffsetY + (mouseY - drag.dragStartMouseY)
            }
        }

        lastMouseX = mouseX
        lastMouseY = mouseY
    }

    fun onMouseReleased(world: EcsWorld) {
        world.query(SliderComponent::class).forEach { world.getComponent<SliderComponent>(it)?.isDragging = false }
        world.query(DraggableComponent::class).forEach { world.getComponent<DraggableComponent>(it)?.isDragging = false }
        world.query(PanCanvasComponent::class).forEach { world.getComponent<PanCanvasComponent>(it)?.isPanning = false }
    }

    fun onMouseScrolled(world: EcsWorld, mx: Double, my: Double, amount: Double): Boolean {
        // 1. 優先處理 PanCanvas 縮放
        world.query(PanCanvasComponent::class).forEach { id ->
            if (isPointInside(world, id, mx, my)) {
                val pan = world.getComponent<PanCanvasComponent>(id) ?: return@forEach
                val comp = world.getComponent<ComputedTransform>(id) ?: return@forEach
                val oldZoom = pan.zoom
                val factor = if (amount > 0) 1.15f else 0.85f
                pan.zoom = (pan.zoom * factor).coerceIn(pan.minZoom, pan.maxZoom)
                val localX = mx.toFloat() - comp.x
                val localY = my.toFloat() - comp.y
                pan.panX += localX / oldZoom - localX / pan.zoom
                pan.panY += localY / oldZoom - localY / pan.zoom
                return true
            }
        }

        // 2. 處理 ScrollBox 捲動
        world.query(ScrollComponent::class).forEach { id ->
            if (isPointInside(world, id, mx, my)) {
                val scroll = world.getComponent<ScrollComponent>(id) ?: return@forEach
                val comp = world.getComponent<ComputedTransform>(id) ?: return@forEach
                val maxScroll = scroll.contentHeight - comp.h
                scroll.scrollAmount = (scroll.scrollAmount - amount.toFloat() * 20f).coerceIn(0f, maxScroll)
                return true
            }
        }
        return false
    }

    // ── 內部處理邏輯 (Internal Logic) ────────────────────────────────────────

    private fun handleItemInteraction(hitId: Int, slot: ItemSlotComponent) {
        if (!DragDropSystem.isHoldingItem()) {
            if (!slot.stack.isEmpty) {
                DragDropSystem.startDragging(hitId, slot.stack)
                slot.stack = net.minecraft.item.ItemStack.EMPTY
            }
        } else {
            val handStack = DragDropSystem.draggingStack
            val slotStack = slot.stack
            if (slotStack.isEmpty) {
                slot.stack = handStack
                DragDropSystem.clear()
            } else if (net.minecraft.item.ItemStack.areItemsEqual(handStack, slotStack)) {
                slot.stack.increment(handStack.count)
                DragDropSystem.clear()
            } else {
                val temp = slot.stack.copy()
                slot.stack = handStack
                DragDropSystem.draggingStack = temp
            }
        }
    }

    private fun updateHoverRecursive(world: EcsWorld, entityId: Int, x: Double, y: Double) {
        val state = world.getComponent<WidgetStateComponent>(entityId) ?: return
        if (!state.isVisible) return

        val wasHovered = state.isHovered
        state.isHovered = isPointInside(world, entityId, x, y)

        if (state.isHovered) {
            if (world.hasComponent<TooltipComponent>(entityId)) hoveredTooltipEntity = entityId
            if (!wasHovered) world.getComponent<ClickableComponent>(entityId)?.onHover?.invoke(true)
        } else if (wasHovered) {
            world.getComponent<ClickableComponent>(entityId)?.onHover?.invoke(false)
        }

        world.getComponent<HierarchyComponent>(entityId)?.children?.forEach { updateHoverRecursive(world, it, x, y) }
    }

    // ── 核心輔助方法 (Helpers) ─────────────────────────────────────────────

    /**
     * [演算法] 遞迴點擊測試。
     * 關鍵：採用「逆序遍歷子元件」，確保重疊時最上層的元件優先攔截點擊。
     */
    private fun hitTestRecursive(world: EcsWorld, entityId: Int, x: Double, y: Double, btn: Int): Int {
        val state = world.getComponent<WidgetStateComponent>(entityId)
        if (state?.isVisible == false || state?.isEnabled == false) return -1

        val dropdown = world.getComponent<DropdownComponent>(entityId)
        val hierarchy = world.getComponent<HierarchyComponent>(entityId) ?: return -1

        // 1. 優先子元件 (逆序遍歷，對應渲染層級)
        for (i in hierarchy.children.indices.reversed()) {
            val res = hitTestRecursive(world, hierarchy.children[i], x, y, btn)
            if (res != -1) return res
        }

        // 2. 特殊處理：Dropdown 展開列表的點擊
        if (dropdown != null && dropdown.isExpanded) {
            val comp = world.getComponent<ComputedTransform>(entityId) ?: return -1
            val listH = minOf(dropdown.maxVisibleOptions, dropdown.options.size) * 14 + 2
            // 下拉列表區塊判定 (16 為主體高度)
            if (x in comp.x..(comp.x + comp.w) && y in (comp.y + 16)..(comp.y + 16 + listH)) {
                val idx = ((y - (comp.y + 16)) / 14).toInt()
                if (idx in dropdown.options.indices) {
                    dropdown.selectedIndex = idx
                    dropdown.onSelect(idx)
                }
                dropdown.isExpanded = false
                return entityId
            }
        }

        // 3. 基礎矩形判定
        if (isPointInside(world, entityId, x, y)) {
            world.getComponent<ClickableComponent>(entityId)?.onClick?.invoke(btn)
            return entityId
        } else {
            // 點擊外部時關閉下拉選單
            dropdown?.isExpanded = false
        }
        return -1
    }

    /**
     * [演算法] 點在矩形內判定。
     * 考慮了 Dropdown 展開時的擴展碰撞區。
     */
    private fun isPointInside(world: EcsWorld, entityId: Int, x: Double, y: Double): Boolean {
        val comp = world.getComponent<ComputedTransform>(entityId) ?: return false
        val dropdown = world.getComponent<DropdownComponent>(entityId)

        // 標準 AABB 判定
        var hit = x >= comp.x && x <= (comp.x + comp.w) && y >= comp.y && y <= (comp.y + comp.h)

        // 如果是展開的 Dropdown，擴大 y 軸的判定範圍
        if (dropdown != null && dropdown.isExpanded) {
            val listH = minOf(dropdown.maxVisibleOptions, dropdown.options.size) * 14 + 2
            hit = hit || (x in comp.x..(comp.x + comp.w) && y in (comp.y)..(comp.y + 16 + listH))
        }
        return hit
    }

    /**
     * [演算法] 更新 Slider 數值。
     * 將滑鼠螢幕位置映射回 [min, max] 區間，並套用步進 (Step)。
     */
    private fun updateSliderValue(data: SliderComponent, comp: ComputedTransform, mx: Double) {
        val percent = ((mx - comp.x) / comp.w).coerceIn(0.0, 1.0)
        var newValue = data.min + percent * (data.max - data.min)

        // 如果有設定步進值 (例如 0.1)，則進行捨入
        if (data.step > 0) {
            newValue = round(newValue / data.step) * data.step
        }

        if (newValue != data.value) {
            data.value = newValue
            data.onValueChanged(newValue)
        }
    }
}