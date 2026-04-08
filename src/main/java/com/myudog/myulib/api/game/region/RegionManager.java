package com.myudog.myulib.api.game.region;

import com.myudog.myulib.api.game.instance.GameInstance;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegionManager {
    private static final Map<Identifier, RegionModels.RegionDefinition> REGIONS = new LinkedHashMap<>();
    private static final Map<Integer, Map<Identifier, RegionModels.RegionDefinition>> REGIONS_BY_INSTANCE = new ConcurrentHashMap<>();
    private static final List<RegionModels.RegionRule> RULES = new ArrayList<>();

    private RegionManager() {
    }

    public static void install() {
    }

    public static void register(RegionModels.RegionDefinition region) {
        REGIONS.put(region.id(), region);
    }

    public static void registerAll(Iterable<RegionModels.RegionDefinition> regions) {
        if (regions != null) {
            for (RegionModels.RegionDefinition region : regions) {
                register(region);
            }
        }
    }

    public static RegionModels.RegionDefinition unregister(Identifier regionId) {
        return REGIONS.remove(regionId);
    }

    public static RegionModels.RegionDefinition get(Identifier regionId) {
        return REGIONS.get(regionId);
    }

    public static List<RegionModels.RegionDefinition> getByOwner(Identifier ownerId) {
        return REGIONS.values().stream().filter(region -> region.ownerId().equals(ownerId)).toList();
    }

    public static List<RegionModels.RegionDefinition> getByGameInstance(int instanceId) {
        Map<Identifier, RegionModels.RegionDefinition> map = REGIONS_BY_INSTANCE.get(instanceId);
        return map == null ? List.of() : List.copyOf(map.values());
    }

    public static List<RegionModels.RegionDefinition> findAt(double x, double y, double z) {
        return REGIONS.values().stream().filter(region -> region.bounds().contains(x, y, z)).toList();
    }

    public static void validate(Iterable<RegionModels.RegionDefinition> regions) {
        // Minimal validation: ensure at most one main region and no zero-sized main region.
        int mainCount = 0;
        if (regions != null) {
            for (RegionModels.RegionDefinition region : regions) {
                if (region.role() == RegionModels.RegionRole.MAIN) {
                    mainCount++;
                    if (region.bounds().isZeroSized()) {
                        throw new IllegalArgumentException("Main region cannot be zero-sized: " + region.id());
                    }
                }
            }
        }
        if (mainCount > 1) {
            throw new IllegalArgumentException("Only one main region is allowed");
        }
    }

    public static void publish(RegionModels.RegionSignal signal) {
        // Intentionally lightweight: the in-memory manager does not route signals further yet.
    }

    public static void registerRule(RegionModels.RegionRule rule) {
        RULES.add(rule);
    }

    public static void clearRules() {
        RULES.clear();
    }

    public static void bindInstance(GameInstance<?> instance, Iterable<RegionModels.RegionDefinition> regions) {
        Map<Identifier, RegionModels.RegionDefinition> map = new LinkedHashMap<>();
        if (regions != null) {
            for (RegionModels.RegionDefinition region : regions) {
                RegionModels.RegionDefinition attached = region.withGameInstanceId(instance.getInstanceId());
                map.put(attached.id(), attached);
                REGIONS.put(attached.id(), attached);
            }
        }
        REGIONS_BY_INSTANCE.put(instance.getInstanceId(), map);
    }

    public static void unbindInstance(int instanceId) {
        Map<Identifier, RegionModels.RegionDefinition> removed = REGIONS_BY_INSTANCE.remove(instanceId);
        if (removed != null) {
            for (Identifier id : removed.keySet()) {
                REGIONS.remove(id);
            }
        }
    }
}


