package com.myudog.myulib.api.event.listener

import com.myudog.myulib.api.event.Event
import com.myudog.myulib.api.event.ProcessResult

/**
 * 事件訂閱者介面。
 * 宣告為 `fun interface`，支援傳統 Class 實作，也支援 Lambda 簡寫。
 */
fun interface EventListener<T : Event> {

    /**
     * 處理接收到的事件。
     * @param event 觸發的事件實例
     * @return 處理結果，決定 Dispatcher 是否繼續傳遞
     */
    fun handle(event: T): ProcessResult
}