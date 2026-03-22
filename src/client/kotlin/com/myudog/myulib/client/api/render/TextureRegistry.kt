package com.myudog.myulib.client.api.render

import com.myudog.myulib.client.gui.widgets.base.NineSliceRenderer.SingleTextureNineSlice
import com.myudog.myulib.client.gui.enums.PanelStyle.ScaleMode
import net.minecraft.util.Identifier
import java.util.concurrent.ConcurrentHashMap

/**
 * 材質類型枚舉
 */
enum class TextureType { SIMPLE, THREE_SLICE_H, THREE_SLICE_V, NINE_SLICE }

/**
 * 材質元數據：存儲貼圖路徑、原始尺寸與切片資訊
 */
data class TextureMetadata(
    val id: Identifier,
    val width: Int,
    val height: Int,
    val cornerW: Int = 0,
    val cornerH: Int = 0,
    val type: TextureType = TextureType.SIMPLE,
    val scaleMode: ScaleMode = ScaleMode.STRETCH
)

object TextureRegistry {
    private val registry = ConcurrentHashMap<String, TextureMetadata>()

    // ── 註冊方法 ────────────────────────────────────────────────────────────

    /** 註冊普通圖片 */
    fun registerSimple(key: String, id: Identifier, w: Int, h: Int) {
        registry[key] = TextureMetadata(id, w, h, type = TextureType.SIMPLE)
    }

    /** 註冊 9-Slice 材質 (需輸入角落尺寸) */
    fun registerNineSlice(key: String, id: Identifier, w: Int, h: Int, cW: Int, cH: Int) {
        registry[key] = TextureMetadata(id, w, h, cW, cH, TextureType.NINE_SLICE)
    }

    /** 註冊 3-Slice 材質 (水平或垂直) */
    fun registerThreeSlice(key: String, id: Identifier, w: Int, h: Int, cap: Int, vertical: Boolean) {
        val type = if (vertical) TextureType.THREE_SLICE_V else TextureType.THREE_SLICE_H
        registry[key] = TextureMetadata(id, w, h, cap, cap, type)
    }

    // ── 取用方法 ────────────────────────────────────────────────────────────

    fun get(key: String): TextureMetadata? = registry[key]

    /** 將 Metadata 轉換為你舊有的 SingleTextureNineSlice 格式以相容渲染器 */
    fun toNineSlice(key: String): SingleTextureNineSlice? {
        val meta = registry[key] ?: return null
        return SingleTextureNineSlice.of(
            meta.id.namespace, meta.id.path,
            meta.width, meta.height, meta.cornerW, meta.cornerH, meta.scaleMode
        )
    }
}