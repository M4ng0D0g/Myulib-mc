package com.myudog.myulib.api

import com.myudog.myulib.api.`object`.IFloatingObject
import com.myudog.myulib.internal.entity.ItemDisplayObject
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld

object MyuVFX {
    /**
     * 創建一個懸浮的物品模型
     */
    fun createItemObject(world: ServerWorld, itemStack: ItemStack): IFloatingObject {
        return ItemDisplayObject(world, itemStack)
    }
}