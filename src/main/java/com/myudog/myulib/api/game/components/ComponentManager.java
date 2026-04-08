package com.myudog.myulib.api.game.components;

import com.myudog.myulib.api.game.instance.GameInstance;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ComponentManager {
    private static final Map<Identifier, ComponentModels.ComponentBindingDefinition> BINDINGS = new LinkedHashMap<>();
    private static final Map<Integer, Map<Identifier, ComponentModels.ComponentBindingDefinition>> BINDINGS_BY_INSTANCE = new ConcurrentHashMap<>();
    private static final List<ComponentModels.ComponentRule> RULES = new ArrayList<>();

    private ComponentManager() {
    }

    public static void install() {
    }

    public static void register(ComponentModels.ComponentBindingDefinition binding) {
        BINDINGS.put(binding.id(), binding);
    }

    public static void registerAll(Iterable<ComponentModels.ComponentBindingDefinition> bindings) {
        if (bindings != null) {
            for (ComponentModels.ComponentBindingDefinition binding : bindings) {
                register(binding);
            }
        }
    }

    public static ComponentModels.ComponentBindingDefinition unregister(Identifier bindingId) {
        return BINDINGS.remove(bindingId);
    }

    public static ComponentModels.ComponentBindingDefinition get(Identifier bindingId) {
        return BINDINGS.get(bindingId);
    }

    public static List<ComponentModels.ComponentBindingDefinition> getByOwner(Identifier ownerId) {
        return BINDINGS.values().stream().filter(binding -> Objects.equals(binding.ownerId(), ownerId)).toList();
    }

    public static List<ComponentModels.ComponentBindingDefinition> getByGameInstance(int instanceId) {
        Map<Identifier, ComponentModels.ComponentBindingDefinition> map = BINDINGS_BY_INSTANCE.get(instanceId);
        return map == null ? List.of() : List.copyOf(map.values());
    }

    public static void publish(ComponentModels.ComponentSignal signal) {
        if (signal == null) {
            return;
        }
        List<ComponentModels.ComponentRule> rules = RULES.stream().sorted(Comparator.comparingInt(ComponentModels.ComponentRule::priority).reversed()).toList();
        for (ComponentModels.ComponentRule rule : rules) {
            if (!rule.matches(signal)) {
                continue;
            }
            for (ComponentModels.ComponentBindingDefinition binding : BINDINGS.values()) {
                if (rule.signalType().isInstance(signal)) {
                    ComponentModels.ComponentContext context = new ComponentModels.ComponentContext(binding, signal, null, null, binding.metadata());
                    boolean passed = true;
                    for (ComponentModels.ComponentCondition condition : rule.conditions()) {
                        if (!condition.test(context)) {
                            passed = false;
                            break;
                        }
                    }
                    if (passed) {
                        for (ComponentModels.ComponentAction action : rule.actions()) {
                            action.execute(context);
                        }
                    }
                }
            }
        }
    }

    public static void registerRule(ComponentModels.ComponentRule rule) {
        RULES.add(rule);
    }

    public static void clearRules() {
        RULES.clear();
    }

    public static void bindInstance(GameInstance<?> instance, Iterable<ComponentModels.ComponentBindingDefinition> bindings) {
        Map<Identifier, ComponentModels.ComponentBindingDefinition> map = new LinkedHashMap<>();
        if (bindings != null) {
            for (ComponentModels.ComponentBindingDefinition binding : bindings) {
                ComponentModels.ComponentBindingDefinition attached = binding.withGameInstanceId(instance.getInstanceId());
                map.put(attached.id(), attached);
                BINDINGS.put(attached.id(), attached);
            }
        }
        BINDINGS_BY_INSTANCE.put(instance.getInstanceId(), map);
    }

    public static void unbindInstance(int instanceId) {
        Map<Identifier, ComponentModels.ComponentBindingDefinition> removed = BINDINGS_BY_INSTANCE.remove(instanceId);
        if (removed != null) {
            for (Identifier id : removed.keySet()) {
                BINDINGS.remove(id);
            }
        }
    }
}


