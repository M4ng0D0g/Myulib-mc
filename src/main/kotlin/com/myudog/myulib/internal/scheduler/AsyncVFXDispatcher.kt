package com.myudog.myulib.internal.scheduler

import net.minecraft.server.MinecraftServer
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

internal object AsyncVFXDispatcher {
    private val executor = Executors.newFixedThreadPool(2) // 專門處理數學運算的線程池

    /**
     * 異步計算座標，計算完後回到主執行緒執行渲染（如生成粒子）
     */
    fun runAsyncChain(server: MinecraftServer, calculation: () -> Unit, onComplete: () -> Unit) {
        CompletableFuture.runAsync({
            calculation()
        }, executor).thenRunAsync({
            // 必須回到主執行緒才能操作世界與實體
            server.execute { onComplete() }
        })
    }
}