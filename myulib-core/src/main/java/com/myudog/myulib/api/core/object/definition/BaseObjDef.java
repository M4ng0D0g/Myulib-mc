package com.myudog.myulib.api.core.object.definition;

import com.myudog.myulib.api.core.Property;
import com.myudog.myulib.api.core.object.IObjectDef;
import com.myudog.myulib.api.core.object.ObjectKind;
import com.myudog.myulib.api.core.object.IObjectRt;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class BaseObjDef implements IObjectDef {

    protected final Identifier id;
    protected final ObjectKind kind;

    // 🌟 屬性系統移交給 Definition 管理
    private final Map<String, Property<?>> registry = new HashMap<>();
    private final Map<Property<?>, java.lang.Object> values = new HashMap<>();

    public static final Property<Vec3> POS = new Property<>(
            "pos", Vec3.class,
            s -> {
                String cleanStr = s.replace("(", "").replace(")", "");
                String[] p = cleanStr.split(",");
                return new Vec3(
                        Double.parseDouble(p[0].trim()),
                        Double.parseDouble(p[1].trim()),
                        Double.parseDouble(p[2].trim())
                );
            },
            Vec3::toString
    );

    protected BaseObjDef(Identifier id, ObjectKind kind) {
        this.id = id;
        this.kind = kind;
        define();
        registerProperties();
    }

    protected <T> void define() {
        registry.put(BaseObjDef.POS.name(), BaseObjDef.POS);
        values.put(BaseObjDef.POS, Vec3.ZERO);
    }

    protected abstract void registerProperties();

    @SuppressWarnings("unchecked")
    public <T> T get(Property<T> prop) { return (T) values.get(prop); }
    public <T> void set(Property<T> prop, T value) { values.put(prop, value); }

    public Collection<Property<?>> getProperties() { return List.copyOf(registry.values()); }
    public Optional<Property<?>> getProperty(String name) { return Optional.ofNullable(registry.get(name)); }

    @Override public Identifier id() { return id; }
    public Identifier toIdentifier() { return id; }
    public String getToken() { return id.getPath(); }
    public ObjectKind getKind() { return kind; }

    // 預設驗證：必須有座標
    @Override public boolean validate() { return get(POS) != null; }

    // 強制子類實作工廠方法
    @Override
    public abstract IObjectRt createRuntimeInstance();
}