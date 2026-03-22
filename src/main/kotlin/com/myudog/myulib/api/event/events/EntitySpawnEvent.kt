package com.myudog.myulib.api.event.events

import com.myudog.myulib.api.event.FailableEvent
import net.minecraft.entity.Entity
import net.minecraft.world.World

/**
 * 當任何實體準備生成到世界上時觸發。
 * 實作 FailableEvent，允許攔截者取消特定實體的生成 (CONSUME/FAILED)。
 */
data class EntitySpawnEvent(
    val entity: Entity,
    val world: World,
    override var errorMessage: String? = null
) : FailableEvent