package com.myudog.myulib.api.field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;

public final class FieldManager {
    private static final Map<String, FieldDefinition> FIELDS = new LinkedHashMap<>();

    private FieldManager() {
    }

    public static void install() {
    }

    public static FieldDefinition register(FieldDefinition field) {
        Objects.requireNonNull(field, "field");
        FIELDS.put(field.id(), field);
        return field;
    }

    public static FieldDefinition update(String fieldId, UnaryOperator<FieldDefinition> updater) {
        Objects.requireNonNull(fieldId, "fieldId");
        Objects.requireNonNull(updater, "updater");
        FieldDefinition existing = FIELDS.get(fieldId);
        if (existing == null) {
            return null;
        }
        FieldDefinition updated = Objects.requireNonNull(updater.apply(existing), "updated field");
        FIELDS.put(fieldId, updated);
        return updated;
    }

    public static FieldDefinition unregister(String fieldId) {
        return FIELDS.remove(fieldId);
    }

    public static FieldDefinition get(String fieldId) {
        return FIELDS.get(fieldId);
    }

    public static List<FieldDefinition> all() {
        return List.copyOf(FIELDS.values());
    }

    public static Map<String, FieldDefinition> snapshot() {
        return Map.copyOf(FIELDS);
    }

    public static List<FieldDefinition> findAt(String dimensionId, double x, double y, double z) {
        List<FieldDefinition> result = new ArrayList<>();
        for (FieldDefinition field : FIELDS.values()) {
            if ((dimensionId == null || dimensionId.equals(field.dimensionId())) && field.bounds().contains(x, y, z)) {
                result.add(field);
            }
        }
        return Collections.unmodifiableList(result);
    }

    public static void clear() {
        FIELDS.clear();
    }
}


