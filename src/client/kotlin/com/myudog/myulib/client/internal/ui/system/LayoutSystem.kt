package com.myudog.myulib.client.internal.ui.system

import com.myudog.myulib.api.ecs.EcsWorld
import com.myudog.myulib.client.api.ui.SizeUnit
import com.myudog.myulib.client.api.ui.component.*
import com.myudog.myulib.client.api.ui.data.BoxConstraints
import com.myudog.myulib.client.api.ui.Spacing
import com.myudog.myulib.client.api.ui.node.*
import net.minecraft.client.MinecraftClient
import kotlin.math.max
import kotlin.math.ceil

internal object LayoutSystem {

    fun update(world: EcsWorld, screenW: Float, screenH: Float) {
        val rootEntities = world.query(HierarchyComponent::class)
            .filter { world.getComponent<HierarchyComponent>(it)?.parent == null }

        for (rootId in rootEntities) {
            measureRecursive(world, rootId, BoxConstraints(maxWidth = screenW, maxHeight = screenH))
            calculateRecursive(world, rootId, 0f, 0f, screenW, screenH)
        }
    }

    private fun measureRecursive(world: EcsWorld, entityId: Int, constraints: BoxConstraints): Pair<Float, Float> {
        val transform = world.getComponent<TransformComponent>(entityId) ?: return 0f to 0f
        val item = world.getComponent<FlexItemComponent>(entityId) ?: return 0f to 0f
        val container = world.getComponent<FlexContainerComponent>(entityId)
        val hierarchy = world.getComponent<HierarchyComponent>(entityId) ?: return 0f to 0f
        val computed = world.getComponent<ComputedTransform>(entityId) ?: return 0f to 0f
        val widget = world.getComponent<WidgetInstanceComponent>(entityId)?.instance

        // 1. 獲取內建尺寸 (Intrinsic Size)
        var intrinsicW = 0f
        var intrinsicH = 0f
        when (widget) {
            is Label -> { intrinsicW = MinecraftClient.getInstance().textRenderer.getWidth(widget.content).toFloat(); intrinsicH = 9f }
            is Button -> { intrinsicW = MinecraftClient.getInstance().textRenderer.getWidth(widget.label).toFloat() + 12f; intrinsicH = 20f }
            is ItemSlot -> { intrinsicW = 18f; intrinsicH = 18f }
            is Checkbox -> { intrinsicW = 16f + MinecraftClient.getInstance().textRenderer.getWidth(widget.label).toFloat(); intrinsicH = 12f }
            is ProgressBar -> { intrinsicW = 100f; intrinsicH = 8f }
            is Slider -> { intrinsicW = if (widget.isVertical) 16f else 100f; intrinsicH = if (widget.isVertical) 100f else 16f }
            is TextField -> { intrinsicW = 120f; intrinsicH = 16f }
            is Image -> { intrinsicW = widget.regionW.toFloat(); intrinsicH = widget.regionH.toFloat() }
            is Dropdown -> { intrinsicW = 100f; intrinsicH = 16f }
            is Placeholder -> { intrinsicW = 20f; intrinsicH = 20f }
        }

        // 2. 基礎偏好尺寸
        var preferredW = when (val w = transform.width) {
            is SizeUnit.Fixed -> w.px
            is SizeUnit.Relative -> constraints.maxWidth * w.percent
            is SizeUnit.WrapContent -> intrinsicW
            else -> 0f
        }
        var preferredH = when (val h = transform.height) {
            is SizeUnit.Fixed -> h.px
            is SizeUnit.Relative -> constraints.maxHeight * h.percent
            is SizeUnit.WrapContent -> intrinsicH
            else -> 0f
        }

        // 3. 處理容器內部尺寸
        if (container != null && hierarchy.children.isNotEmpty()) {
            var contentW = 0f; var contentH = 0f
            val innerConstraints = BoxConstraints(
                maxWidth = max(0f, constraints.maxWidth - item.padding.left - item.padding.right),
                maxHeight = if (widget is ScrollBox) Float.MAX_VALUE else max(0f, constraints.maxHeight - item.padding.top - item.padding.bottom)
            )

            var cursorX = 0f; var cursorY = 0f; var rowMaxH = 0f
            var maxChildW = 0f; var maxChildH = 0f

            for (childId in hierarchy.children) {
                val (cw, ch) = measureRecursive(world, childId, innerConstraints)
                val m = world.getComponent<FlexItemComponent>(childId)?.margin ?: Spacing.zero()

                when (container.direction) {
                    FlexDirection.HORIZONTAL -> { contentW += cw + m.horizontal + container.spacing; contentH = max(contentH, ch + m.vertical) }
                    FlexDirection.VERTICAL -> { contentH += ch + m.vertical + container.spacing; contentW = max(contentW, cw + m.horizontal) }
                    FlexDirection.FLOW -> {
                        if (cursorX + cw + m.horizontal > innerConstraints.maxWidth && cursorX > 0) { cursorX = 0f; cursorY += rowMaxH + container.spacing; rowMaxH = 0f }
                        cursorX += cw + m.horizontal + container.spacing
                        rowMaxH = max(rowMaxH, ch + m.vertical)
                        contentW = max(contentW, cursorX); contentH = cursorY + rowMaxH
                    }
                    FlexDirection.GRID -> { maxChildW = max(maxChildW, cw + m.horizontal); maxChildH = max(maxChildH, ch + m.vertical) }
                    FlexDirection.STACK -> { contentW = max(contentW, cw + m.horizontal); contentH = max(contentH, ch + m.vertical) }
                    FlexDirection.ABSOLUTE -> {
                        val ct = world.getComponent<TransformComponent>(childId)
                        contentW = max(contentW, cw + (ct?.offsetX ?: 0f)); contentH = max(contentH, ch + (ct?.offsetY ?: 0f))
                    }
                }
            }

            if (container.direction == FlexDirection.GRID) {
                val cols = container.columns.coerceAtLeast(1)
                val rows = ceil(hierarchy.children.size.toFloat() / cols).toInt()
                contentW = (cols * maxChildW) + (cols - 1) * container.spacing
                contentH = (rows * maxChildH) + (rows - 1) * container.spacing
            }

            if (widget is ScrollBox) widget.scrollData.contentHeight = contentH + widget.scrollData.bottomGap
            if (transform.width is SizeUnit.WrapContent) preferredW = contentW
            if (transform.height is SizeUnit.WrapContent) preferredH = contentH
        }

        computed.w = constraints.constrainWidth(preferredW + item.padding.left + item.padding.right)
        computed.h = constraints.constrainHeight(preferredH + item.padding.top + item.padding.bottom)
        return computed.w to computed.h
    }

    private fun calculateRecursive(world: EcsWorld, entityId: Int, fX: Float, fY: Float, fW: Float, fH: Float) {
        val computed = world.getComponent<ComputedTransform>(entityId) ?: return
        val item = world.getComponent<FlexItemComponent>(entityId) ?: return
        val container = world.getComponent<FlexContainerComponent>(entityId)
        val hierarchy = world.getComponent<HierarchyComponent>(entityId) ?: return
        val widget = world.getComponent<WidgetInstanceComponent>(entityId)?.instance

        computed.x = fX; computed.y = fY; computed.w = fW; computed.h = fH

        if (container != null && hierarchy.children.isNotEmpty()) {
            val innerX = computed.x + item.padding.left
            val innerY = computed.y + item.padding.top
            val innerW = computed.w - item.padding.left - item.padding.right
            val innerH = computed.h - item.padding.top - item.padding.bottom
            val scrollOffset = if (widget is ScrollBox) widget.scrollData.scrollAmount else 0f
            val panCanvas = world.getComponent<PanCanvasComponent>(entityId) // ✅ 取得相機數據

            when (container.direction) {
                FlexDirection.ABSOLUTE -> {
                    for (childId in hierarchy.children) {
                        val ct = world.getComponent<TransformComponent>(childId) ?: continue
                        val cc = world.getComponent<ComputedTransform>(childId) ?: continue

                        if (panCanvas != null) { // ── PanCanvas 投影模式 ──
                            val z = panCanvas.zoom
                            val cx = innerX + (ct.offsetX - panCanvas.panX) * z
                            val cy = (innerY - scrollOffset) + (ct.offsetY - panCanvas.panY) * z
                            calculateRecursive(world, childId, cx, cy, cc.w * z, cc.h * z)
                        } else { // ── 一般 Canvas 錨點模式 ──
                            val cx = innerX + (innerW * ct.anchor.xFactor) - (cc.w * ct.anchor.xFactor) + ct.offsetX
                            val cy = (innerY - scrollOffset) + (innerH * ct.anchor.yFactor) - (cc.h * ct.anchor.yFactor) + ct.offsetY
                            calculateRecursive(world, childId, cx, cy, cc.w, cc.h)
                        }
                    }
                }
                FlexDirection.STACK -> {
                    for (childId in hierarchy.children) {
                        val ci = world.getComponent<FlexItemComponent>(childId) ?: continue
                        val cc = world.getComponent<ComputedTransform>(childId) ?: continue
                        calculateRecursive(world, childId, innerX + ci.margin.left, innerY + ci.margin.top - scrollOffset, innerW - ci.margin.horizontal, innerH - ci.margin.vertical)
                    }
                }
                FlexDirection.GRID -> distributeGridSpace(world, hierarchy.children, container, innerX, innerY - scrollOffset)
                FlexDirection.FLOW -> distributeFlowSpace(world, hierarchy.children, container, innerW, innerX, innerY - scrollOffset)
                else -> distributeFlexSpace(world, hierarchy.children, container, innerW, innerH, innerX, innerY - scrollOffset)
            }
        }
    }

    private fun distributeGridSpace(world: EcsWorld, children: List<Int>, container: FlexContainerComponent, startX: Float, startY: Float) {
        val cols = container.columns.coerceAtLeast(1)
        var cellW = 0f; var cellH = 0f
        for (id in children) {
            val comp = world.getComponent<ComputedTransform>(id) ?: continue
            val item = world.getComponent<FlexItemComponent>(id) ?: continue
            cellW = max(cellW, comp.w + item.margin.horizontal); cellH = max(cellH, comp.h + item.margin.vertical)
        }
        children.forEachIndexed { i, id ->
            val item = world.getComponent<FlexItemComponent>(id) ?: return@forEachIndexed
            val fx = startX + (i % cols) * (cellW + container.spacing) + item.margin.left
            val fy = startY + (i / cols) * (cellH + container.spacing) + item.margin.top
            calculateRecursive(world, id, fx, fy, cellW - item.margin.horizontal, cellH - item.margin.vertical)
        }
    }

    private fun distributeFlowSpace(world: EcsWorld, children: List<Int>, container: FlexContainerComponent, innerW: Float, startX: Float, startY: Float) {
        var cursorX = 0f; var cursorY = 0f; var rowMaxH = 0f
        for (childId in children) {
            val item = world.getComponent<FlexItemComponent>(childId) ?: continue
            val comp = world.getComponent<ComputedTransform>(childId) ?: continue
            if (cursorX + comp.w + item.margin.horizontal > innerW && cursorX > 0) { cursorX = 0f; cursorY += rowMaxH + container.spacing; rowMaxH = 0f }
            calculateRecursive(world, childId, startX + cursorX + item.margin.left, startY + cursorY + item.margin.top, comp.w, comp.h)
            cursorX += comp.w + item.margin.horizontal + container.spacing
            rowMaxH = max(rowMaxH, comp.h + item.margin.vertical)
        }
    }

    private fun distributeFlexSpace(world: EcsWorld, children: List<Int>, container: FlexContainerComponent, innerW: Float, innerH: Float, startX: Float, startY: Float) {
        val isHoriz = container.direction == FlexDirection.HORIZONTAL
        var totalWeight = 0f; var fixedSpace = 0f
        for (childId in children) {
            val item = world.getComponent<FlexItemComponent>(childId) ?: continue
            val comp = world.getComponent<ComputedTransform>(childId) ?: continue
            if (item.weight > 0f) totalWeight += item.weight else fixedSpace += if (isHoriz) comp.w else comp.h
            fixedSpace += container.spacing + (if (isHoriz) item.margin.horizontal else item.margin.vertical)
        }
        fixedSpace -= container.spacing
        val remainingSpace = max(0f, (if (isHoriz) innerW else innerH) - fixedSpace)
        var currentPos = if (isHoriz) startX else startY
        when (container.mainAlign) { MainAxisAlignment.CENTER -> currentPos += remainingSpace / 2; MainAxisAlignment.END -> currentPos += remainingSpace; else -> {} }
        for (childId in children) {
            val item = world.getComponent<FlexItemComponent>(childId) ?: continue
            val comp = world.getComponent<ComputedTransform>(childId) ?: continue
            val mainSize = if (item.weight > 0f) (remainingSpace * (item.weight / totalWeight)) else (if (isHoriz) comp.w else comp.h)
            val crossSize = if (container.crossAlign == CrossAxisAlignment.STRETCH) (if (isHoriz) innerH else innerW) else (if (isHoriz) comp.h else comp.w)
            val crossOffset = when (container.crossAlign) { CrossAxisAlignment.CENTER -> (if (isHoriz) innerH - crossSize else innerW - crossSize) / 2; CrossAxisAlignment.END -> (if (isHoriz) innerH - crossSize else innerW - crossSize); else -> 0f }
            calculateRecursive(world, childId, if (isHoriz) currentPos + item.margin.left else startX + item.margin.left + crossOffset, if (isHoriz) startY + item.margin.top + crossOffset else currentPos + item.margin.top, if (isHoriz) mainSize else crossSize, if (isHoriz) crossSize else mainSize)
            currentPos += mainSize + container.spacing + (if (isHoriz) item.margin.horizontal else item.margin.vertical)
        }
    }
}