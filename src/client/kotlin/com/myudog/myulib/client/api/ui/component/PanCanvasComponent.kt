package com.myudog.myulib.client.api.ui.component

import com.myudog.myulib.api.ecs.Component

class PanCanvasComponent(
    var panX: Float = 0f,
    var panY: Float = 0f,
    var zoom: Float = 1.0f,
    var isPanning: Boolean = false,

    // 縮放限制
    var minZoom: Float = 0.1f,
    var maxZoom: Float = 5.0f,

    // 交互配置
    var allowLeftClickDrag: Boolean = true,
    var scrollZoomSpeed: Float = 0.15f
) : Component {
    // 輔助方法：螢幕座標轉世界座標 (用於點擊測試)
    fun screenToWorld(screenPos: Float, pan: Float, canvasStart: Float, currentZoom: Float): Float {
        return pan + (screenPos - canvasStart) / currentZoom
    }
}