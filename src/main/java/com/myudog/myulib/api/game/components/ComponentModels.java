package com.myudog.myulib.api.game.components;

import com.myudog.myulib.api.game.instance.GameInstance;
import com.myudog.myulib.api.game.region.RegionModels;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class ComponentModels {
    private ComponentModels() {
    }

    public sealed interface ComponentBindingTarget permits ComponentBindingTarget.Entity, ComponentBindingTarget.Block, ComponentBindingTarget.Game {
        record Entity(long entityId) implements ComponentBindingTarget {
        }

        record Block(Object pos, Identifier worldId) implements ComponentBindingTarget {
        }

        record Game(int gameInstanceId) implements ComponentBindingTarget {
        }
    }

    public interface ComponentSignal {
    }

    public record ComponentEntitySpawnSignal(long entityId, Identifier worldId, ComponentBindingTarget target) implements ComponentSignal {
    }

    public record ComponentEntityTickSignal(long entityId, Identifier worldId) implements ComponentSignal {
    }

    public record ComponentBlockBreakSignal(Object pos, Identifier worldId, Long playerEntityId) implements ComponentSignal {
    }

    public record ComponentBlockUseSignal(Object pos, Identifier worldId, Long playerEntityId) implements ComponentSignal {
    }

    public record ComponentCustomSignal(String name, Object payload) implements ComponentSignal {
    }

    public record ComponentContext(ComponentBindingDefinition binding, ComponentSignal signal, GameInstance<?> gameInstance, RegionModels.RegionDefinition region, Map<String, String> metadata) {
        public ComponentContext {
            metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
        }
    }

    @FunctionalInterface
    public interface ComponentCondition {
        boolean test(ComponentContext context);
    }

    @FunctionalInterface
    public interface ComponentAction {
        void execute(ComponentContext context);
    }

    public record ComponentRule(String id, Class<? extends ComponentSignal> signalType, List<ComponentCondition> conditions, List<ComponentAction> actions, int priority) {
        public ComponentRule {
            id = Objects.requireNonNullElse(id, "");
            signalType = Objects.requireNonNull(signalType, "signalType");
            conditions = conditions == null ? List.of() : List.copyOf(conditions);
            actions = actions == null ? List.of() : List.copyOf(actions);
        }

        public boolean matches(ComponentSignal signal) {
            return signal != null && signalType.isInstance(signal);
        }
    }

    public record ComponentBindingDefinition(Identifier id, Identifier ownerId, ComponentBindingTarget target, Integer gameInstanceId, Set<Class<? extends ComponentSignal>> signalTypes, List<ComponentCondition> conditions, List<ComponentAction> actions, Map<String, String> metadata) {
        public ComponentBindingDefinition {
            Objects.requireNonNull(id, "id");
            target = Objects.requireNonNull(target, "target");
            signalTypes = signalTypes == null ? Set.of() : Set.copyOf(signalTypes);
            conditions = conditions == null ? List.of() : List.copyOf(conditions);
            actions = actions == null ? List.of() : List.copyOf(actions);
            metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
        }

        public ComponentBindingDefinition withGameInstanceId(Integer id) {
            return new ComponentBindingDefinition(this.id, ownerId, target, id, signalTypes, conditions, actions, metadata);
        }
    }

    public record ComponentRuleSet(List<ComponentRule> rules) {
        public ComponentRuleSet {
            rules = rules == null ? List.of() : List.copyOf(rules);
        }
    }
}


