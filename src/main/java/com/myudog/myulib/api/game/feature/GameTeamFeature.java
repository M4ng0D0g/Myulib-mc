package com.myudog.myulib.api.game.feature;

import com.myudog.myulib.api.game.team.GameTeamDefinition;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class GameTeamFeature implements GameFeature {
    public final Map<Identifier, GameTeamDefinition> definitions = new LinkedHashMap<>();
    public final Map<Identifier, Object> runtimeTeams = new LinkedHashMap<>();
    public final Map<Identifier, Set<Identifier>> teamMembers = new LinkedHashMap<>();
    public final Map<Identifier, Identifier> playerTeams = new LinkedHashMap<>();

    public GameTeamDefinition register(GameTeamDefinition definition) {
        definitions.put(definition.id(), definition);
        teamMembers.computeIfAbsent(definition.id(), ignored -> new LinkedHashSet<>());
        return definition;
    }

    public boolean bindRuntime(Identifier teamId, Object runtimeTeam) {
        if (!definitions.containsKey(teamId)) {
            return false;
        }
        runtimeTeams.put(teamId, runtimeTeam);
        return true;
    }

    public Optional<Object> getRuntime(Identifier teamId) {
        return Optional.ofNullable(runtimeTeams.get(teamId));
    }

    public Optional<GameTeamDefinition> getDefinition(Identifier teamId) {
        return Optional.ofNullable(definitions.get(teamId));
    }

    public boolean addPlayer(Identifier teamId, Identifier playerId) {
        if (!definitions.containsKey(teamId) || playerId == null) {
            return false;
        }
        Identifier previous = playerTeams.put(playerId, teamId);
        if (previous != null && !previous.equals(teamId)) {
            Set<Identifier> previousMembers = teamMembers.get(previous);
            if (previousMembers != null) {
                previousMembers.remove(playerId);
            }
        }
        teamMembers.computeIfAbsent(teamId, ignored -> new LinkedHashSet<>()).add(playerId);
        return true;
    }

    public boolean removePlayer(Identifier playerId) {
        Identifier teamId = playerTeams.remove(playerId);
        if (teamId == null) {
            return false;
        }
        Set<Identifier> members = teamMembers.get(teamId);
        if (members != null) {
            members.remove(playerId);
        }
        return true;
    }

    public boolean isOnTeam(Identifier playerId, Identifier teamId) {
        return teamId != null && teamId.equals(playerTeams.get(playerId));
    }

    public int playerCount(Identifier teamId) {
        Set<Identifier> members = teamMembers.get(teamId);
        return members == null ? 0 : members.size();
    }

    public int playerCount() {
        return playerTeams.size();
    }

    public Identifier teamOf(Identifier playerId) {
        return playerTeams.get(playerId);
    }

    public void clear() {
        definitions.clear();
        runtimeTeams.clear();
        teamMembers.clear();
        playerTeams.clear();
    }
}




