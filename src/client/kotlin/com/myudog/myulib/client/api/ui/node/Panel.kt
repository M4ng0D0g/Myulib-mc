package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.ui.SizeUnit
import com.myudog.myulib.client.api.ui.data.UiSchema
import net.minecraft.text.Text

/**
 * [API] 現代化面板元件。
 * 透過組合 Column 與 Label 實現舊版 PanelComponent 的功能。
 */
class Panel(title: String? = null) : Column() {

    // 內建一個 Label 作為標題
    private var titleLabel: Label? = null

    init {
        // 套用主題中的面板樣式
        textureKey = "panel_bg"
        itemConfig.padding.set(6f)
        flexContainer.spacing = 4f

        // 如果有標題，建立一個 Label 並放在最上方
        title?.let {
            val label = Label(Text.literal("[ $it ]"))
                .withColor(UiSchema.Text.SECONDARY.argb)
            this.titleLabel = label
            addChild(label)
        }
    }

    // 允許動態修改標題
    fun setTitle(newTitle: String) {
        titleLabel?.setText("[ $newTitle ]")
    }
}