package com.myudog.myulib.client.mixin.event

import com.myudog.myulib.client.api.event.ClientEventBus
import com.myudog.myulib.client.api.event.events.ClientTickEvent
import net.minecraft.client.MinecraftClient
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(MinecraftClient::class)
abstract class MixinMinecraftClient {

    @Inject(method = ["tick"], at = [At("TAIL")])
    private fun onClientTick(ci: CallbackInfo) {
        ClientEventBus.dispatch(ClientTickEvent(this as MinecraftClient))
    }
}