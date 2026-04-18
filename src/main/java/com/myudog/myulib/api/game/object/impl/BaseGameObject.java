package com.myudog.myulib.api.game.object.impl;

import com.myudog.myulib.api.game.core.GameInstance;
import com.myudog.myulib.api.game.object.GameObjectKind;
import com.myudog.myulib.api.game.object.GameObjectProperty;
import com.myudog.myulib.api.game.object.GameObjectState;
import com.myudog.myulib.api.game.object.IGameObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class BaseGameObject implements IGameObject {

    protected final Identifier id;
    protected final GameObjectKind kind;
    private GameObjectState state;

    private final Map<String, GameObjectProperty<?>> registry = new HashMap<>();
    private final Map<GameObjectProperty<?>, Object> values = new HashMap<>();

    public static final GameObjectProperty<Vec3> POS = new GameObjectProperty<>(
            "pos",
            Vec3.class,
            s -> {
                String[] p = s.split(",");
                return new Vec3(Double.parseDouble(p[0]), Double.parseDouble(p[1]), Double.parseDouble(p[2]));
            }
    );

    protected BaseGameObject(Identifier id, GameObjectKind kind) {
        this.id = id;
        this.kind = kind;
        this.state = GameObjectState.NOT_LOADED;

        define(POS, Vec3.ZERO);
        registerProperties();
    }

    /** 🌟 狀態檢查：僅能執行一次生成 */
    @Override
    public final void spawn(GameInstance<?, ?, ?> instance) {
        if (this.state != GameObjectState.NOT_LOADED) return;

        onSpawn(instance); // 呼叫子類實作的具體生成邏輯
        this.state = GameObjectState.SPAWNED;
    }

    /** 🌟 狀態檢查：僅能執行一次銷毀 */
    @Override
    public final void destroy(GameInstance<?, ?, ?> instance) {
        if (this.state != GameObjectState.SPAWNED) return;

        onDestroy(instance); // 呼叫子類實作的具體銷毀邏輯
        this.state = GameObjectState.DESTROYED;
    }

    /** 子類實作具體的物理生成邏輯 */
    protected abstract void onSpawn(GameInstance<?, ?, ?> instance);

    /** 子類實作具體的資源清理邏輯 */
    protected abstract void onDestroy(GameInstance<?, ?, ?> instance);

    public GameObjectState getState() { return state; }
    protected void setState(GameObjectState state) { this.state = state; }

    protected <T> void define(GameObjectProperty<T> prop, T defaultValue) {
        registry.put(prop.name(), prop);
        values.put(prop, defaultValue);
    }

    /** 子類在此處定義哪些變數要暴露給指令系統 */
    protected abstract void registerProperties();

    @SuppressWarnings("unchecked")
    public <T> T get(GameObjectProperty<T> prop) {
        return (T) values.get(prop);
    }

    public <T> void set(GameObjectProperty<T> prop, T value) {
        values.put(prop, value);
    }

    @Override public Collection<GameObjectProperty<?>> getProperties() { return registry.values(); }
    @Override public Optional<GameObjectProperty<?>> getProperty(String name) { return Optional.ofNullable(registry.get(name)); }
    @Override public Identifier getId() { return id; }
    @Override public GameObjectKind getKind() { return kind; }

    @Override public Vec3 getPosition() { return get(POS); }
    @Override public void setPosition(Vec3 pos) { set(POS, pos); }

    protected final void copyBaseStateTo(BaseGameObject target) {
        target.state = GameObjectState.NOT_LOADED;
        target.values.clear();
        for (Map.Entry<GameObjectProperty<?>, Object> entry : this.values.entrySet()) {
            target.values.put(entry.getKey(), cloneValue(entry.getValue()));
        }
    }

    private static Object cloneValue(Object value) {
        if (value instanceof CompoundTag tag) {
            return tag.copy();
        }
        return value;
    }

}