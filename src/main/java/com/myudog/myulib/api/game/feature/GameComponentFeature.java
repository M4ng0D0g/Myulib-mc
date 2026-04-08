package com.myudog.myulib.api.game.feature;

import net.minecraft.resources.Identifier;

import java.util.LinkedHashSet;
import java.util.Set;

public class GameComponentFeature implements GameFeature {
    public final Set<Identifier> bindingIds = new LinkedHashSet<>();

    public boolean add(Identifier bindingId) {
        return bindingIds.add(bindingId);
    }

    public boolean remove(Identifier bindingId) {
        return bindingIds.remove(bindingId);
    }

    public void clear() {
        bindingIds.clear();
    }
}


