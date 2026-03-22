package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.ui.BaseWidget
import com.myudog.myulib.client.api.ui.node.Label
import com.myudog.myulib.client.api.ui.node.Image

/**
 * [API] 受限的按鈕元件。
 * 僅允許添加內容類元件 (Label, Image, Box)，禁止嵌套按鈕。
 */
open class Button(onClick: () -> Unit = {}) : Box() {

    var label: String = ""

    init {
        // 設定按鈕的點擊邏輯 (略，同前次對話)
        this.textureKey = "button_default" // 預設使用註冊表中的 Key
    }

    /**
     * 核心修正：覆寫添加子元件的方法進行過濾
     */
    override fun addChild(child: BaseWidget) {
        if (isValidContent(child)) {
            super.addChild(child)
        } else {
            // 拋出異常或紀錄警告，防止奇怪的嵌套
            throw IllegalArgumentException("Button 內僅允許添加內容元件 (Label, Image, Box)，禁止嵌套 ${child::class.simpleName}")
        }
    }

    private fun isValidContent(child: BaseWidget): Boolean {
        // 白名單機制：只允許 Label, Image 或純背景 Box
        return child is Label || child is Image || (child is Box && child !is Button)
    }
}