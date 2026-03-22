package com.myudog.myulib.client.gui.enums

import com.myudog.myulib.client.gui.widgets.base.NineSliceRenderer

/** Minimal panel style used by UI code. */
class PanelStyle(
    val singleTexture: NineSliceRenderer.SingleTextureNineSlice? = null,
    val scaleMode: ScaleMode = ScaleMode.STRETCH
) {
    enum class ScaleMode { STRETCH, FIT }
}

