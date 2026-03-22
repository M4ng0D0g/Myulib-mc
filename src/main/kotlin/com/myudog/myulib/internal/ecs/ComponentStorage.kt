package com.myudog.myulib.internal.ecs

import com.myudog.myulib.api.ecs.Component

@Suppress("UNCHECKED_CAST")
class ComponentStorage<T : Component>(initialCapacity: Int = 1024) {
    // 密集陣列：存儲真正的組件實例
    internal var components = arrayOfNulls<Any>(initialCapacity)
    // 密集索引：存儲對應的實體 ID (用於遍歷)
    internal var dense = IntArray(initialCapacity) { -1 }
    // 稀疏陣列：下標為實體 ID，值為在密集陣列中的位置
    internal var sparse = IntArray(initialCapacity) { -1 }

    internal var size = 0
        private set

    /**
     * 高性能遍歷：避免產生中間陣列
     * 用法：storage.forEach { entityId, component -> ... }
     */
    fun forEach(action: (Int, T) -> Unit) {
        for (i in 0 until size) {
            action(dense[i], components[i] as T)
        }
    }

    /**
     * 獲取原始密集陣列 (System 內部優化用)
     * 注意：僅應訪問 0 until size 的區間
     */
    fun getRawDense(): IntArray = dense

    fun has(entityId: Int): Boolean {
        // 快速過濾越界 ID
        if (entityId < 0 || entityId >= sparse.size) return false
        val index = sparse[entityId]
        // Sparse Set 核心：檢查稀疏值是否在有效區間，且密集陣列反向指向該實體
        return index in 0 until size && dense[index] == entityId
    }

    fun add(entityId: Int, component: T) {
        ensureSparseCapacity(entityId)
        val index = sparse[entityId]

        // 如果實體尚未擁有此組件 (或已被移除)
        if (index !in 0 until size || dense[index] != entityId) {
            ensureDenseCapacity(size)

            // 寫入新組件至末尾
            sparse[entityId] = size
            dense[size] = entityId
            components[size] = component
            size++
        } else {
            // 已存在則直接更新數據
            components[index] = component
        }
    }

    fun get(entityId: Int): T? {
        if (entityId < 0 || entityId >= sparse.size) return null
        val index = sparse[entityId]
        if (index in 0 until size && dense[index] == entityId) {
            return components[index] as T?
        }
        return null
    }

    fun remove(entityId: Int) {
        if (!has(entityId)) return

        val indexToRemove = sparse[entityId]
        val lastIndex = size - 1
        val lastEntityId = dense[lastIndex]

        // Swap and Pop (精簡版)：將最後一個元素搬到被刪除的位置
        components[indexToRemove] = components[lastIndex]
        dense[indexToRemove] = lastEntityId
        sparse[lastEntityId] = indexToRemove

        // 徹底清理殘留引用，防止記憶體洩漏 (重要！)
        sparse[entityId] = -1
        components[lastIndex] = null
        size--
    }

    private fun ensureSparseCapacity(id: Int) {
        if (id >= sparse.size) {
            // 稀疏陣列不需要頻繁倍增，採 +128 或倍數增長
            val newSize = (id + 1).coerceAtLeast(sparse.size * 2)
            val oldSparse = sparse
            sparse = IntArray(newSize) { -1 }
            oldSparse.copyInto(sparse)
        }
    }

    private fun ensureDenseCapacity(targetSize: Int) {
        if (targetSize >= components.size) {
            val newSize = components.size * 2
            components = components.copyOf(newSize)
            dense = dense.copyOf(newSize)
        }
    }

    fun clear() {
        // 僅清理密集陣列中的實體索引，Sparse 會在下次 add 時覆寫
        for (i in 0 until size) {
            sparse[dense[i]] = -1
            components[i] = null
        }
        size = 0
    }
}