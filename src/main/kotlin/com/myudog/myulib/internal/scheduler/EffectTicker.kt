package com.myudog.myulib.internal.scheduler

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import java.util.concurrent.CopyOnWriteArrayList

internal object EffectTicker {
    // 儲存所有當前活動的動畫任務
    private val activeTasks = CopyOnWriteArrayList<() -> Boolean>()

    fun register() {
        ServerTickEvents.START_SERVER_TICK.register { _ ->
            val iterator = activeTasks.iterator()
            while (iterator.hasNext()) {
                val task = iterator.next()
                // 如果任務返回 false，代表已結束，將其移除
                if (!task()) activeTasks.remove(task)
            }
        }
    }

    fun addTask(task: () -> Boolean) {
        activeTasks.add(task)
    }
}