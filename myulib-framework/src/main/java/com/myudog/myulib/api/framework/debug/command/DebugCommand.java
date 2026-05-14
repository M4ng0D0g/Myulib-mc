package com.myudog.myulib.api.framework.debug.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.myudog.myulib.api.core.debug.DebugFeature;
import com.myudog.myulib.api.core.debug.DebugLogManager;
import com.myudog.myulib.api.core.debug.DebugTraceManager;
import com.myudog.myulib.api.framework.command.AccessCommandService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.List;

public final class DebugCommand {
    private DebugCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        var root = Commands.literal(AccessCommandService.COMMAND_PREFIX + "debug")
                .requires(source -> source.permissions().hasPermission(AccessCommandService.gamemasterPermission()));

        root.then(Commands.literal("on").executes(context -> {
            if (context.getSource().getPlayer() == null) {
                return AccessCommandService.reply(context.getSource(), "debug=player_only");
            }
            var playerId = context.getSource().getPlayer().getUUID();
            DebugLogManager.INSTANCE.enable(playerId);
            return AccessCommandService.reply(context.getSource(), "debug=on");
        }));

        root.then(Commands.literal("off").executes(context -> {
            if (context.getSource().getPlayer() == null) {
                return AccessCommandService.reply(context.getSource(), "debug=player_only");
            }
            var playerId = context.getSource().getPlayer().getUUID();
            DebugLogManager.INSTANCE.disable(playerId);
            return AccessCommandService.reply(context.getSource(), "debug=off");
        }));

        root.then(Commands.literal("feature")
                .then(Commands.argument("name", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.debugFeatureSuggestions(), builder))
                        .then(Commands.argument("enabled", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(List.of("on", "off"), builder))
                                .executes(context -> {
                                    if (context.getSource().getPlayer() == null) {
                                        return AccessCommandService.reply(context.getSource(), "debug=player_only");
                                    }
                                    var playerId = context.getSource().getPlayer().getUUID();
                                    DebugFeature feature = DebugFeature.parse(StringArgumentType.getString(context, "name"));
                                    boolean enabled = "on".equalsIgnoreCase(StringArgumentType.getString(context, "enabled"));
                                    DebugLogManager.INSTANCE.setFeature(playerId, feature, enabled);
                                    return AccessCommandService.reply(context.getSource(), "debug=feature:" + feature.token() + "=" + (enabled ? "on" : "off"));
                                }))));

        root.then(Commands.literal("all")
                .then(Commands.argument("enabled", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(List.of("on", "off"), builder))
                        .executes(context -> {
                            if (context.getSource().getPlayer() == null) {
                                return AccessCommandService.reply(context.getSource(), "debug=player_only");
                            }
                            var playerId = context.getSource().getPlayer().getUUID();
                            boolean enabled = "on".equalsIgnoreCase(StringArgumentType.getString(context, "enabled"));
                            DebugLogManager.INSTANCE.setAll(playerId, enabled);
                            return AccessCommandService.reply(context.getSource(), "debug=all=" + (enabled ? "on" : "off"));
                        })));

        root.then(Commands.literal("status").executes(context -> {
            if (context.getSource().getPlayer() == null) {
                return AccessCommandService.reply(context.getSource(), "debug=player_only");
            }
            var playerId = context.getSource().getPlayer().getUUID();
            String status = DebugLogManager.INSTANCE.isEnabled(playerId) ? "on" : "off";
            String features = DebugLogManager.INSTANCE.getFeatures(playerId).stream().map(DebugFeature::token).sorted().reduce((a, b) -> a + "|" + b).orElse("none");
            return AccessCommandService.reply(context.getSource(), "debug=status=" + status + "\nfeatures=" + features);
        }));

        root.then(Commands.literal("trace")
                .then(Commands.literal("on").executes(context -> {
                    if (context.getSource().getPlayer() == null) {
                        return AccessCommandService.reply(context.getSource(), "debug=player_only");
                    }
                    var playerId = context.getSource().getPlayer().getUUID();
                    DebugTraceManager.INSTANCE.enable(playerId);
                    return AccessCommandService.reply(context.getSource(), "debug=trace:on");
                }))
                .then(Commands.literal("off").executes(context -> {
                    if (context.getSource().getPlayer() == null) {
                        return AccessCommandService.reply(context.getSource(), "debug=player_only");
                    }
                    var playerId = context.getSource().getPlayer().getUUID();
                    DebugTraceManager.INSTANCE.disable(playerId);
                    return AccessCommandService.reply(context.getSource(), "debug=trace:off");
                }))
                .then(Commands.literal("status").executes(context -> {
                    if (context.getSource().getPlayer() == null) {
                        return AccessCommandService.reply(context.getSource(), "debug=player_only");
                    }
                    var playerId = context.getSource().getPlayer().getUUID();
                    return AccessCommandService.reply(context.getSource(), "debug=trace:" + (DebugTraceManager.INSTANCE.isEnabled(playerId) ? "on" : "off"));
                })));

        dispatcher.register(root);
    }
}

