package com.myudog.myulib.api.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyContainer {

    private final Map<Property<?>, Object> properties = new ConcurrentHashMap<>();
    private final Map<String, Property<?>> registry = new ConcurrentHashMap<>();

    public void register(Property<?> property) {
        registry.put(property.name(), property);
    }

    public <T> Optional<T> get(Property<T> property) {
        Object rawValue = properties.get(property);

        // 如果值存在，且型別與藍圖相符
        if (property.type().isInstance(rawValue)) {
            return Optional.of(property.type().cast(rawValue));
        }

        // 如果值是 null 或型別不符，安全地回傳空的 Optional
        return Optional.empty();
    }

    public <T> T getOrCreate(Property<T> property, T defaultValue) {
        Object rawValue = properties.get(property);

        if (property.type().isInstance(rawValue)) {
            return property.type().cast(rawValue);
        }

        properties.put(property, defaultValue);
        return defaultValue;
    }

    public <T> void set(Property<T> property, T value) {
        if (value == null) {
            properties.remove(property);
        }
        else {
            properties.put(property, value);
        }
        registry.put(property.name(), property);
    }

    public <T> void set(Property<T> property, String value) {
        T parsedValue = property.parse(value);
        set(property, parsedValue);
    }

    /**
     * 透過名稱取得屬性定義。
     */
    public Optional<Property<?>> getByName(String name) {
        return Optional.ofNullable(registry.get(name));
    }

    /**
     * 透過名稱設定數值 (輸入為字串，自動解析)。
     * 適合用於：指令輸入、設定檔讀取。
     */
    public boolean setByName(String name, String valueInput) {
        Property<?> prop = registry.get(name);
        if (prop == null) return false;

        try {
            // 利用 Property 內建的解析器轉換型別
            Object parsedValue = prop.parse(valueInput);
            if (parsedValue == null) {
                properties.remove(prop);
            }
            else {
                properties.put(prop, parsedValue);
            }
            return true;
        } catch (Exception e) {
            // 解析失敗（例如：數字格式錯誤）
            return false;
        }
    }

    /**
     * 透過名稱取得數值的字串表示。
     * 適合用於：顯示在 UI、序列化存檔。
     */
    public String getAsString(String name) {
        Property<?> prop = registry.get(name);
        if (prop == null) return null;

        Object val = properties.get(prop);
        if (val == null) return null;

        // 利用 Property 內建的格式化器轉回字串
        return castAndFormat(prop, val);
    }

    /**
     * 輔助方法：處理泛型轉型並格式化。
     */
    @SuppressWarnings("unchecked")
    private <T> String castAndFormat(Property<T> prop, Object value) {
        return prop.toString((T) value);
    }

    /**
     * 取得目前所有已註冊的屬性名稱。
     */
    public Set<String> getRegisteredNames() {
        return Set.copyOf(registry.keySet());
    }

    /**
     * 取得目前已註冊的屬性定義。
     */
    public Collection<Property<?>> getRegisteredProperties() {
        return List.copyOf(registry.values());
    }

    /**
     * 取得目前「已有數值」的屬性名稱。
     */
    public Set<String> getActiveNames() {
        return properties.keySet().stream()
                .map(Property::name)
                .collect(java.util.stream.Collectors.toSet());
    }


}
