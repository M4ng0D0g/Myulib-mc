package com.myudog.myulib.api.ecs

import com.myudog.myulib.internal.ecs.ComponentStorage
import com.myudog.myulib.internal.event.EventDispatcherImpl
import com.myudog.myulib.api.ecs.event.ComponentAddedEvent
import com.myudog.myulib.api.ecs.lifecycle.DimensionAware
import com.myudog.myulib.api.ecs.lifecycle.DimensionChangePolicy
import com.myudog.myulib.api.ecs.lifecycle.Resettable
import kotlin.reflect.KClass

class EcsWorld {
    /**
     * [核心擴充] 每個 World 實例擁有自己專屬的 Event Bus。
     */
    val eventBus = EventDispatcherImpl()

    private var nextEntityId = 0
    // 核心倉庫：所有的組件都存放在這裡
    private val storages = mutableMapOf<KClass<out Component>, ComponentStorage<*>>()

    /**
     * 建立新的實體 ID
     */
    fun createEntity(): Int = nextEntityId++

    /**
     * [Internal] 取得特定類型的倉庫，若不存在則建立。
     */
    fun <T : Component> getStorage(type: KClass<T>): ComponentStorage<T> {
        @Suppress("UNCHECKED_CAST")
        return storages.getOrPut(type) { ComponentStorage<T>() } as ComponentStorage<T>
    }

    /**
     * [Public API] 查詢所有擁有特定組件的實體 ID
     */
    fun query(type: KClass<out Component>): List<Int> {
        val storage = storages[type] ?: return emptyList()
        return storage.getRawDense().slice(0 until storage.size)
    }

    /**
     * [Public API] 刪除實體及其所有組件
     */
    fun destroyEntity(entityId: Int) {
        storages.values.forEach { storage ->
            storage.remove(entityId)
        }
    }

    /**
     * [生命週期] 軟重置實體。
     */
    fun resetEntity(entityId: Int) {
        storages.values.forEach { storage ->
            val component = storage.get(entityId)
            if (component is Resettable) {
                component.reset()
            }
        }
    }

    /**
     * [生命週期] 處理實體切換維度。
     */
    fun processDimensionChange(entityId: Int) {
        storages.values.forEach { storage ->
            val component = storage.get(entityId)
            if (component is DimensionAware) {
                when (component.dimensionPolicy) {
                    DimensionChangePolicy.REMOVE -> storage.remove(entityId)
                    DimensionChangePolicy.RESET -> if (component is Resettable) component.reset()
                    DimensionChangePolicy.KEEP -> {}
                }
            }
        }
    }

    // --- 快速存取方法 ---

    /**
     * 為實體添加組件，並派發 ComponentAddedEvent 事件。
     */
    inline fun <reified T : Component> addComponent(entityId: Int, component: T) {
        getStorage(T::class).add(entityId, component)
        eventBus.dispatch(ComponentAddedEvent(entityId, component))
    }

    /**
     * 核心查詢方法 (Class 類型版本，供 Java 或內部反射使用)
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Component> getComponent(entityId: Int, type: KClass<T>): T? {
        val storage = storages[type] as? ComponentStorage<T>
        return storage?.get(entityId)
    }

    /**
     * 💡 修正關鍵：支援 reified 泛型的擴充函數。
     * 這是你 InputSystem 報錯的主因，現在它會正確轉發到對應的 KClass。
     */
    inline fun <reified T : Component> getComponent(entityId: Int): T? {
        return getComponent(entityId, T::class)
    }

    /**
     * 檢查是否擁有組件
     */
    inline fun <reified T : Component> hasComponent(entityId: Int): Boolean {
        return getComponent<T>(entityId) != null
    }
}