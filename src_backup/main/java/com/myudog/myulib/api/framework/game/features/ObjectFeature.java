package com.myudog.myulib.api.framework.game.features;

import com.myudog.myulib.api.framework.game.core.GameInstance;
import com.myudog.myulib.api.core.object.IObjectRt;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public interface ObjectFeature {
    void addRuntimeObject(@NotNull Identifier instanceId, @NotNull IObjectRt obj);

    Optional<IObjectRt> getObject(@NotNull Identifier instanceId);

    Collection<IObjectRt> getRuntimeObjects();

    IObjectRt spawnObject(GameInstance<?, ?, ?> instance, Identifier defId, Identifier instanceId);

    void clean(GameInstance<?, ?, ?> instance);


}
