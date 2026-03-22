package com.myudog.myulib.api.event

import com.myudog.myulib.internal.event.EventDispatcherImpl

/**
 * 專門負責處理 Minecraft 伺服器端 (Server/Common) 邏輯事件的總線。
 * 包含 Tick 更新、實體生成、方塊破壞等。
 */
object ServerEventBus : EventDispatcherImpl()