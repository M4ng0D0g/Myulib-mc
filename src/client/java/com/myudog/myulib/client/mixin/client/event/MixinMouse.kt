package com.myudog.myulib.client.mixin.event

import com.myudog.myulib.api.event.ProcessResult
import com.myudog.myulib.client.api.event.ClientEventBus
import com.myudog.myulib.client.api.event.events.MouseButtonEvent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.Mouse
import net.minecraft.text.Text
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Mouse::class)
class MixinMouse {

    @Inject(method = ["onMouseButton"], at = [At("HEAD")], cancellable = true)
    private fun onMouseButtonInject(
        window: Long, button: Int, action: Int, mods: Int, ci: CallbackInfo
    ) {
        val event = MouseButtonEvent(button, action, mods)
        val result = ClientEventBus.dispatch(event)

        // 處理失敗狀態並輸出 Event Payload 攜帶的錯誤訊息
        if (result == ProcessResult.FAILED) {
            event.errorMessage?.let { msg ->
                // 在玩家的快捷欄上方 (ActionBar) 顯示紅色錯誤提示
                MinecraftClient.getInstance().player?.sendMessage(Text.literal("§c$msg"), true)
            }
            ci.cancel()
            return
        }

        // 處理一般的攔截 (不一定有錯誤，單純吸收事件)
        if (result == ProcessResult.CONSUME || result == ProcessResult.SUCCESS) {
            ci.cancel()
        }
    }
}