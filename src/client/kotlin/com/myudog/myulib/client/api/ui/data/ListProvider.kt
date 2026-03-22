package com.myudog.myulib.client.api.ui.data

/**
 * [API] 通用資料供應器介面
 * @param T 資料模型的型別 (例如 ItemStack 或 MyAgentModel)
 */
interface ListProvider<T> {
    fun size(): Int
    fun get(index: Int): T

    // 選擇性實作：資料是否發生變動？
    fun isDirty(): Boolean = false
    fun markClean() {}
}