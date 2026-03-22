package com.myudog.myulib.client.api.ui.node

import com.myudog.myulib.client.api.ui.SizeUnit

/**
 * [API] 分頁組容器。
 * 自動處理標籤按鈕與內容面板的切換邏輯。
 */
class TabGroup : Column() {
    private val tabHeader = Row().apply { transform.height = SizeUnit.Fixed(22f) }
    private val contentStack = Stack().apply { itemConfig.weight = 1f }

    private val tabs = mutableListOf<TabButton>()
    private val pages = mutableListOf<Box>()

    init {
        addChild(tabHeader)
        addChild(contentStack)
    }

    /**
     * 新增一個分頁
     * @param label 標籤文字
     * @param contentBuilder 分頁內容的 DSL
     */
    fun addPage(label: String, contentBuilder: Box.() -> Unit) {
        val index = pages.size

        // 1. 建立標籤按鈕
        val btn = TabButton(label) { selectPage(index) }
        tabs.add(btn)
        tabHeader.addChild(btn)

        // 2. 建立內容面板
        val page = Box().apply {
            contentBuilder()
            state.isVisible = (index == 0) // 預設只顯示第一頁
        }
        pages.add(page)
        contentStack.addChild(page)

        if (index == 0) btn.isSelected = true
    }

    private fun selectPage(index: Int) {
        pages.forEachIndexed { i, page ->
            page.state.isVisible = (i == index)
            tabs[i].isSelected = (i == index)
        }
    }
}