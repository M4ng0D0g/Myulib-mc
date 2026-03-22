package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.MyulibApiClient
import com.myudog.myulib.client.api.ui.component.ClickableComponent
import com.myudog.myulib.client.api.ui.component.DraggableComponent

/**
 * [API] 可拖拽的容器元件。
 */
open class DraggableBox : Box() {
    val dragData = DraggableComponent()

    init {
        MyulibApiClient.addComponent(entityId, dragData)

        // 註冊點擊以啟動拖拽捕捉
        MyulibApiClient.addComponent(entityId, ClickableComponent(
            onClick = {
                // 這裡的邏輯會由 InputSystem 統一接手處理起始座標紀錄
            }
        ))
    }
}