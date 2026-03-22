package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.MyulibApiClient
import com.myudog.myulib.client.api.ui.component.FlexDirection
import com.myudog.myulib.client.api.ui.component.PanCanvasComponent
import net.minecraft.client.gui.DrawContext

/**
 * [API] 可平移與縮放的畫布。
 * 支援：左鍵拖拽平移、滾輪以滑鼠為中心縮放。
 */
class PanCanvas : Box() {
    val panData = PanCanvasComponent()

    init {
        // 設定佈局模式為絕對定位 (Canvas 模式)
        flexContainer.direction = FlexDirection.ABSOLUTE
        MyulibApiClient.addComponent(entityId, panData)
    }

    override fun draw(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        if (!state.isVisible) return

        // 1. 畫背景與邊框
        super.draw(context, mouseX, mouseY, delta)

        // 2. 開啟 Scissor 裁剪 (防止子元件跑出畫布)
        context.enableScissor(
            computed.x.toInt(),
            computed.y.toInt(),
            (computed.x + computed.w).toInt(),
            (computed.y + computed.h).toInt()
        )

        // 3. 繪製子元件 (其座標已被 LayoutSystem 根據 Pan/Zoom 修正)
        drawChildren(context, mouseX, mouseY, delta)

        context.disableScissor()
    }
}