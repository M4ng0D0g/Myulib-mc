package com.myudog.myulib.api.ecs.lifecycle

import com.myudog.myulib.api.ecs.Component

/**
 * 特徵 1：可重置的組件 (Resettable)
 * 適用場景：實體死亡後準備重生、或從物件池 (Object Pool) 拿出來重複使用時。
 * 範例：HealthComponent (血量回滿)、ManaComponent (魔力歸零)。
 */
interface Resettable : Component {
    /**
     * 當實體需要被「軟重置」時呼叫，而不是整顆 Component 被拔除。
     */
    fun reset()
}

/**
 * 定義跨越維度時的處理策略。
 */
enum class DimensionChangePolicy {
    /** 保留原本的狀態 (預設值)。適用：背包、等級、經驗值。 */
    KEEP,

    /** 徹底移除該組件。適用：尋路目標、當前維度的特定狀態 (如：著火、座標)。 */
    REMOVE,

    /** 保留組件，但觸發重置。必須同時實作 [Resettable] 介面才有效。適用：跨維度時重新計算環境適應力。 */
    RESET
}

/**
 * 特徵 2：具備維度感知能力的組件 (Dimension Aware)
 * 適用場景：需要根據維度切換改變行為的組件。若沒實作此介面，預設行為就是 KEEP。
 */
interface DimensionAware : Component {
    val dimensionPolicy: DimensionChangePolicy
        get() = DimensionChangePolicy.KEEP // 預設策略
}