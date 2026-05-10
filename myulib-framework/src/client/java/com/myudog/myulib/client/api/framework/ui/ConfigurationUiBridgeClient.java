package com.myudog.myulib.client.api.framework.ui;

import com.myudog.myulib.api.framework.permission.ScopeLayer;
import com.myudog.myulib.api.framework.ui.ConfigurationUiBridge;
import net.minecraft.resources.Identifier;

import java.util.Objects;

public final class ConfigurationUiBridgeClient implements ConfigurationUiBridge {
    private Identifier lastFieldId;
    private Identifier lastIdentityGroupId;
    private Identifier lastRoleGroupId;
    private Identifier lastTeamId;
    private ScopeLayer lastPermissionLayer;
    private String lastPermissionScopeId;

    @Override
    public void openFieldEditor(Identifier fieldId) {
        lastFieldId = fieldId;
    }

    @Override
    public void openIdentityGroupEditor(Identifier groupId) {
        lastIdentityGroupId = groupId;
    }

    @Override
    public void openRoleGroupEditor(Identifier groupId) {
        lastRoleGroupId = groupId;
    }

    @Override
    public void openTeamEditor(Identifier teamId) {
        lastTeamId = teamId;
    }

    @Override
    public void openPermissionEditor(ScopeLayer layer, String scopeId) {
        lastPermissionLayer = layer;
        lastPermissionScopeId = Objects.requireNonNullElse(scopeId, "");
    }

    public Identifier lastFieldId() {
        return lastFieldId;
    }

    public Identifier lastIdentityGroupId() {
        return lastIdentityGroupId;
    }

    public Identifier lastRoleGroupId() {
        return lastRoleGroupId;
    }

    public Identifier lastTeamId() {
        return lastTeamId;
    }

    public ScopeLayer lastPermissionLayer() {
        return lastPermissionLayer;
    }

    public String lastPermissionScopeId() {
        return lastPermissionScopeId;
    }
}
