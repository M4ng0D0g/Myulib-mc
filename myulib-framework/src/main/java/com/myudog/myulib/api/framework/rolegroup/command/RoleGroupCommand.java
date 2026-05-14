package com.myudog.myulib.api.framework.rolegroup.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.myudog.myulib.api.framework.command.AccessCommandService;
import com.myudog.myulib.api.framework.rolegroup.RoleGroupDefinition;
import com.myudog.myulib.api.framework.rolegroup.RoleGroupManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.UUID;

public final class RoleGroupCommand {
    private RoleGroupCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(AccessCommandService.COMMAND_PREFIX + "rolegroup")
                .requires(source -> source.permissions().hasPermission(AccessCommandService.gamemasterPermission()))
                .then(Commands.literal("create")
                        .then(Commands.argument("id", StringArgumentType.word())
                                .then(Commands.argument("priority", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            String groupToken = StringArgumentType.getString(context, "id");
                                            UUID id = AccessCommandService.resolveRoleGroupIdToken(groupToken);
                                            int priority = IntegerArgumentType.getInteger(context, "priority");
                                            AccessCommandService.createRoleGroup(id, Component.literal(id.toString()), priority);
                                            return AccessCommandService.reply(context.getSource(), "rolegroup=create:" + id);
                                        }))))
                .then(Commands.literal("read")
                        .then(Commands.argument("id", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.rolegroupIdSuggestions(), builder))
                                .executes(context -> {
                                    UUID id = AccessCommandService.resolveRoleGroupIdToken(StringArgumentType.getString(context, "id"));
                                    RoleGroupDefinition group = RoleGroupManager.INSTANCE.get(id);
                                    if (group == null) {
                                        return AccessCommandService.reply(context.getSource(), "rolegroup=not_found");
                                    }
                                    return AccessCommandService.reply(context.getSource(), "rolegroup=id:" + group.id() + ",priority:" + group.priority());
                                })))
                .then(Commands.literal("update")
                        .then(Commands.argument("id", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.rolegroupIdSuggestions(), builder))
                                .then(Commands.argument("priority", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            UUID id = AccessCommandService.resolveRoleGroupIdToken(StringArgumentType.getString(context, "id"));
                                            if (RoleGroupManager.INSTANCE.get(id) == null) {
                                                return AccessCommandService.reply(context.getSource(), "rolegroup=not_found");
                                            }
                                            int priority = IntegerArgumentType.getInteger(context, "priority");
                                            RoleGroupManager.INSTANCE.update(id, old -> new RoleGroupDefinition(old.uuid(), old.translationKey(), priority, old.metadata(), old.members()));
                                            return AccessCommandService.reply(context.getSource(), "rolegroup=updated:" + id);
                                        }))))
                .then(Commands.literal("delete")
                        .then(Commands.argument("id", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.rolegroupIdSuggestions(), builder))
                                .executes(context -> {
                                    UUID id = AccessCommandService.resolveRoleGroupIdToken(StringArgumentType.getString(context, "id"));
                                    if (RoleGroupManager.INSTANCE.get(id) == null) {
                                        return AccessCommandService.reply(context.getSource(), "rolegroup=not_found");
                                    }
                                    AccessCommandService.deleteRoleGroup(id);
                                    return AccessCommandService.reply(context.getSource(), "rolegroup=deleted:" + id);
                                })))
                .then(Commands.literal("assign")
                        .then(Commands.argument("id", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.rolegroupIdSuggestions(), builder))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context -> {
                                            UUID id = AccessCommandService.resolveRoleGroupIdToken(StringArgumentType.getString(context, "id"));
                                            if (RoleGroupManager.INSTANCE.get(id) == null) {
                                                return AccessCommandService.reply(context.getSource(), "rolegroup=not_found");
                                            }
                                            var target = EntityArgument.getPlayer(context, "player");
                                            boolean added = RoleGroupManager.INSTANCE.assign(target.getUUID(), id);
                                            return AccessCommandService.reply(context.getSource(), added
                                                    ? "rolegroup=assigned:" + id + "->" + target.getName().getString()
                                                    : "rolegroup=already_assigned");
                                        }))))
                .then(Commands.literal("revoke")
                        .then(Commands.argument("id", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.rolegroupIdSuggestions(), builder))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context -> {
                                            UUID id = AccessCommandService.resolveRoleGroupIdToken(StringArgumentType.getString(context, "id"));
                                            if (RoleGroupManager.INSTANCE.get(id) == null) {
                                                return AccessCommandService.reply(context.getSource(), "rolegroup=not_found");
                                            }
                                            var target = EntityArgument.getPlayer(context, "player");
                                            boolean removed = RoleGroupManager.INSTANCE.revoke(target.getUUID(), id);
                                            return AccessCommandService.reply(context.getSource(), removed
                                                    ? "rolegroup=revoked:" + id + "->" + target.getName().getString()
                                                    : "rolegroup=not_assigned");
                                        }))))
                .then(Commands.literal("groups-of")
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(context -> {
                                    var target = EntityArgument.getPlayer(context, "player");
                                    List<String> groups = RoleGroupManager.INSTANCE.getSortedGroupIdsOf(target.getUUID());
                                    return AccessCommandService.reply(context.getSource(), "rolegroup=groups_of:" + target.getName().getString() + "=" + String.join("|", groups));
                                })))
                .then(Commands.literal("list")
                        .executes(context -> AccessCommandService.reply(context.getSource(), "rolegroup=count:" + RoleGroupManager.INSTANCE.groups().size()))));
    }
}

