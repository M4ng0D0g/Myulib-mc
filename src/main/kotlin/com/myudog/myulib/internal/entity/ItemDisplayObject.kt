package com.myudog.myulib.internal.entity

import com.myudog.myulib.api.`object`.IFloatingObject
import net.minecraft.entity.EntityType
import net.minecraft.entity.decoration.DisplayEntity.ItemDisplayEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d
import org.joml.Vector3f

internal class ItemDisplayObject(
    private val world: ServerWorld,
    private val itemStack: ItemStack
) : IFloatingObject {

    private var entity: ItemDisplayEntity? = null

    override fun spawn(pos: Vec3d) {
        val display = ItemDisplayEntity(EntityType.ITEM_DISPLAY, world)
        display.setItemStack(itemStack)
        display.setPosition(pos.x, pos.y, pos.z)

        // 1.21 特色：設置插值時間，讓移動變平滑
        display.interpolationDuration = 0

        world.spawnEntity(display)
        this.entity = display
    }

    override fun remove() {
        entity?.discard()
    }

    override fun moveTo(pos: Vec3d, interpolationDuration: Int) {
        entity?.let {
            it.interpolationDuration = interpolationDuration
            it.setPosition(pos.x, pos.y, pos.z)
        }
    }

    override fun setScale(scale: Vector3f, interpolationDuration: Int) {
        entity?.let {
            it.interpolationDuration = interpolationDuration
            // 透過修改實體的 Transformation 數據
            // 這部分在 1.21 需要操作 it.transformation 屬性
        }
    }

    override fun setRotation(leftRotation: Vector3f, interpolationDuration: Int) {
        // 實作旋轉邏輯...
    }
}