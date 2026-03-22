package com.myudog.myulib.api.event

/**
 * 定義事件處理後的結果，並指示 Dispatcher 的後續行為。
 */
enum class ProcessResult {
    /** * 不干涉此事件。
     * Dispatcher 行為：繼續將事件傳遞給下一個優先級的訂閱者。
     */
    PASS,

    /** * 吸收/攔截此事件。
     * Dispatcher 行為：立即中斷分發。通常用來通知 Mixin「取消」原版行為。
     */
    CONSUME,

    /** * 成功處理此事件。
     * Dispatcher 行為：立即中斷分發。代表事件已圓滿解決，不需要其他優先級較低的系統插手。
     */
    SUCCESS,

    /** * 處理過程遭遇預期內的失敗（例如條件不符）。
     * Dispatcher 行為：立即中斷分發。可用來通知 Mixin 中止後續行為並可能伴隨錯誤提示。
     */
    FAILED
}