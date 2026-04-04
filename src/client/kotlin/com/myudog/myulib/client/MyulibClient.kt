package com.myudog.myulib.client

import com.myudog.myulib.api.ecs.EcsWorld
import com.myudog.myulib.client.api.ui.hud.HudManager
import com.myudog.myulib.client.api.command.TestCommandsRegistry
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.loader.api.FabricLoader
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * [Internal] Fabric 客戶端入口點。
 */
object MyulibClient : ClientModInitializer {

    val LOGGER: Logger = LoggerFactory.getLogger("myulib")

    /**
     * 修正點 1：建議改用 val 直接初始化。
     * 因為 EcsWorld 是純數據容器，不依賴 Minecraft 生命週期，
     * 這樣可以確保 inline 函式在任何時刻呼叫都是安全的，且效能更高。
     */
    @PublishedApi
    internal val internalWorld = EcsWorld()

    override fun onInitializeClient() {
        LOGGER.info("MyuLib Client Initializing...")

        HudRenderCallback.EVENT.register { context, deltaAny ->
            // deltaAny may be Float (older mapping) or RenderTickCounter (newer mapping). Extract a float accordingly.
            val deltaFloat: Float = try {
                when (deltaAny) {
                    is Float -> deltaAny
                    else -> {
                        // Try reflectively read a float field or invoke toFloat()
                        try {
                            val k = deltaAny::class.java.getMethod("asFloat")
                            (k.invoke(deltaAny) as Number).toFloat()
                        } catch (_: Throwable) {
                            try {
                                val f = deltaAny::class.java.getMethod("toFloat")
                                (f.invoke(deltaAny) as Number).toFloat()
                            } catch (_: Throwable) {
                                // fallback: use 0f
                                0f
                            }
                        }
                    }
                }
            } catch (_: Throwable) { 0f }

            HudManager.render(context, deltaFloat)
        }

        // 這裡可以放置需要依賴 Minecraft 啟動後的邏輯
        // 例如：註冊渲染事件、監聽按鍵等

        if (FabricLoader.getInstance().isDevelopmentEnvironment) {
            TestCommandsRegistry.register()
        }
    }
}