package com.myudog.myulib.mixin.event

import com.myudog.myulib.api.event.ProcessResult
import com.myudog.myulib.api.event.ServerEventBus
import com.myudog.myulib.api.event.events.EntitySpawnEvent
import net.minecraft.entity.Entity
import net.minecraft.server.world.ServerWorld
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(ServerWorld::class)
abstract class MixinServerWorld {

    @Inject(method = ["spawnEntity"], at = [At("HEAD")], cancellable = true)
    private fun onSpawnEntity(entity: Entity, cir: CallbackInfoReturnable<Boolean>) {
        val event = EntitySpawnEvent(entity, this as ServerWorld)
        val result = ServerEventBus.dispatch(event)

        // 如果有人攔截了這個實體的生成
        if (result == ProcessResult.CONSUME || result == ProcessResult.FAILED) {
            // 阻止 Minecraft 生成它，並回傳 false
            cir.returnValue = false
        }
    }
}