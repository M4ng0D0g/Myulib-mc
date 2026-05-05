package com.myudog.myulib.api.object.runtime;

import com.myudog.myulib.api.game.core.GameInstance;
import com.myudog.myulib.api.core.Property;
import com.myudog.myulib.api.object.IObjectRt;
import com.myudog.myulib.api.object.ObjectKind;
import com.myudog.myulib.api.object.ObjectState;
import com.myudog.myulib.api.object.definition.BaseObjDef;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.Optional;

public abstract class BaseObjRt<D extends BaseObjDef> implements IObjectRt {

    public static final Property<Vec3> POS = BaseObjDef.POS;

    // 🌟 執行期物件必定持有其藍圖的參照
    protected final D definition;
    private ObjectState state;

    protected BaseObjRt(D definition) {
        this.definition = definition;
        this.state = ObjectState.NOT_LOADED;
    }

    @Override
    public final void spawn(GameInstance<?, ?, ?> instance) {
        if (this.state != ObjectState.NOT_LOADED) return;
        onSpawn(instance);
        this.state = ObjectState.SPAWNED;
    }

    @Override
    public final void destroy(GameInstance<?, ?, ?> instance) {
        if (this.state != ObjectState.SPAWNED) return;
        onDestroy(instance);
        this.state = ObjectState.DESTROYED;
    }

    @Override
    public void onInitialize(GameInstance<?, ?, ?> instance) {
        // 預設不需要初始化行為；子類可覆寫以掛載事件或做一次性準備。
    }

    protected abstract void onSpawn(GameInstance<?, ?, ?> instance);
    protected abstract void onDestroy(GameInstance<?, ?, ?> instance);

    @Override public String getToken() { return definition.getToken(); }
    @Override public ObjectKind getKind() { return definition.getKind(); }
    @Override public Collection<Property<?>> getProperties() { return definition.getProperties(); }
    @Override public Optional<Property<?>> getProperty(String name) { return definition.getProperty(name); }
    public <T> T get(Property<T> prop) { return definition.get(prop); }
    public <T> void set(Property<T> prop, T value) { definition.set(prop, value); }
    @Override
    public void setPosition(Vec3 pos) {
        definition.set(BaseObjDef.POS, pos);
    }
    @Override
    public IObjectRt copy() {
        try {
            return getClass().getDeclaredConstructor(definition.getClass()).newInstance(definition);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("無法複製遊戲物件: " + definition.getToken(), e);
        }
    }

    // 預設取得藍圖上的靜態座標，子類(如實體)可覆寫為動態座標
    @Override public Vec3 getPosition() { return definition.get(BaseObjDef.POS); }
    public ObjectState getState() { return state; }
}