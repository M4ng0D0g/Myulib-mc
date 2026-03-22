package com.myudog.myulib.api.event

/**
 * 事件訂閱者的優先級基準點 (UInt 型別)。
 * 數值越小代表優先級越高 (0 為絕對最高優先級，最先接收事件)。
 * 你可以傳入任意 UInt 數值 (例如 EventPriority.NORMAL - 10u) 來做精細排序。
 */
object EventPriority {
    const val HIGHEST: UInt = 0u
    const val HIGH: UInt = 1000u
    const val NORMAL: UInt = 2000u
    const val LOW: UInt = 3000u
    const val LOWEST: UInt = 4000u
}