package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.ui.data.ListProvider
import com.myudog.myulib.client.api.ui.data.ObservableListProvider
import com.myudog.myulib.client.api.ui.data.UiDataListener

/**
 * [二階元件] 自動捲動的資料綁定清單。
 * 內建 ScrollBox 與 Column 佈局，適合日誌、任務或商店清單。
 */
class DataBoundList<T>(
    private val provider: ListProvider<T>,
    private val spacing: Float = 2f,
    private val mapper: (T) -> Box
) : ScrollBox(), UiDataListener {

    private val container = Column().apply {
        flexContainer.spacing = spacing
    }

    init {
        // 設定容器為子元件
        this.addChild(container)

        // 綁定響應式監聽 [cite: 2026-03-11]
        if (provider is ObservableListProvider<*>) {
            provider.addListener(this)
        }
        refresh()
    }

    override fun onDataChanged() {
        refresh()
    }

    fun refresh() {
        // Remove existing children from container
        val hc = com.myudog.myulib.client.api.MyulibApiClient.getComponent<com.myudog.myulib.client.api.ui.component.HierarchyComponent>(container.entityId)
        hc?.children?.forEach { cid ->
            com.myudog.myulib.client.api.MyulibApiClient.getComponent<com.myudog.myulib.client.api.ui.component.WidgetInstanceComponent>(cid)?.instance?.let { it.setParent(null) }
        }
        for (i in 0 until provider.size()) {
            container.addChild(mapper(provider.get(i)))
        }
    }
}