package com.myudog.myulib.api.game.region;

import com.myudog.myulib.api.game.instance.GameInstance;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class RegionModels {
    private RegionModels() {
    }

    public record RegionBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        public RegionBounds {
            if (maxX < minX || maxY < minY || maxZ < minZ) {
                throw new IllegalArgumentException("Invalid bounds");
            }
        }

        public boolean isZeroSized() {
            return minX == maxX && minY == maxY && minZ == maxZ;
        }

        public boolean contains(double x, double y, double z) {
            return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
        }

        public boolean intersects(RegionBounds other) {
            return other != null && maxX >= other.minX && other.maxX >= minX && maxY >= other.minY && other.maxY >= minY && maxZ >= other.minZ && other.maxZ >= minZ;
        }
    }

    public enum RegionRole { MAIN, SUB }

    public record RegionDefinition(Identifier id, Identifier ownerId, RegionBounds bounds, RegionRole role, Integer gameInstanceId, Map<String, String> metadata) {
        public RegionDefinition {
            Objects.requireNonNull(id, "id");
            Objects.requireNonNull(ownerId, "ownerId");
            Objects.requireNonNull(bounds, "bounds");
            role = role == null ? RegionRole.SUB : role;
            metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
        }

        public RegionDefinition withGameInstanceId(Integer gameInstanceId) {
            return new RegionDefinition(id, ownerId, bounds, role, gameInstanceId, metadata);
        }
    }

    public interface RegionSignal {
    }

    public record RegionRegisteredSignal(RegionDefinition region) implements RegionSignal {
    }

    public record RegionUnregisteredSignal(RegionDefinition region) implements RegionSignal {
    }

    public record RegionEnteredSignal(RegionDefinition region, long entityId, Object position, Identifier previousRegionId) implements RegionSignal {
    }

    public record RegionExitedSignal(RegionDefinition region, long entityId, Object position, Identifier nextRegionId) implements RegionSignal {
    }

    public record RegionBoundarySignal(RegionDefinition region, long entityId, Object from, Object to, String reason) implements RegionSignal {
    }

    public record RegionContext(RegionDefinition region, RegionSignal signal, GameInstance<?> gameInstance, Map<String, String> metadata) {
        public RegionContext {
            metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
        }
    }

    @FunctionalInterface
    public interface RegionCondition {
        boolean test(RegionContext context);
    }

    @FunctionalInterface
    public interface RegionAction {
        void execute(RegionContext context);
    }

    public record RegionRule(String id, Class<? extends RegionSignal> signalType, List<RegionCondition> conditions, List<RegionAction> actions, int priority) {
        public RegionRule {
            id = Objects.requireNonNullElse(id, "");
            signalType = Objects.requireNonNull(signalType, "signalType");
            conditions = conditions == null ? List.of() : List.copyOf(conditions);
            actions = actions == null ? List.of() : List.copyOf(actions);
        }

        public boolean matches(RegionSignal signal) {
            return signal != null && signalType.isInstance(signal);
        }
    }

    public record RegionRuleSet(List<RegionRule> rules) {
        public RegionRuleSet {
            rules = rules == null ? List.of() : List.copyOf(rules);
        }
    }
}


