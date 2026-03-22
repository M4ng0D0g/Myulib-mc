package com.myudog.myulib.client.api.ui.component

import com.myudog.myulib.api.ecs.Component
import net.minecraft.item.ItemStack

/**
 * [ECS Component] 標記實體為物品槽，持有 Minecraft 的 ItemStack。
 */
class ItemSlotComponent(
    var stack: ItemStack = ItemStack.EMPTY,
    var isLocked: Boolean = false // 是否禁止拿取 (例如唯讀面板)
) : Component