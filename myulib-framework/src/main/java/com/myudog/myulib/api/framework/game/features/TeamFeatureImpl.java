package com.myudog.myulib.api.framework.game.features;

import com.myudog.myulib.api.framework.game.core.GameInstance;
import com.myudog.myulib.api.framework.team.TeamDefinition;
import com.myudog.myulib.api.framework.team.TeamManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TeamFeatureImpl implements TeamFeature {

    private final UUID spectatorTeamId;
    private final Set<UUID> members = ConcurrentHashMap.newKeySet();

    public TeamFeatureImpl(UUID spectatorTeamId) {
        this.spectatorTeamId = spectatorTeamId;
    }

    @Override
    public @Nullable UUID getParticipantTeam(@NotNull UUID playerId) {
        // 確保查詢的玩家真的是這個房間的參與者
        if (!members.contains(playerId)) return null;
        return TeamManager.INSTANCE.getTeamByMember(playerId);
    }

    @Override
    public boolean containsParticipant(UUID playerId) {
        return members.contains(playerId);
    }

    @Override
    public Set<UUID> participantsOf(@Nullable UUID teamId) {
        if (teamId == null) {
            return Set.copyOf(members);
        }

        Set<UUID> globalMembers = TeamManager.INSTANCE.getMembersByTeam(teamId);
        if (globalMembers == null || globalMembers.isEmpty()) return Set.of();

        // 🌟 核心修復：交集過濾。只回傳「全域隊伍中」且「屬於本房間」的玩家
        return globalMembers.stream()
                .filter(members::contains)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public UUID teamOf(@NotNull UUID playerId) {
        return getParticipantTeam(playerId); // 複用已經加了安全檢查的邏輯
    }

    @Override
    public int countAllParticipant(@Nullable UUID teamId) {
        if (teamId == null) return members.size();
        // 🌟 核心修復：直接呼叫 participantsOf 來取得過濾後的大小，避免算到別的房間的人
        return participantsOf(teamId).size();
    }

    @Override
    public int countActiveParticipant() {
        // 🌟 核心修復：只計算「本房間內」且「隊伍不是觀戰者」的玩家
        return (int) members.stream()
                .filter(playerId -> !spectatorTeamId.equals(TeamManager.INSTANCE.getTeamByMember(playerId)))
                .count();
    }

    @Override
    public boolean canJoinTeam(@Nullable UUID teamId, @NotNull UUID playerId) {
        var teamManager = TeamManager.INSTANCE;

        if (teamId == null) teamId = spectatorTeamId;
        if (!teamManager.hasTeam(teamId)) return false;

        UUID current = teamManager.getTeamByMember(playerId);
        if (current != null && current.equals(teamId)) return false;

        TeamDefinition definition = teamManager.getDefinition(teamId);
        if (definition == null) return false;

        // 如果該隊伍沒有人數限制，直接允許
        if (definition.playerLimit() <= 0) return true;

        // 注意：這裡檢查的是該隊伍在「全域」的人數上限，這符合邏輯 (如果該隊伍是跨房間共用的)
        return teamManager.teamSize(teamId) < definition.playerLimit();
    }

    @Override
    public boolean moveParticipantToTeam(@Nullable UUID teamId, @NotNull UUID playerId) {
        if (teamId == null) teamId = spectatorTeamId;
        if (!canJoinTeam(teamId, playerId)) return false;

        removeParticipantFromTeams(playerId);

        TeamManager.INSTANCE.addPlayer(teamId, playerId);
        members.add(playerId);

        return true;
    }

    @Override
    public void removeParticipantFromTeams(@NotNull UUID playerId) {
        if (members.contains(playerId)) {
            TeamManager.INSTANCE.removePlayer(playerId);
            members.remove(playerId);
        }
    }

    @Override
    public void clean(GameInstance<?, ?, ?> instance) {
        // 🌟 實作清理：遊戲結束或房間銷毀時，將本房間所有玩家從全域隊伍中踢出
        for (UUID playerId : members) {
            TeamManager.INSTANCE.removePlayer(playerId);
        }
        // 徹底清空本地追蹤器
        members.clear();
    }
}