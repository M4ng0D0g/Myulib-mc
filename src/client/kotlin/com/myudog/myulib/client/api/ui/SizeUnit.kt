package com.myudog.myulib.client.api.ui

import com.myudog.myulib.api.ecs.Component


/**
 * 尺寸單位：支援固定像素、父容器百分比、填滿或包裹內容
 */
sealed interface SizeUnit : Component {
    data class Fixed(val px: Float) : SizeUnit
    data class Relative(val percent: Float) : SizeUnit
    object FillContainer : SizeUnit // 填滿父容器
    object WrapContent : SizeUnit   // 根據子元件縮放
}