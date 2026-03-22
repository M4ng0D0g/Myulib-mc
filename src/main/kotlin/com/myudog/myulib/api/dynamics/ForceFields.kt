package com.myudog.myulib.api.dynamics

import com.myudog.myulib.internal.dynamics.RadialForceField

object ForceFields {
    /** 預設吸引力 (範圍 10) */
    val ATTRACTION: IForceField = RadialForceField(true)

    /** 預設排斥力 (範圍 10) */
    val REPULSION: IForceField = RadialForceField(false)

    /** 建立具備特定範圍的引力場 */
    fun attraction(range: Double): IForceField = RadialForceField(true, range)

    /** 建立具備特定範圍的排斥場 */
    fun repulsion(range: Double): IForceField = RadialForceField(false, range)

    // ... 其他如 VORTEX 保持不變
}