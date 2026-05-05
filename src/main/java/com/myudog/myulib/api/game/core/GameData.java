package com.myudog.myulib.api.game.core;

import com.myudog.myulib.api.game.features.*;

public abstract class GameData {

    public final EcsFeature ECS_FEATURE;
    public final FieldFeature FIELD_FEATURE;
    public final ObjectFeature OBJECT_FEATURE;
    public final TeamFeature TEAM_FEATURE;

    protected GameData(
            EcsFeature ecsFeature,
            FieldFeature fieldFeature,
            ObjectFeature objectFeature,
            TeamFeature teamFeature
    ) {
        this.ECS_FEATURE = ecsFeature;
        this.FIELD_FEATURE = fieldFeature;
        this.OBJECT_FEATURE = objectFeature;
        this.TEAM_FEATURE = teamFeature;
    }

    public void clean(GameInstance<?, ?, ?> instance) {
        ECS_FEATURE.clean(instance);
        FIELD_FEATURE.clean(instance);
        OBJECT_FEATURE.clean(instance);
        TEAM_FEATURE.clean(instance);
    }




}
