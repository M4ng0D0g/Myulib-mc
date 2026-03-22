package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.ui.component.FlexDirection

/**
 * [API] 畫布容器。
 * 允許子元件透過 Anchor (錨點) 與 Offset (偏移) 進行自由定位。
 * 適合用於製作 HUD、重疊介面或需要精確定位的場景。
 */
class Canvas : Box() {
    init {
        // 在我們的系統中，將其標記為一種特殊的 Direction 或佈局模式
        // 這裡我們假設 FlexDirection 增加一個 ABSOLUTE 類型
        flexContainer.direction = FlexDirection.ABSOLUTE
    }
}