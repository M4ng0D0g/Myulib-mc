package com.myudog.myulib.client.api

import com.myudog.myulib.client.MyulibClient
import com.myudog.myulib.api.ecs.Component
import com.myudog.myulib.api.ecs.EcsWorld
import com.myudog.myulib.client.internal.ui.system.LayoutSystem
import com.myudog.myulib.client.internal.ui.system.RenderSystem
import net.minecraft.client.gui.DrawContext

/**
 * [API] MyuLib 客戶端入口。
 * 封裝了對底層 ECS 世界的操作，提供更簡潔的 UI 開發介面。
 */
object MyulibApiClient {

    // 佈局髒標記：控制 LayoutSystem 是否需要重新計算
    private var isLayoutDirty = true

    /**
     * 每幀渲染驅動心臟。
     * @param screenW 螢幕像素寬度
     * @param screenH 螢幕像素高度
     */
    fun onRenderTick(context: DrawContext, screenW: Int, screenH: Int, mouseX: Double, mouseY: Double, delta: Float) {
        val world = MyulibClient.internalWorld

        // 1. 執行佈局計算 (只有髒掉時才重新算，節省效能)
        if (isLayoutDirty) {
            // 這裡傳入 rootId，實務上會從你的 Screen 傳進來
            // LayoutSystem.update(world, rootId, screenW.toFloat(), screenH.toFloat())
            isLayoutDirty = false
        }

        // 2. 執行渲染系統
        RenderSystem.render(world, context, mouseX.toInt(), mouseY.toInt(), delta)
    }

    /**
     * 建立一個新的實體 ID。
     */
    fun createEntity(): Int = MyulibClient.internalWorld.createEntity()

    /**
     * [核心] 為實體添加組件。
     */
    inline fun <reified T : Component> addComponent(entityId: Int, component: T) {
        // internalWorld 在 MyulibClient 中必須是 @PublishedApi
        MyulibClient.internalWorld.addComponent(entityId, component)
    }

    /**
     * [核心] 獲取實體的組件。
     */
    inline fun <reified T : Component> getComponent(entityId: Int): T? {
        return MyulibClient.internalWorld.getComponent<T>(entityId)
    }

    /**
     * [核心] 檢查實體是否擁有特定組件。
     * 效能優化：直接透傳至底層 Storage.has()
     */
    inline fun <reified T : Component> hasComponent(entityId: Int): Boolean {
        // 注意：EcsWorld.getStorage 必須是 @PublishedApi internal
        return MyulibClient.internalWorld.getStorage(T::class).has(entityId)
    }

    /**
     * 刪除實體。
     */
    fun destroyEntity(entityId: Int) {
        MyulibClient.internalWorld.destroyEntity(entityId)
    }

    /**
     * 請求全域重新佈局。
     */
    fun requestLayout() {
        isLayoutDirty = true
    }

    /**
     * 獲取底層世界引用。
     */
    internal fun getInternalWorld(): EcsWorld = MyulibClient.internalWorld

    fun onMouseDown(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val world = MyulibClient.internalWorld
        // 實務上你會有多個 Root (例如不同的 Panel)，這裡需要遍歷所有 Root
        // 假設你目前只有一個 rootId
        // return InputSystem.onMouseDown(world, rootId, mouseX, mouseY, button)
        return false
    }
}