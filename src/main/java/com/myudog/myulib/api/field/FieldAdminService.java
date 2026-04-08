package com.myudog.myulib.api.field;

import com.myudog.myulib.api.ui.ConfigurationUiBridge;

import java.util.List;
import java.util.function.UnaryOperator;

public final class FieldAdminService {
    private FieldAdminService() {
    }

    public static FieldDefinition create(FieldDefinition field) {
        return FieldManager.register(field);
    }

    public static FieldDefinition delete(String fieldId) {
        return FieldManager.unregister(fieldId);
    }

    public static FieldDefinition update(String fieldId, UnaryOperator<FieldDefinition> updater) {
        return FieldManager.update(fieldId, updater);
    }

    public static FieldDefinition get(String fieldId) {
        return FieldManager.get(fieldId);
    }

    public static List<FieldDefinition> list() {
        return FieldManager.all();
    }

    public static void openEditor(String fieldId, ConfigurationUiBridge ui) {
        if (ui != null) {
            ui.openFieldEditor(fieldId);
        }
    }
}

