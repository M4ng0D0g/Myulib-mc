package com.myudog.myulib.client.api.ui.data

/**
 * [API] 資料變動監聽器
 */
interface UiDataListener {
    fun onDataChanged()
}

/**
 * [API] 具備觀察者功能的資料供應器
 */
abstract class ObservableListProvider<T> : ListProvider<T> {
    private val listeners = mutableListOf<UiDataListener>()

    /**
     * 訂閱資料變動
     */
    fun addListener(listener: UiDataListener) {
        listeners.add(listener)
    }

    /**
     * 取消訂閱
     */
    fun removeListener(listener: UiDataListener) {
        listeners.remove(listener)
    }

    /**
     * 當資料更新時，由子類別呼叫此方法發送廣播
     */
    protected fun notifyChanged() {
        listeners.forEach { it.onDataChanged() }
    }
}