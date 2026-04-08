package com.myudog.myulib.api.game.feature;

import net.minecraft.resources.Identifier;

import java.util.LinkedHashSet;
import java.util.Set;

public class GameRegionFeature implements GameFeature {
    public final Set<Identifier> regionIds = new LinkedHashSet<>();
    public Identifier mainRegionId;

    public boolean add(Identifier regionId, boolean isMain) {
        boolean added = regionIds.add(regionId);
        if (isMain) {
            mainRegionId = regionId;
        }
        return added;
    }

    public boolean remove(Identifier regionId) {
        if (regionId != null && regionId.equals(mainRegionId)) {
            mainRegionId = null;
        }
        return regionIds.remove(regionId);
    }

    public void clear() {
        regionIds.clear();
        mainRegionId = null;
    }
}


