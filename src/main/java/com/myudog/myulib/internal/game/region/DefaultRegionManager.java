package com.myudog.myulib.internal.game.region;

import com.myudog.myulib.api.game.instance.GameInstance;
import com.myudog.myulib.api.game.region.RegionManager;
import com.myudog.myulib.api.game.region.RegionModels;
import net.minecraft.resources.Identifier;

public class DefaultRegionManager {
    public static void install() { RegionManager.install(); }
    public static void register(RegionModels.RegionDefinition region) { RegionManager.register(region); }
    public static void bindInstance(GameInstance<?> instance, Iterable<RegionModels.RegionDefinition> regions) { RegionManager.bindInstance(instance, regions); }
    public static RegionModels.RegionDefinition get(Identifier regionId) { return RegionManager.get(regionId); }
}
