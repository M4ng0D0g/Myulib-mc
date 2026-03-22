package com.myudog.myulib.client.api.event.events

import com.myudog.myulib.api.event.Event
import net.minecraft.client.MinecraftClient

/**
 * 客戶端每 Tick 觸發。
 * 適合用來處理 Myu Agent 的客戶端邏輯 (如判斷畫面資訊、更新按鍵狀態)。
 */
data class ClientTickEvent(val client: MinecraftClient) : Event