package com.myudog.myulib.api.framework.game;

import com.myudog.myulib.api.core.Property;
import com.myudog.myulib.api.core.PropertyContainer;
import net.minecraft.resources.Identifier;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * GameConfig
 * 遊戲配置基類，支援動態變數註冊。
 */
public abstract class GameConfig {

    public final UUID SPECTATOR_TEAM = UUID.randomUUID();
    public final Identifier GAME_DEF_ID;

    public final PropertyContainer PROPERTIES;

    public GameConfig(Identifier gameDefId) {
        this.GAME_DEF_ID = gameDefId;
        this.PROPERTIES = new PropertyContainer();
        registerProperties();
    }

    /**
     * 子類在此註冊可透過指令設定的變數。
     */
    protected abstract void registerProperties();

    public abstract boolean validate() throws Exception;

    public boolean allowSpectator() {
        return true;
    }

    public final Collection<Property<?>> getProperties() {
        return PROPERTIES.getRegisteredProperties();
    }

    public final Set<String> getPropertyNames() {
        return PROPERTIES.getRegisteredNames();
    }

    public final Optional<Property<?>> getProperty(String name) {
        return PROPERTIES.getByName(name);
    }

    public final String getPropertyAsString(String name) {
        return PROPERTIES.getAsString(name);
    }

    public final boolean setPropertyFromString(String name, String valueInput) {
        return PROPERTIES.setByName(name, valueInput);
    }
}
