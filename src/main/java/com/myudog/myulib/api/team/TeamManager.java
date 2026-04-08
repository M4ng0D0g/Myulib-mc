package com.myudog.myulib.api.team;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.UnaryOperator;

public final class TeamManager {
    private static final Map<String, TeamDefinition> TEAMS = new LinkedHashMap<>();
    private static final Map<String, Set<UUID>> TEAM_MEMBERS = new LinkedHashMap<>();
    private static final Map<UUID, String> PLAYER_TEAM = new LinkedHashMap<>();

    private TeamManager() {
    }

    public static void install() {
    }

    public static TeamDefinition register(TeamDefinition team) {
        Objects.requireNonNull(team, "team");
        TEAMS.put(team.id(), team);
        TEAM_MEMBERS.computeIfAbsent(team.id(), ignored -> new LinkedHashSet<>());
        return team;
    }

    public static TeamDefinition update(String teamId, UnaryOperator<TeamDefinition> updater) {
        Objects.requireNonNull(teamId, "teamId");
        Objects.requireNonNull(updater, "updater");
        TeamDefinition existing = TEAMS.get(teamId);
        if (existing == null) {
            return null;
        }
        TeamDefinition updated = Objects.requireNonNull(updater.apply(existing), "updated team");
        TEAMS.put(teamId, updated);
        return updated;
    }

    public static TeamDefinition unregister(String teamId) {
        TEAM_MEMBERS.remove(teamId);
        for (Map.Entry<UUID, String> entry : new LinkedHashMap<>(PLAYER_TEAM).entrySet()) {
            if (Objects.equals(entry.getValue(), teamId)) {
                PLAYER_TEAM.remove(entry.getKey());
            }
        }
        return TEAMS.remove(teamId);
    }

    public static TeamDefinition get(String teamId) {
        return TEAMS.get(teamId);
    }

    public static boolean addPlayer(String teamId, UUID playerId) {
        if (!TEAMS.containsKey(teamId) || playerId == null) {
            return false;
        }
        String previous = PLAYER_TEAM.put(playerId, teamId);
        if (previous != null && !previous.equals(teamId)) {
            Set<UUID> previousMembers = TEAM_MEMBERS.get(previous);
            if (previousMembers != null) {
                previousMembers.remove(playerId);
            }
        }
        TEAM_MEMBERS.computeIfAbsent(teamId, ignored -> new LinkedHashSet<>()).add(playerId);
        return true;
    }

    public static boolean removePlayer(UUID playerId) {
        String teamId = PLAYER_TEAM.remove(playerId);
        if (teamId == null) {
            return false;
        }
        Set<UUID> members = TEAM_MEMBERS.get(teamId);
        if (members != null) {
            members.remove(playerId);
        }
        return true;
    }

    public static String teamOf(UUID playerId) {
        return PLAYER_TEAM.get(playerId);
    }

    public static Set<UUID> members(String teamId) {
        Set<UUID> members = TEAM_MEMBERS.get(teamId);
        return members == null ? Set.of() : Collections.unmodifiableSet(new LinkedHashSet<>(members));
    }

    public static List<TeamDefinition> all() {
        return List.copyOf(TEAMS.values());
    }

    public static Map<String, TeamDefinition> snapshot() {
        return Map.copyOf(TEAMS);
    }

    public static void clear() {
        TEAMS.clear();
        TEAM_MEMBERS.clear();
        PLAYER_TEAM.clear();
    }
}


