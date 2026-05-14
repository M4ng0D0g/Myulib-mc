package com.myudog.myulib.api.framework.field.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.myudog.myulib.api.framework.command.AccessCommandService;
import com.myudog.myulib.api.framework.field.FieldDefinition;
import com.myudog.myulib.api.framework.field.FieldManager;
import com.myudog.myulib.api.framework.field.FieldVisualizationManager;
import com.myudog.myulib.api.core.hologram.HologramFeature;
import com.myudog.myulib.api.core.hologram.network.HologramNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class FieldCommand {
    private FieldCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        var root = Commands.literal(AccessCommandService.COMMAND_PREFIX + "field")
                .requires(source -> source.permissions().hasPermission(AccessCommandService.gamemasterPermission()));

        root.then(Commands.literal("create")
                .then(Commands.argument("id", StringArgumentType.word())
                        .then(Commands.argument("x1", DoubleArgumentType.doubleArg())
                                .then(Commands.argument("y1", DoubleArgumentType.doubleArg())
                                        .then(Commands.argument("z1", DoubleArgumentType.doubleArg())
                                                .then(Commands.argument("x2", DoubleArgumentType.doubleArg())
                                                        .then(Commands.argument("y2", DoubleArgumentType.doubleArg())
                                                                .then(Commands.argument("z2", DoubleArgumentType.doubleArg())
                                                                        .executes(context -> {
                                                                            String fieldToken = StringArgumentType.getString(context, "id");
                                                                            Identifier dimId = context.getSource().getLevel().dimension().identifier();
                                                                            double x1 = DoubleArgumentType.getDouble(context, "x1");
                                                                            double y1 = DoubleArgumentType.getDouble(context, "y1");
                                                                            double z1 = DoubleArgumentType.getDouble(context, "z1");
                                                                            double x2 = DoubleArgumentType.getDouble(context, "x2");
                                                                            double y2 = DoubleArgumentType.getDouble(context, "y2");
                                                                            double z2 = DoubleArgumentType.getDouble(context, "z2");
                                                                            AABB bounds = AccessCommandService.cuboidFromCorners(x1, y1, z1, x2, y2, z2);
                                                                            FieldDefinition field = new FieldDefinition(fieldToken, dimId, bounds, Map.of("source", "command"));
                                                                            AccessCommandService.createField(field);
                                                                            return AccessCommandService.reply(context.getSource(), "field=create:" + field.id());
                                                                        })))))))));

        root.then(Commands.literal("read")
                .then(Commands.argument("id", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.fieldIdSuggestions(), builder))
                        .executes(context -> {
                            String fieldToken = StringArgumentType.getString(context, "id");
                            UUID fieldId = AccessCommandService.resolveFieldIdToken(fieldToken);
                            FieldDefinition field = fieldId == null ? null : FieldManager.INSTANCE.get(fieldId);
                            if (field == null) {
                                return AccessCommandService.reply(context.getSource(), "field=not_found");
                            }
                            return AccessCommandService.reply(context.getSource(), "field=id:" + field.id() + ",dim:" + field.dimensionId());
                        })));

        root.then(Commands.literal("update")
                .then(Commands.argument("id", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.fieldIdSuggestions(), builder))
                        .executes(context -> {
                            String fieldToken = StringArgumentType.getString(context, "id");
                            UUID fieldId = AccessCommandService.resolveFieldIdToken(fieldToken);
                            if (fieldId == null) {
                                return AccessCommandService.reply(context.getSource(), "field=not_found");
                            }
                            FieldDefinition existing = FieldManager.INSTANCE.get(fieldId);
                            if (existing == null) {
                                return AccessCommandService.reply(context.getSource(), "field=not_found");
                            }
                            FieldManager.INSTANCE.unregister(fieldId);
                            var newData = new java.util.HashMap<String, Object>(existing.fieldData());
                            newData.put("updated", "true");
                            AccessCommandService.createField(new FieldDefinition(existing.token(), existing.dimensionId(), existing.bounds(), newData));
                            return AccessCommandService.reply(context.getSource(), "field=updated:" + fieldId);
                        })));

        root.then(Commands.literal("delete")
                .then(Commands.argument("id", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.fieldIdSuggestions(), builder))
                        .executes(context -> {
                            String fieldToken = StringArgumentType.getString(context, "id");
                            UUID fieldId = AccessCommandService.resolveFieldIdToken(fieldToken);
                            if (fieldId == null || FieldManager.INSTANCE.get(fieldId) == null) {
                                return AccessCommandService.reply(context.getSource(), "field=not_found");
                            }
                            AccessCommandService.deleteField(fieldId);
                            return AccessCommandService.reply(context.getSource(), "field=deleted:" + fieldId);
                        })));

        root.then(Commands.literal("list")
                .executes(context -> AccessCommandService.reply(context.getSource(), "field=count:" + FieldManager.INSTANCE.all().size())));

        root.then(Commands.literal("visualize")
                .then(Commands.literal("on")
                        .executes(context -> {
                            if (context.getSource().getPlayer() == null) {
                                return AccessCommandService.reply(context.getSource(), "field=visualize:player_only");
                            }
                            FieldVisualizationManager.INSTANCE.enable(context.getSource().getPlayer().getUUID());
                            return AccessCommandService.reply(context.getSource(), "field=visualize:on");
                        }))
                .then(Commands.literal("off")
                        .executes(context -> {
                            if (context.getSource().getPlayer() == null) {
                                return AccessCommandService.reply(context.getSource(), "field=visualize:player_only");
                            }
                            var player = context.getSource().getPlayer();
                            FieldVisualizationManager.INSTANCE.disable(player.getUUID());
                            HologramNetworking.syncToPlayer(player, List.of());
                            return AccessCommandService.reply(context.getSource(), "field=visualize:off");
                        }))
                .then(Commands.literal("status")
                        .executes(context -> {
                            if (context.getSource().getPlayer() == null) {
                                return AccessCommandService.reply(context.getSource(), "field=visualize:player_only");
                            }
                            var playerId = context.getSource().getPlayer().getUUID();
                            boolean enabled = FieldVisualizationManager.INSTANCE.isEnabled(playerId);
                            var style = FieldVisualizationManager.INSTANCE.getStyle(playerId);
                            return AccessCommandService.reply(context.getSource(), "field=visualize:" + (enabled ? "on" : "off")
                                    + ",radius=" + FieldVisualizationManager.INSTANCE.getRadius(playerId)
                                    + ",mode=" + FieldVisualizationManager.INSTANCE.getMode(playerId).token()
                                    + ",points=" + AccessCommandService.onOff(style.showPoints())
                                    + ",lines=" + AccessCommandService.onOff(style.showLines())
                                    + ",faces=" + AccessCommandService.onOff(style.showFaces())
                                    + ",name=" + AccessCommandService.onOff(style.showName())
                                    + ",axes=" + AccessCommandService.onOff(style.showAxes()));
                        }))
                .then(Commands.literal("radius")
                        .then(Commands.argument("value", IntegerArgumentType.integer(8, 256))
                                .executes(context -> {
                                    if (context.getSource().getPlayer() == null) {
                                        return AccessCommandService.reply(context.getSource(), "field=visualize:player_only");
                                    }
                                    var playerId = context.getSource().getPlayer().getUUID();
                                    int value = IntegerArgumentType.getInteger(context, "value");
                                    FieldVisualizationManager.INSTANCE.setRadius(playerId, value);
                                    return AccessCommandService.reply(context.getSource(), "field=visualize:radius=" + FieldVisualizationManager.INSTANCE.getRadius(playerId));
                                })))
                .then(Commands.literal("mode")
                        .then(Commands.argument("value", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.fieldModeSuggestions(), builder))
                                .executes(context -> {
                                    if (context.getSource().getPlayer() == null) {
                                        return AccessCommandService.reply(context.getSource(), "field=visualize:player_only");
                                    }
                                    var playerId = context.getSource().getPlayer().getUUID();
                                    String raw = StringArgumentType.getString(context, "value");
                                    FieldVisualizationManager.INSTANCE.setMode(playerId, FieldVisualizationManager.DisplayMode.parse(raw));
                                    return AccessCommandService.reply(context.getSource(), "field=visualize:mode=" + FieldVisualizationManager.INSTANCE.getMode(playerId).token());
                                })))
                .then(Commands.literal("show")
                        .then(Commands.argument("feature", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(AccessCommandService.hologramFeatureSuggestions(), builder))
                                .then(Commands.argument("enabled", StringArgumentType.word())
                                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(List.of("on", "off"), builder))
                                        .executes(context -> {
                                            if (context.getSource().getPlayer() == null) {
                                                return AccessCommandService.reply(context.getSource(), "field=visualize:player_only");
                                            }
                                            var playerId = context.getSource().getPlayer().getUUID();
                                            HologramFeature feature = HologramFeature.parse(StringArgumentType.getString(context, "feature"));
                                            boolean enabled = "on".equalsIgnoreCase(StringArgumentType.getString(context, "enabled"));
                                            FieldVisualizationManager.INSTANCE.setFeature(playerId, feature, enabled);
                                            return AccessCommandService.reply(context.getSource(), "field=visualize:show:" + feature.token() + "=" + AccessCommandService.onOff(enabled));
                                        })))));

        dispatcher.register(root);
    }
}

