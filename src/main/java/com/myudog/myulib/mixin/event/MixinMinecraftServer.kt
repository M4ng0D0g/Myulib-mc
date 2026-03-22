package com.myudog.myulib.mixin.event

import com.myudog.myulib.api.event.ServerEventBus
import com.myudog.myulib.api.event.events.ServerTickEvent
import net.minecraft.server.MinecraftServer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.util.function.BooleanSupplier

@Mixin(MinecraftServer::class)
abstract class MixinMinecraftServer {

    @Inject(method = ["tick"], at = [At("TAIL")])
    private fun onServerTick(shouldKeepTicking: BooleanSupplier, ci: CallbackInfo) {
        // 在伺服器 Tick 結束時觸發我們的事件
        ServerEventBus.dispatch(ServerTickEvent(this as MinecraftServer))
    }
}