package com.myudog.myulib.api.team;

import com.myudog.myulib.api.ui.ConfigurationUiBridge;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.UnaryOperator;

public final class TeamAdminService {
    private TeamAdminService() {
    }

    public static TeamDefinition create(TeamDefinition team) {
        return TeamManager.register(team);
    }

    public static TeamDefinition delete(String teamId) {
        return TeamManager.unregister(teamId);
    }

    public static TeamDefinition update(String teamId, UnaryOperator<TeamDefinition> updater) {
        return TeamManager.update(teamId, updater);
    }

    public static boolean addPlayer(String teamId, UUID playerId) {
        return TeamManager.addPlayer(teamId, playerId);
    }

    public static boolean removePlayer(UUID playerId) {
        return TeamManager.removePlayer(playerId);
    }

    public static String teamOf(UUID playerId) {
        return TeamManager.teamOf(playerId);
    }

    public static Set<UUID> members(String teamId) {
        return TeamManager.members(teamId);
    }

    public static List<TeamDefinition> list() {
        return TeamManager.all();
    }

    public static Map<String, TeamDefinition> snapshot() {
        return TeamManager.snapshot();
    }

    public static void openEditor(String teamId, ConfigurationUiBridge ui) {
        if (ui != null) {
            ui.openTeamEditor(teamId);
        }
    }
}

