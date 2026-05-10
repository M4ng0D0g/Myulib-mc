package com.myudog.myulib.api.core.ui;

import com.myudog.myulib.api.framework.permission.ScopeLayer;
import net.minecraft.resources.Identifier;

public enum NoopConfigurationUiBridge implements ConfigurationUiBridge {
    INSTANCE;

    @Override
    public void openFieldEditor(Identifier fieldId) {
    }

    @Override
    public void openIdentityGroupEditor(Identifier groupId) {
    }

    @Override
    public void openRoleGroupEditor(Identifier groupId) {
    }

    @Override
    public void openTeamEditor(Identifier teamId) {
    }

    @Override
    public void openPermissionEditor(ScopeLayer layer, String scopeId) {
    }
}
