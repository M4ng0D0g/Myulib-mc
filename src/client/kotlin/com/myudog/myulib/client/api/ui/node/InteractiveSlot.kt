package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.ui.component.ClickableComponent
import net.minecraft.item.ItemStack

/**
 * [二階元件] 具備進階交互能力的物品槽。
 * 支援右鍵快速裝備、雙擊使用等邏輯。
 */
class InteractiveSlot(
    stack: ItemStack = ItemStack.EMPTY,
    var onRightClick: ((ItemStack) -> Unit)? = null,
    var onDoubleClick: ((ItemStack) -> Unit)? = null
) : ItemSlot(stack) {

    private var lastClickTime: Long = 0

    init {
        // 透過 ClickableComponent 擴展點擊行為；透過 MyulibApiClient 存取元件
        val clickable = com.myudog.myulib.client.api.MyulibApiClient.getComponent<ClickableComponent>(entityId)
        clickable?.onClick = { btn: Int ->
            when (btn) {
                1 -> onRightClick?.invoke(this.stack) // 1 代表右鍵
                0 -> handleDoubleClick()            // 0 代表左鍵
            }
        }
    }

    private fun handleDoubleClick() {
        val now = System.currentTimeMillis()
        if (now - lastClickTime < 300) { // 300ms 判定為雙擊
            onDoubleClick?.invoke(this.stack)
        }
        lastClickTime = now
    }
}