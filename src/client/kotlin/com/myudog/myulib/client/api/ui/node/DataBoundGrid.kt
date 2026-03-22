package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.ui.LeafWidget
import com.myudog.myulib.client.api.ui.data.ListProvider
import com.myudog.myulib.client.api.ui.data.ObservableListProvider
import com.myudog.myulib.client.api.ui.data.UiDataListener

/**
 * [二階元件] 支援自動同步的資料綁定網格
 */
class DataBoundGrid<T>(
    private val provider: ListProvider<T>,
    columns: Int = 9,
    spacing: Float = 2f,
    private val mapper: (T) -> LeafWidget
) : Grid(columns, spacing), UiDataListener {

    init {
        // 如果供應器是可觀察的，則進行綁定
        if (provider is ObservableListProvider<*>) {
            provider.addListener(this)
        }
        refresh()
    }

    /**
     * 實作 UiDataListener 接口
     * 當資料變動時，自動觸發重繪邏輯
     */
    override fun onDataChanged() {
        // 由於 LayoutSystem 會在下一幀處理，這裡我們只需標記需要重新整理
        refresh()
    }

    fun refresh() {
        // Clear existing children via hierarchy
        val hierarchyComp = com.myudog.myulib.client.api.MyulibApiClient.getComponent<com.myudog.myulib.client.api.ui.component.HierarchyComponent>(this.entityId)
        hierarchyComp?.children?.forEach { cid ->
            com.myudog.myulib.client.api.MyulibApiClient.getComponent<com.myudog.myulib.client.api.ui.component.WidgetInstanceComponent>(cid)?.instance?.let { it.setParent(null) }
        }
        for (i in 0 until provider.size()) {
            val data = provider.get(i)
            addChild(mapper(data))
        }
    }

    // 💡 重要：元件銷毀時應移除監聽，避免記憶體洩漏
    // 這部分可以整合進你的 ECS 生命週期系統
}