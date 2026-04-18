package com.myudog.myulib.api.hologram;

import com.myudog.myulib.api.hologram.network.HologramNetworking;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class HologramManager {
    private static final Map<Identifier, HologramDefinition> REGISTRY = new ConcurrentHashMap<>();

    public static void register(HologramDefinition definition) {
        REGISTRY.put(definition.id(), definition);
    }

    public static void unregister(Identifier id) {
        REGISTRY.remove(id);
    }

    public static HologramDefinition get(Identifier id) {
        return REGISTRY.get(id);
    }

    public static AABB cuboidFromCorners(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new AABB(
                Math.min(x1, x2),
                Math.min(y1, y2),
                Math.min(z1, z2),
                Math.max(x1, x2),
                Math.max(y1, y2),
                Math.max(z1, z2)
        );
    }

    /**
     * 🌟 將特定投影同步給指定玩家 (實現選擇性渲染)
     */
    public static void updatePlayerView(ServerPlayer player, List<Identifier> ids) {
        List<HologramDefinition> toSync = REGISTRY.entrySet().stream()
                .filter(e -> ids.contains(e.getKey()))
                .map(Map.Entry::getValue)
                .toList();
        HologramNetworking.syncToPlayer(player, toSync);
    }

    public static Map<Identifier, HologramDefinition> all() {
        return Map.copyOf(REGISTRY);
    }

    public static void clearForPlayer(ServerPlayer player) {
        HologramNetworking.syncToPlayer(player, List.of());
    }
}