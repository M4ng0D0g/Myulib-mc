package com.myudog.myulib.client.mixin.event

import com.myudog.myulib.client.api.event.ClientEventBus
import com.myudog.myulib.client.api.event.events.HudRenderEvent
import net.minecraft.client.gui.hud.InGameHud
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(InGameHud::class)
abstract class MixinInGameHud {

    // 依據你的遊戲版本，這裡的方法簽名可能會包含 DrawContext
    @Inject(method = ["render"], at = [At("TAIL")])
    private fun onRenderHud(tickDelta: Float, ci: CallbackInfo) { // 簡化版參數
        ClientEventBus.dispatch(HudRenderEvent(tickDelta))
    }
}