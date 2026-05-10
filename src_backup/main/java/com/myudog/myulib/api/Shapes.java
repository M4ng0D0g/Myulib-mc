package com.myudog.myulib.api;
import com.myudog.myulib.api.core.shape.IShape;
import com.myudog.myulib.internal.generator.CircleGenerator;
import com.myudog.myulib.internal.generator.ConeGenerator;
import com.myudog.myulib.internal.generator.CubeGenerator;
import com.myudog.myulib.internal.generator.CylinderGenerator;
import com.myudog.myulib.internal.generator.SphereGenerator;
import com.myudog.myulib.internal.generator.TorusGenerator;
public final class Shapes {
    public static final IShape CIRCLE = new CircleGenerator();
    public static final IShape CUBE = new CubeGenerator();
    public static final IShape SPHERE = new SphereGenerator();
    public static final IShape CYLINDER = new CylinderGenerator();
    public static final IShape CONE = new ConeGenerator();
    public static final IShape TORUS = new TorusGenerator();
    private Shapes() {
    }
}