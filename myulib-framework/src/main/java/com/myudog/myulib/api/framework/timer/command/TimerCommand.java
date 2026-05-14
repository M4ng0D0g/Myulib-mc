package com.myudog.myulib.api.framework.timer.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.myudog.myulib.api.core.timer.TimerDefinition;
import com.myudog.myulib.api.core.timer.TimerManager;
import com.myudog.myulib.api.framework.command.AccessCommandService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public final class TimerCommand {
    private static final Set<UUID> TRACKED_TIMERS = new LinkedHashSet<>();

    private TimerCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        var root = Commands.literal(AccessCommandService.COMMAND_PREFIX + "timer")
                .requires(source -> source.permissions().hasPermission(AccessCommandService.gamemasterPermission()));

        root.then(Commands.literal("create")
                .then(Commands.argument("id", StringArgumentType.word())
                        .then(Commands.argument("ticks", LongArgumentType.longArg(1L))
                                .executes(context -> {
                                    String idToken = StringArgumentType.getString(context, "id");
                                    UUID id = AccessCommandService.resolveTimerIdToken(idToken);
                                    long ticks = LongArgumentType.getLong(context, "ticks");
                                    TimerManager.INSTANCE.register(new TimerDefinition(id, ticks));
                                    TRACKED_TIMERS.add(id);
                                    return AccessCommandService.reply(context.getSource(), "timer=create:" + id);
                                }))));

        root.then(Commands.literal("read")
                .then(Commands.argument("id", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(timerIdSuggestions(), builder))
                        .executes(context -> {
                            String idToken = StringArgumentType.getString(context, "id");
                            UUID id = AccessCommandService.resolveTimerIdToken(idToken);
                            return AccessCommandService.reply(context.getSource(), "timer=exists:" + TimerManager.INSTANCE.hasDefinition(id));
                        })));

        root.then(Commands.literal("update")
                .then(Commands.argument("id", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(timerIdSuggestions(), builder))
                        .then(Commands.argument("ticks", LongArgumentType.longArg(1L))
                                .executes(context -> {
                                    String idToken = StringArgumentType.getString(context, "id");
                                    UUID id = AccessCommandService.resolveTimerIdToken(idToken);
                                    long ticks = LongArgumentType.getLong(context, "ticks");
                                    if (!TimerManager.INSTANCE.hasDefinition(id)) {
                                        return AccessCommandService.reply(context.getSource(), "timer=not_found");
                                    }
                                    TimerManager.INSTANCE.unregister(id);
                                    TimerManager.INSTANCE.register(new TimerDefinition(id, ticks));
                                    TRACKED_TIMERS.add(id);
                                    return AccessCommandService.reply(context.getSource(), "timer=updated:" + id);
                                }))));

        root.then(Commands.literal("delete")
                .then(Commands.argument("id", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(timerIdSuggestions(), builder))
                        .executes(context -> {
                            String idToken = StringArgumentType.getString(context, "id");
                            UUID id = AccessCommandService.resolveTimerIdToken(idToken);
                            boolean removed = TimerManager.INSTANCE.unregister(id) != null;
                            TRACKED_TIMERS.remove(id);
                            return AccessCommandService.reply(context.getSource(), removed ? "timer=deleted:" + id : "timer=not_found");
                        })));

        root.then(Commands.literal("list")
                .executes(context -> AccessCommandService.reply(context.getSource(), "timer=definition_count:" + TimerManager.INSTANCE.timerDefinitionCount() + ",tracked:" + TRACKED_TIMERS.size())));

        dispatcher.register(root);
    }

    private static java.util.List<String> timerIdSuggestions() {
        java.util.Set<String> suggestions = new java.util.LinkedHashSet<>();
        for (UUID id : TRACKED_TIMERS) {
            suggestions.add(id.toString());
        }
        return java.util.List.copyOf(suggestions);
    }
}

