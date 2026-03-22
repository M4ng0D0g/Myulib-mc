package com.myudog.myulib.api.event

/**
 * 標記一個事件是「可失敗的」。
 * 實作此介面的事件允許 Listener 在回傳 FAILED 或 CONSUME 時，
 * 附帶具體的錯誤訊息或取消原因，供觸發端（如 Mixin）讀取並顯示給玩家。
 */
interface FailableEvent : Event {
    var errorMessage: String?
}