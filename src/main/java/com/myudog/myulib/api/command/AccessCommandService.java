package com.myudog.myulib.api.command;

import com.myudog.myulib.api.field.FieldAdminService;
import com.myudog.myulib.api.field.FieldDefinition;
import com.myudog.myulib.api.identity.IdentityAdminService;
import com.myudog.myulib.api.identity.IdentityGroupDefinition;
import com.myudog.myulib.api.permission.PermissionAdminService;
import com.myudog.myulib.api.permission.PermissionContext;
import com.myudog.myulib.api.permission.PermissionGrant;
import com.myudog.myulib.api.permission.PermissionLayer;
import com.myudog.myulib.api.permission.PermissionResolution;
import com.myudog.myulib.api.team.TeamAdminService;
import com.myudog.myulib.api.team.TeamDefinition;
import com.myudog.myulib.api.ui.ConfigurationUiRegistry;
import com.myudog.myulib.api.ui.ConfigurationUiBridge;
import java.util.UUID;
import java.util.function.UnaryOperator;

public final class AccessCommandService {
    private AccessCommandService() {
    }

    public static CommandResult createField(FieldDefinition field) {
        FieldAdminService.create(field);
        return CommandResult.success("Field created: " + field.id());
    }

    public static CommandResult updateField(String fieldId, UnaryOperator<FieldDefinition> updater) {
        return FieldAdminService.update(fieldId, updater) != null
            ? CommandResult.success("Field updated: " + fieldId)
            : CommandResult.failure("Missing field: " + fieldId);
    }

    public static CommandResult deleteField(String fieldId) {
        return FieldAdminService.delete(fieldId) != null
            ? CommandResult.success("Field deleted: " + fieldId)
            : CommandResult.failure("Missing field: " + fieldId);
    }

    public static CommandResult openFieldEditor(String fieldId, ConfigurationUiBridge ui) {
        FieldAdminService.openEditor(fieldId, ui);
        return CommandResult.success("Open field editor: " + fieldId);
    }

    public static CommandResult createIdentityGroup(IdentityGroupDefinition group) {
        IdentityAdminService.create(group);
        return CommandResult.success("Identity group created: " + group.id());
    }

    public static CommandResult updateIdentityGroup(String groupId, UnaryOperator<IdentityGroupDefinition> updater) {
        return IdentityAdminService.update(groupId, updater) != null
            ? CommandResult.success("Identity group updated: " + groupId)
            : CommandResult.failure("Missing identity group: " + groupId);
    }

    public static CommandResult deleteIdentityGroup(String groupId) {
        return IdentityAdminService.delete(groupId) != null
            ? CommandResult.success("Identity group deleted: " + groupId)
            : CommandResult.failure("Missing identity group: " + groupId);
    }

    public static CommandResult openIdentityGroupEditor(String groupId, ConfigurationUiBridge ui) {
        IdentityAdminService.openEditor(groupId, ui);
        return CommandResult.success("Open identity editor: " + groupId);
    }

    public static CommandResult createTeam(TeamDefinition team) {
        TeamAdminService.create(team);
        return CommandResult.success("Team created: " + team.id());
    }

    public static CommandResult updateTeam(String teamId, UnaryOperator<TeamDefinition> updater) {
        return TeamAdminService.update(teamId, updater) != null
            ? CommandResult.success("Team updated: " + teamId)
            : CommandResult.failure("Missing team: " + teamId);
    }

    public static CommandResult deleteTeam(String teamId) {
        return TeamAdminService.delete(teamId) != null
            ? CommandResult.success("Team deleted: " + teamId)
            : CommandResult.failure("Missing team: " + teamId);
    }

    public static CommandResult openTeamEditor(String teamId, ConfigurationUiBridge ui) {
        TeamAdminService.openEditor(teamId, ui);
        return CommandResult.success("Open team editor: " + teamId);
    }

    public static CommandResult grantPermission(PermissionLayer layer, String scopeId, PermissionGrant grant, UUID playerId) {
        switch (layer) {
            case GLOBAL -> PermissionAdminService.grantGlobal(grant);
            case DIMENSION -> PermissionAdminService.grantDimension(scopeId, grant);
            case FIELD -> PermissionAdminService.grantField(scopeId, grant);
            case USER -> PermissionAdminService.grantUser(playerId, grant);
        }
        return CommandResult.success("Permission granted: " + grant.id());
    }

    public static CommandResult evaluatePermission(PermissionContext context) {
        PermissionResolution resolution = PermissionAdminService.evaluate(context);
        return CommandResult.success(resolution.decision().name());
    }

    public static void registerDefaults() {
        CommandRegistry.register("field.open", ctx -> openFieldEditor(ctx.arguments().getOrDefault("id", ""), ConfigurationUiRegistry.bridge()));
        CommandRegistry.register("identity.open", ctx -> openIdentityGroupEditor(ctx.arguments().getOrDefault("id", ""), ConfigurationUiRegistry.bridge()));
        CommandRegistry.register("team.open", ctx -> openTeamEditor(ctx.arguments().getOrDefault("id", ""), ConfigurationUiRegistry.bridge()));
    }
}

