package com.myudog.myulib.client.api.hologram;

import com.myudog.myulib.api.hologram.HologramDefinition;
import java.util.ArrayList;
import java.util.List;

public class HologramClientManager {
    private static final List<HologramDefinition> ACTIVE_HOLOGRAMS = new ArrayList<>();

    public static void update(List<HologramDefinition> newHolograms) {
        ACTIVE_HOLOGRAMS.clear();
        ACTIVE_HOLOGRAMS.addAll(newHolograms);
    }

    public static List<HologramDefinition> getActiveHolograms() {
        return ACTIVE_HOLOGRAMS;
    }
}