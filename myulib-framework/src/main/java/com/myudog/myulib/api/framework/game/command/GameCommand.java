package com.myudog.myulib.api.framework.game.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.myudog.myulib.MyulibFramework;
import com.myudog.myulib.api.core.debug.DebugFeature;
import com.myudog.myulib.api.core.debug.DebugLogManager;
import com.myudog.myulib.api.core.Property;
import com.myudog.myulib.api.framework.game.GameConfig;
import com.myudog.myulib.api.framework.game.GameDefinition;
import com.myudog.myulib.api.framework.game.GameInstance;
import com.myudog.myulib.api.framework.game.GameManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;

import java.util.List;
import java.util.Optional;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class GameCommand {
    private static final String COMMAND_PREFIX = MyulibFramework.MOD_ID + ":";

    private GameCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> root = literal(COMMAND_PREFIX + "game")
                .requires(source -> source.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.GAMEMASTERS)));

        root.then(literal("create")
                .then(argument("definition", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(gameDefinitionSuggestions(), builder))
                        .then(argument("instanceId", StringArgumentType.word())
                                .executes(GameCommand::createInstance))));

        LiteralArgumentBuilder<CommandSourceStack> config = literal("config");
        config.then(literal("get")
                .then(instanceIdArgument()
                        .then(argument("property", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(configPropertySuggestions(StringArgumentType.getString(context, "instanceId")), builder))
                                .executes(GameCommand::configGet))));

        config.then(literal("set")
                .then(instanceIdArgument()
                        .then(argument("property", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(configPropertySuggestions(StringArgumentType.getString(context, "instanceId")), builder))
                                .then(argument("value", StringArgumentType.greedyString())
                                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                                configValueSuggestions(StringArgumentType.getString(context, "instanceId"), StringArgumentType.getString(context, "property")), builder))
                                        .executes(GameCommand::configSet)))));

        config.then(literal("list")
                .then(instanceIdArgument()
                        .executes(GameCommand::configList)));

        root.then(config);

        root.then(literal("init")
                .then(argument("instanceId", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(gameInstanceSuggestions(), builder))
                        .executes(context -> {
                            String instanceId = StringArgumentType.getString(context, "instanceId");
                            boolean success = GameManager.INSTANCE.initInstance(instanceId);
                            return reply(context.getSource(), success ? "game=initialized:" + instanceId : "game=init_failed");
                        })));

        root.then(literal("join")
                .then(argument("player", EntityArgument.player())
                        .then(argument("instanceId", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(gameInstanceSuggestions(), builder))
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    String instanceId = StringArgumentType.getString(context, "instanceId");
                                    boolean success = GameManager.INSTANCE.joinPlayer(instanceId, player.getUUID(), null);
                                    return reply(context.getSource(), success ? "game=joined:" + instanceId : "game=join_failed");
                                }))));

        root.then(literal("leave")
                .then(argument("player", EntityArgument.player())
                        .then(argument("instanceId", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(gameInstanceSuggestions(), builder))
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    String instanceId = StringArgumentType.getString(context, "instanceId");
                                    GameManager.INSTANCE.leavePlayer(instanceId, player.getUUID());
                                    return reply(context.getSource(), "game=left:" + instanceId);
                                }))));

        root.then(literal("leave_all")
                .then(argument("player", EntityArgument.player())
                        .executes(context -> {
                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                            GameManager.INSTANCE.leaveAllInstances(player.getUUID());
                            return reply(context.getSource(), "game=left_all");
                        })));

        root.then(literal("start")
                .then(argument("instanceId", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(gameInstanceSuggestions(), builder))
                        .executes(context -> {
                            String instanceId = StringArgumentType.getString(context, "instanceId");
                            boolean success = GameManager.INSTANCE.startInstance(instanceId);
                            return reply(context.getSource(), success ? "game=started:" + instanceId : "game=start_failed");
                        })));

        root.then(literal("shutdown")
                .then(argument("instanceId", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(gameInstanceSuggestions(), builder))
                        .executes(context -> {
                            String instanceId = StringArgumentType.getString(context, "instanceId");
                            boolean success = GameManager.INSTANCE.shutdownInstance(instanceId);
                            return reply(context.getSource(), success ? "game=shutdown:" + instanceId : "game=shutdown_failed");
                        })));

        root.then(literal("delete")
                .then(argument("instanceId", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(gameInstanceSuggestions(), builder))
                        .executes(context -> {
                            String instanceId = StringArgumentType.getString(context, "instanceId");
                            boolean success = GameManager.INSTANCE.deleteInstance(instanceId);
                            return reply(context.getSource(), success ? "game=deleted:" + instanceId : "game=delete_failed");
                        })));

        dispatcher.register(root);
    }

    private static int createInstance(CommandContext<CommandSourceStack> context) {
        String definitionRaw = StringArgumentType.getString(context, "definition");
        String instanceId = StringArgumentType.getString(context, "instanceId");

        try {
            Identifier defId = Identifier.parse(definitionRaw);
            GameManager.INSTANCE.createInstance(defId, instanceId, context.getSource().getLevel());
            return reply(context.getSource(), "game=created:" + instanceId);
        } catch (Exception ex) {
            return reply(context.getSource(), "game=create_failed:" + ex.getMessage());
        }
    }

    private static int configGet(CommandContext<CommandSourceStack> context) {
        GameInstance<?, ?, ?> instance = resolveInstance(context);
        if (instance == null) {
            return reply(context.getSource(), "game=not_found");
        }

        GameConfig config = instance.getConfig();
        String propertyName = StringArgumentType.getString(context, "property");
        if (config.getProperty(propertyName).isEmpty()) {
            return reply(context.getSource(), "game=config:property_not_found:" + propertyName);
        }

        String value = config.getPropertyAsString(propertyName);
        return reply(context.getSource(), "game=config:get:" + instance.getInstanceId() + "," + propertyName + "=" + (value == null ? "<unset>" : value));
    }

    private static int configSet(CommandContext<CommandSourceStack> context) {
        GameInstance<?, ?, ?> instance = resolveInstance(context);
        if (instance == null) {
            return reply(context.getSource(), "game=not_found");
        }

        GameConfig config = instance.getConfig();
        String propertyName = StringArgumentType.getString(context, "property");
        String valueInput = StringArgumentType.getString(context, "value");

        Optional<Property<?>> property = config.getProperty(propertyName);
        if (property.isEmpty()) {
            return reply(context.getSource(), "game=config:property_not_found:" + propertyName);
        }

        if (!config.setPropertyFromString(propertyName, valueInput)) {
            String typeName = property.get().type().getSimpleName();
            return reply(context.getSource(), "game=config:parse_failed:" + propertyName + ":" + typeName + "=" + valueInput);
        }

        String value = config.getPropertyAsString(propertyName);
        return reply(context.getSource(), "game=config:set:" + instance.getInstanceId() + "," + propertyName + "=" + (value == null ? "<unset>" : value));
    }

    private static int configList(CommandContext<CommandSourceStack> context) {
        GameInstance<?, ?, ?> instance = resolveInstance(context);
        if (instance == null) {
            return reply(context.getSource(), "game=not_found");
        }

        GameConfig config = instance.getConfig();
        StringBuilder builder = new StringBuilder();
        builder.append("game=config:list");
        builder.append("\ninstance=").append(instance.getInstanceId());
        builder.append("\ndefinition=").append(instance.getDefinitionId());

        config.getPropertyNames().stream()
                .sorted()
                .forEach(name -> {
                    String value = config.getPropertyAsString(name);
                    builder.append("\n").append(name).append("=").append(value == null ? "<unset>" : value);
                });

        return reply(context.getSource(), builder.toString());
    }

    private static GameInstance<?, ?, ?> resolveInstance(CommandContext<CommandSourceStack> context) {
        String instanceId = StringArgumentType.getString(context, "instanceId");
        return GameManager.INSTANCE.getInstance(instanceId);
    }

    private static List<String> gameDefinitionSuggestions() {
        return GameManager.INSTANCE.getDefinitions().stream().map(GameDefinition::id).map(Identifier::toString).toList();
    }

    private static List<String> gameInstanceSuggestions() {
        return GameManager.INSTANCE.getInstances().stream().map(GameInstance::getInstanceId).toList();
    }

    private static com.mojang.brigadier.builder.RequiredArgumentBuilder<CommandSourceStack, String> instanceIdArgument() {
        return argument("instanceId", StringArgumentType.word())
                .suggests((context, builder) -> SharedSuggestionProvider.suggest(gameInstanceSuggestions(), builder));
    }

    private static List<String> configPropertySuggestions(String instanceId) {
        GameInstance<?, ?, ?> instance = GameManager.INSTANCE.getInstance(instanceId);
        if (instance == null) {
            return List.of();
        }
        return instance.getConfig().getPropertyNames().stream().sorted().toList();
    }

    private static List<String> configValueSuggestions(String instanceId, String propertyName) {
        GameInstance<?, ?, ?> instance = GameManager.INSTANCE.getInstance(instanceId);
        if (instance == null) {
            return List.of();
        }
        GameConfig config = instance.getConfig();
        Optional<Property<?>> property = config.getProperty(propertyName);
        if (property.isEmpty()) {
            return List.of();
        }

        java.util.LinkedHashSet<String> suggestions = new java.util.LinkedHashSet<>();
        String current = config.getPropertyAsString(propertyName);
        if (current != null && !current.isBlank()) {
            suggestions.add(current);
        }

        Class<?> type = property.get().type();
        if (type == Boolean.class || type == boolean.class) {
            suggestions.add("true");
            suggestions.add("false");
        } else if (type.isEnum()) {
            for (Object constant : type.getEnumConstants()) {
                if (constant instanceof Enum<?> enumValue) {
                    suggestions.add(enumValue.name());
                }
            }
        }

        return List.copyOf(suggestions);
    }

    private static int reply(CommandSourceStack source, String message) {
        source.sendSuccess(() -> Component.literal(message), true);
        DebugLogManager.INSTANCE.log(DebugFeature.COMMAND,
                "source=" + source.getTextName() + ",message=" + message.replace('\n', '|'));
        return 1;
    }
}




