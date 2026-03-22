package com.myudog.myulib.client.api.event

import com.myudog.myulib.internal.event.EventDispatcherImpl

/**
 * 專門負責處理 Minecraft 客戶端 (Client) 相關事件的總線。
 * 未來如果開發伺服器端，可以再建一個 ServerEventBus。
 * Mixin 攔截到的滑鼠、鍵盤、渲染事件都會發送到這裡。
 */
object ClientEventBus : EventDispatcherImpl()