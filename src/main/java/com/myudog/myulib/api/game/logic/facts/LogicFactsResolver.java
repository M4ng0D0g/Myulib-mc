package com.myudog.myulib.api.game.logic.facts;

import com.myudog.myulib.api.game.instance.GameInstance;
import net.minecraft.resources.Identifier;

public interface LogicFactsResolver {
    LogicFactsResolver DEFAULT = new LogicFactsResolver() {
    };

    default int playerCount(GameInstance<?> instance) {
        return instance == null ? 0 : instance.teams().playerCount();
    }

    default int playerCountInTeam(GameInstance<?> instance, Identifier teamId) {
        return instance == null ? 0 : instance.teams().playerCount(teamId);
    }

    default boolean isOnTeam(GameInstance<?> instance, Identifier playerId, Identifier teamId) {
        return instance != null && instance.teams().isOnTeam(playerId, teamId);
    }

    default boolean isRedTeam(GameInstance<?> instance, Identifier playerId) {
        if (instance == null) {
            return false;
        }
        Identifier teamId = instance.teams().teamOf(playerId);
        if (teamId == null) {
            return false;
        }
        return instance.teams().getDefinition(teamId)
            .map(definition -> definition.color().isRed())
            .orElse(false);
    }

    default int gameTimeTicks(GameInstance<?> instance) {
        return instance == null ? 0 : (int) instance.getTickCount();
    }

    default boolean hasSpecialObject(GameInstance<?> instance, Identifier objectId) {
        return instance != null && instance.hasSpecialObject(objectId);
    }
}
