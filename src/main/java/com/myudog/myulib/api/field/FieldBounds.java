package com.myudog.myulib.api.field;

public record FieldBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
    public FieldBounds {
        if (maxX < minX || maxY < minY || maxZ < minZ) {
            throw new IllegalArgumentException("Invalid bounds");
        }
    }

    public boolean contains(double x, double y, double z) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
    }

    public boolean intersects(FieldBounds other) {
        return other != null && maxX >= other.minX && other.maxX >= minX && maxY >= other.minY && other.maxY >= minY && maxZ >= other.minZ && other.maxZ >= minZ;
    }
}

