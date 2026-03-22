package com.myudog.myulib.internal.monitor

import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger

internal object VFXMonitor {
    private val logger = LoggerFactory.getLogger("MyuVFX-Monitor")
    private val particleCount = AtomicInteger(0)

    // 安全閾值：每秒最大粒子數 (20 ticks 總計)
    var maxParticlesPerSecond = 10000
    private var lastResetTime = System.currentTimeMillis()

    /**
     * 嘗試申請生成粒子，若超過閾值則回傳 false
     */
    fun requestSpawn(amount: Int): Boolean {
        checkReset()
        if (particleCount.get() + amount > maxParticlesPerSecond) {
            return false
        }
        particleCount.addAndGet(amount)
        return true
    }

    private fun checkReset() {
        val now = System.currentTimeMillis()
        if (now - lastResetTime > 1000) {
            // 每秒回報一次數據（可選）
            if (particleCount.get() > 0) {
                // logger.info("Current VFX Load: ${particleCount.get()} particles/sec")
            }
            particleCount.set(0)
            lastResetTime = now
        }
    }

    fun getCurrentTPSLoad(): Int = particleCount.get()
}