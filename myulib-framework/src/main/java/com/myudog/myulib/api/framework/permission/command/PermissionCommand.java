package com.myudog.myulib.api.framework.permission.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.myudog.myulib.MyulibFramework;
import com.myudog.myulib.api.framework.command.AccessCommandService;
import com.myudog.myulib.api.framework.field.FieldDefinition;
import com.myudog.myulib.api.framework.field.FieldManager;
import com.myudog.myulib.api.framework.permission.PermissionAction;
import com.myudog.myulib.api.framework.permission.PermissionDecision;
import com.myudog.myulib.api.framework.permission.PermissionManager;
import com.myudog.myulib.api.framework.permission.ScopeLayer;
import com.myudog.myulib.api.framework.rolegroup.RoleGroupManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public final class PermissionCommand {
    private PermissionCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        var root = Commands.literal(AccessCommandService.COMMAND_PREFIX + "permission")
                .requires(source -> source.permissions().hasPermission(AccessCommandService.gamemasterPermission()));

        root.then(Commands.literal("create")
                .then(Commands.argument("group", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.permissionGroupSuggestions(), builder))
                        .then(AccessCommandService.permissionActionArgument()
                                .then(AccessCommandService.permissionDecisionArgument()
                                        .executes(context -> {
                                            String group = AccessCommandService.normalizeGroupToken(StringArgumentType.getString(context, "group"));
                                            PermissionAction action = AccessCommandService.parseAction(StringArgumentType.getString(context, "action"));
                                            PermissionDecision decision = AccessCommandService.parseDecision(StringArgumentType.getString(context, "decision"));
                                            AccessCommandService.grantGlobalPermission(group, action, decision);
                                            return AccessCommandService.reply(context.getSource(), "permission=create:global," + group + "," + action + "=" + decision);
                                        })))));

        root.then(Commands.literal("set")
                .then(Commands.literal("global")
                        .then(Commands.argument("group", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.permissionGroupSuggestions(), builder))
                                .then(AccessCommandService.permissionActionArgument()
                                        .then(AccessCommandService.permissionDecisionArgument()
                                                .executes(context -> {
                                                    String group = AccessCommandService.normalizeGroupToken(StringArgumentType.getString(context, "group"));
                                                    PermissionAction action = AccessCommandService.parseAction(StringArgumentType.getString(context, "action"));
                                                    PermissionDecision decision = AccessCommandService.parseDecision(StringArgumentType.getString(context, "decision"));
                                                    AccessCommandService.setScopedGroupPermission(ScopeLayer.GLOBAL, Identifier.fromNamespaceAndPath(MyulibFramework.MOD_ID, "global"), group, action, decision);
                                                    return AccessCommandService.reply(context.getSource(), "permission=set:global," + group + "," + action + "=" + decision);
                                                }))))
                .then(Commands.argument("scope", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.scopeNamesForScopedSet(), builder))
                        .then(Commands.argument("scopeId", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.scopeIdSuggestions(StringArgumentType.getString(context, "scope")), builder))
                                .then(Commands.argument("group", StringArgumentType.word())
                                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.permissionGroupSuggestions(), builder))
                                        .then(AccessCommandService.permissionActionArgument()
                                                .then(AccessCommandService.permissionDecisionArgument()
                                                        .executes(context -> {
                                                            ScopeLayer scope = AccessCommandService.parseScope(StringArgumentType.getString(context, "scope"));
                                                            Identifier scopeId = AccessCommandService.resolveScopeId(scope, StringArgumentType.getString(context, "scopeId"));
                                                            String group = AccessCommandService.normalizeGroupToken(StringArgumentType.getString(context, "group"));
                                                            PermissionAction action = AccessCommandService.parseAction(StringArgumentType.getString(context, "action"));
                                                            PermissionDecision decision = AccessCommandService.parseDecision(StringArgumentType.getString(context, "decision"));
                                                            AccessCommandService.setScopedGroupPermission(scope, scopeId, group, action, decision);
                                                            return AccessCommandService.reply(context.getSource(), "permission=set:" + scope.name().toLowerCase() + "," + group + "," + action + "=" + decision);
                                                        }))))))));

        root.then(Commands.literal("read")
                .then(Commands.argument("group", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.permissionGroupSuggestions(), builder))
                        .then(AccessCommandService.permissionActionArgument()
                                .executes(context -> {
                                    String group = AccessCommandService.normalizeGroupToken(StringArgumentType.getString(context, "group"));
                                    PermissionAction action = AccessCommandService.parseAction(StringArgumentType.getString(context, "action"));
                                    PermissionDecision decision = PermissionManager.INSTANCE.global().forGroup(group).get(action);
                                    return AccessCommandService.reply(context.getSource(), "permission=read:global," + group + "," + action + "=" + decision);
                                }))));

        root.then(Commands.literal("player")
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> {
                            var player = EntityArgument.getPlayer(context, "player");
                            var groups = RoleGroupManager.INSTANCE.getSortedGroupIdsOf(player.getUUID());
                            Identifier dimId = player.level().dimension().identifier();
                            FieldDefinition field = FieldManager.INSTANCE.findAt(dimId, player.position()).orElse(null);
                            Identifier fieldId = field == null ? null : Identifier.fromNamespaceAndPath(MyulibFramework.MOD_ID, FieldDefinition.ROUTE + "/" + field.id());
                            return AccessCommandService.reply(context.getSource(), AccessCommandService.renderFlattenedPlayerPermissions(player.getName().getString(), player.getUUID(), groups, fieldId, dimId));
                        })));

        root.then(Commands.literal("list")
                .then(Commands.argument("group", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.permissionGroupSuggestions(), builder))
                        .executes(context -> {
                            String group = AccessCommandService.normalizeGroupToken(StringArgumentType.getString(context, "group"));
                            return AccessCommandService.reply(context.getSource(), AccessCommandService.renderPermissionGroupList(group, ScopeLayer.GLOBAL, null, "scope"));
                        })
                        .then(Commands.argument("scope", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.scopeNames(), builder))
                                .then(Commands.argument("scopeId", StringArgumentType.word())
                                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.scopeIdSuggestions(StringArgumentType.getString(context, "scope")), builder))
                                        .then(Commands.argument("mode", StringArgumentType.word())
                                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(List.of("scope", "merged"), builder))
                                                .executes(context -> {
                                                    String group = AccessCommandService.normalizeGroupToken(StringArgumentType.getString(context, "group"));
                                                    ScopeLayer scope = AccessCommandService.parseScope(StringArgumentType.getString(context, "scope"));
                                                    Identifier scopeId = AccessCommandService.resolveScopeId(scope, StringArgumentType.getString(context, "scopeId"));
                                                    String mode = StringArgumentType.getString(context, "mode").toLowerCase();
                                                    return AccessCommandService.reply(context.getSource(), AccessCommandService.renderPermissionGroupList(group, scope, scopeId, mode));
                                                }))))));

        root.then(Commands.literal("delete")
                .then(Commands.argument("group", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.permissionGroupSuggestions(), builder))
                        .then(AccessCommandService.permissionActionArgument()
                                .executes(context -> {
                                    String group = AccessCommandService.normalizeGroupToken(StringArgumentType.getString(context, "group"));
                                    PermissionAction action = AccessCommandService.parseAction(StringArgumentType.getString(context, "action"));
                                    AccessCommandService.grantGlobalPermission(group, action, PermissionDecision.UNSET);
                                    return AccessCommandService.reply(context.getSource(), "permission=deleted:global," + group + "," + action);
                                }))));

        dispatcher.register(root);
    }
}

