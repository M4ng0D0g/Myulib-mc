package com.myudog.myulib.api.framework.game.features;

import com.myudog.myulib.api.framework.game.GameInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

public interface TeamFeature {

    @Nullable UUID getParticipantTeam(@NotNull UUID playerId);

    boolean containsParticipant(UUID playerId);

    Set<UUID> participantsOf(@Nullable UUID teamId);

    UUID teamOf(@NotNull UUID playerId);

    int countAllParticipant(@Nullable UUID teamId);

    int countActiveParticipant();

    boolean canJoinTeam(@Nullable UUID teamId, @NotNull UUID playerId);

    boolean moveParticipantToTeam(@Nullable UUID teamId, @NotNull UUID playerId);

    void removeParticipantFromTeams(@NotNull UUID playerId);

    void clean(GameInstance<?, ?, ?> instance);

}
