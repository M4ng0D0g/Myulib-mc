package com.myudog.myulib.api

import com.myudog.myulib.api.ecs.EcsWorld

object MyulibApi {
    val world = EcsWorld()

    fun tick() {
        // 無論是 Server 還是 Client 都會跑的基礎邏輯
        // basicLogicSystem.update(world)
    }
}