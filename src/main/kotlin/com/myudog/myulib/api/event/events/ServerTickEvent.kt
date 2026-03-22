package com.myudog.myulib.api.event.events

import com.myudog.myulib.api.event.Event
import net.minecraft.server.MinecraftServer

/**
 * 伺服器每 Tick (預設 1 秒 20 次) 觸發。
 * 這是驅動你 ECS 系統 (System) 更新的最完美時機！
 */
data class ServerTickEvent(val server: MinecraftServer) : Event