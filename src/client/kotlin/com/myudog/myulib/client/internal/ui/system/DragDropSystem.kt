package com.myudog.myulib.client.internal.ui.system

import net.minecraft.item.ItemStack

/**
 * [Internal] 物品拖拽管理器。
 * 負責儲存目前正被玩家用滑鼠「拎著」的物品。
 */
internal object DragDropSystem {
    // 玩家「手上」的物品
    var draggingStack: ItemStack = ItemStack.EMPTY

    // 來源實體 ID (如果拖拽失敗，要把物品還回去)
    var sourceSlotId: Int = -1

    fun isHoldingItem(): Boolean = !draggingStack.isEmpty

    /**
     * 嘗試從槽位抓取物品
     */
    fun startDragging(slotId: Int, stack: ItemStack) {
        this.draggingStack = stack.copy()
        this.sourceSlotId = slotId
    }

    /**
     * 清除手上狀態
     */
    fun clear() {
        draggingStack = ItemStack.EMPTY
        sourceSlotId = -1
    }
}