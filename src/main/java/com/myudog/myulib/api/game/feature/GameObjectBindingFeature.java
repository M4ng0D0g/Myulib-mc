package com.myudog.myulib.api.game.feature;

import com.myudog.myulib.api.game.bootstrap.GameObjectConfig;
import com.myudog.myulib.api.game.instance.GameInstance;
import com.myudog.myulib.api.game.object.GameObjectContext;
import com.myudog.myulib.api.game.object.GameObjectDefinition;
import com.myudog.myulib.api.game.object.GameObjectKind;
import com.myudog.myulib.api.game.object.GameObjectRuntime;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GameObjectBindingFeature implements GameFeature {
    public final Map<Identifier, GameObjectConfig> requiredConfigs = new LinkedHashMap<>();
    public final Map<Identifier, GameObjectDefinition> definitions = new LinkedHashMap<>();
    public final Map<Identifier, Object> runtimeBindings = new LinkedHashMap<>();

    public GameObjectDefinition register(GameObjectConfig config) {
        GameObjectDefinition definition = new GameObjectDefinition(config.id(), config.kind(), config.type(), config.name(), config.required(), config.properties());
        requiredConfigs.put(config.id(), config);
        definitions.put(config.id(), definition);
        return definition;
    }

    public GameObjectDefinition register(GameInstance<?> instance, GameObjectConfig config, Object runtimeObject) {
        GameObjectDefinition definition = register(config);
        if (runtimeObject != null) {
            runtimeBindings.put(config.id(), runtimeObject);
            if (runtimeObject instanceof GameObjectRuntime runtime) {
                runtime.onAttach(new GameObjectContext(instance, config.id(), config, runtimeObject, null, config.kind(), config.properties()));
            }
        }
        return definition;
    }

    public GameObjectDefinition register(GameObjectConfig config, Object runtimeObject) {
        return register(null, config, runtimeObject);
    }

    public void bind(GameObjectConfig config, Object runtimeObject) {
        register(config, runtimeObject);
    }

    public void attachRuntime(Identifier id, Object runtimeObject) {
        runtimeBindings.put(id, runtimeObject);
    }

    public Optional<Object> getRuntime(Identifier id) {
        return Optional.ofNullable(runtimeBindings.get(id));
    }

    public Optional<GameObjectDefinition> getDefinition(Identifier id) {
        return Optional.ofNullable(definitions.get(id));
    }

    public List<GameObjectDefinition> findAllByKind(GameObjectKind kind) {
        List<GameObjectDefinition> result = new ArrayList<>();
        for (GameObjectDefinition definition : definitions.values()) {
            if (definition.kind() == kind) {
                result.add(definition);
            }
        }
        return List.copyOf(result);
    }

    public List<GameObjectDefinition> findAllByType(Identifier type) {
        List<GameObjectDefinition> result = new ArrayList<>();
        for (GameObjectDefinition definition : definitions.values()) {
            if (type != null && type.equals(definition.type())) {
                result.add(definition);
            }
        }
        return List.copyOf(result);
    }

    public Optional<GameObjectDefinition> findFirstByKindAndType(GameObjectKind kind, Identifier type) {
        for (GameObjectDefinition definition : definitions.values()) {
            if (definition.kind() == kind && (type == null || type.equals(definition.type()) || type.equals(definition.id()))) {
                return Optional.of(definition);
            }
        }
        return Optional.empty();
    }

    public void bindRuntime(GameInstance<?> instance, Identifier id, Object runtimeObject) {
        runtimeBindings.put(id, runtimeObject);
        if (runtimeObject instanceof GameObjectRuntime runtime) {
            GameObjectDefinition definition = definitions.get(id);
            GameObjectConfig config = requiredConfigs.get(id);
            if (definition != null && config != null) {
                runtime.onAttach(new GameObjectContext(instance, id, config, runtimeObject, null, config.kind(), Map.of()));
            }
        }
    }

    public void tick(GameInstance<?> instance) {
        if (instance == null) {
            return;
        }
        for (Map.Entry<Identifier, Object> entry : runtimeBindings.entrySet()) {
            if (entry.getValue() instanceof GameObjectRuntime runtime) {
                GameObjectConfig config = requiredConfigs.get(entry.getKey());
                if (config != null) {
                    runtime.onTick(new GameObjectContext(instance, entry.getKey(), config, entry.getValue(), null, config.kind(), Map.of()));
                }
            }
        }
    }

    public boolean interact(GameInstance<?> instance, Identifier id, Identifier sourceEntityId, GameObjectKind interactionKind, Map<String, String> payload) {
        Object runtimeObject = runtimeBindings.get(id);
        if (!(runtimeObject instanceof GameObjectRuntime runtime)) {
            return false;
        }
        GameObjectConfig config = requiredConfigs.get(id);
        if (config == null) {
            return false;
        }
        return runtime.onInteract(new GameObjectContext(instance, id, config, runtimeObject, sourceEntityId, interactionKind, payload));
    }

    public boolean interactByKindAndType(GameInstance<?> instance, GameObjectKind interactionKind, Identifier type, Identifier sourceEntityId, Map<String, String> payload) {
        boolean consumed = false;
        for (GameObjectDefinition definition : findAllByKind(interactionKind)) {
            if (type == null || type.equals(definition.type()) || type.equals(definition.id())) {
                consumed |= interact(instance, definition.id(), sourceEntityId, interactionKind, payload);
            }
        }
        return consumed;
    }

    public void registerAll(Iterable<GameObjectConfig> configs) {
        if (configs == null) {
            return;
        }
        for (GameObjectConfig config : configs) {
            register(config);
        }
    }

    public void clear() {
        clear(null);
    }

    public void clear(GameInstance<?> instance) {
        for (Map.Entry<Identifier, Object> entry : runtimeBindings.entrySet()) {
            Object runtimeObject = entry.getValue();
            if (runtimeObject instanceof GameObjectRuntime runtime) {
                GameObjectConfig config = requiredConfigs.get(entry.getKey());
                if (config != null) {
                    runtime.onDetach(new GameObjectContext(instance, entry.getKey(), config, runtimeObject, null, config.kind(), config.properties()));
                }
            }
        }
        requiredConfigs.clear();
        definitions.clear();
        runtimeBindings.clear();
    }
}
