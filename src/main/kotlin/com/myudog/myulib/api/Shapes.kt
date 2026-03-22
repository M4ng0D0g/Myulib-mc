package com.myudog.myulib.api

import com.myudog.myulib.api.shape.IShape
import com.myudog.myulib.internal.generator.*

object Shapes {
    val CIRCLE: IShape = CircleGenerator()
    val CUBE: IShape = CubeGenerator()
    val SPHERE: IShape = SphereGenerator()
    val CYLINDER: IShape = CylinderGenerator()
    val CONE: IShape = ConeGenerator()
    val TORUS: IShape = TorusGenerator()

    // 你還可以擴充如：
    // val PYRAMID: IShape = PyramidGenerator()
}