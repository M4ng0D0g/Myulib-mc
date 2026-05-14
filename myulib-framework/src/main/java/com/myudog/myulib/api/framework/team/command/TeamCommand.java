package com.myudog.myulib.api.framework.team.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.myudog.myulib.api.framework.command.AccessCommandService;
import com.myudog.myulib.api.framework.team.TeamColor;
import com.myudog.myulib.api.framework.team.TeamDefinition;
import com.myudog.myulib.api.framework.team.TeamManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.Map;
import java.util.UUID;

public final class TeamCommand {
    private TeamCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(AccessCommandService.COMMAND_PREFIX + "team")
                .requires(source -> source.permissions().hasPermission(AccessCommandService.gamemasterPermission()))
                .then(Commands.literal("create")
                        .then(Commands.argument("id", StringArgumentType.word())
                                .then(Commands.argument("color", StringArgumentType.word())
                                        .executes(context -> {
                                            String teamToken = StringArgumentType.getString(context, "id");
                                            Identifier id = AccessCommandService.toScopedMyulibIdentifier(TeamDefinition.ROUTE, teamToken);
                                            TeamColor color = AccessCommandService.parseTeamColor(StringArgumentType.getString(context, "color"));
                                            TeamManager.INSTANCE.register(new TeamDefinition(teamToken, Component.literal(id.toString()), color, Map.of()));
                                            return AccessCommandService.reply(context.getSource(), "team=create:" + id);
                                        }))))
                .then(Commands.literal("read")
                        .then(Commands.argument("id", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.teamIdSuggestions(), builder))
                                .executes(context -> {
                                    UUID id = AccessCommandService.resolveTeamIdToken(StringArgumentType.getString(context, "id"));
                                    TeamDefinition team = TeamManager.INSTANCE.get(id);
                                    if (team == null) {
                                        return AccessCommandService.reply(context.getSource(), "team=not_found");
                                    }
                                    return AccessCommandService.reply(context.getSource(), "team=id:" + team.id() + ",color:" + team.color());
                                })))
                .then(Commands.literal("update")
                        .then(Commands.argument("id", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.teamIdSuggestions(), builder))
                                .then(Commands.argument("color", StringArgumentType.word())
                                        .executes(context -> {
                                            UUID id = AccessCommandService.resolveTeamIdToken(StringArgumentType.getString(context, "id"));
                                            if (TeamManager.INSTANCE.get(id) == null) {
                                                return AccessCommandService.reply(context.getSource(), "team=not_found");
                                            }
                                            TeamColor color = AccessCommandService.parseTeamColor(StringArgumentType.getString(context, "color"));
                                            TeamManager.INSTANCE.update(id, old -> new TeamDefinition(old.uuid().toString(), old.translationKey(), color, old.flags(), old.playerLimit()));
                                            return AccessCommandService.reply(context.getSource(), "team=updated:" + id);
                                        }))))
                .then(Commands.literal("delete")
                        .then(Commands.argument("id", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.teamIdSuggestions(), builder))
                                .executes(context -> {
                                    UUID id = AccessCommandService.resolveTeamIdToken(StringArgumentType.getString(context, "id"));
                                    if (TeamManager.INSTANCE.get(id) == null) {
                                        return AccessCommandService.reply(context.getSource(), "team=not_found");
                                    }
                                    TeamManager.INSTANCE.unregister(id);
                                    return AccessCommandService.reply(context.getSource(), "team=deleted:" + id);
                                })))
                .then(Commands.literal("list")
                        .executes(context -> AccessCommandService.reply(context.getSource(), "team=count:" + TeamManager.INSTANCE.all().size()))));
    }
}

