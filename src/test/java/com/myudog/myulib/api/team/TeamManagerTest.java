package com.myudog.myulib.api.team;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.junit.jupiter.api.Test;
import java.util.EnumMap;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
final class TeamManagerTest {
    @Test
    void teamIdsCanBeScopedByGameAndRemovedWithGameCleanup() {
        TeamManager.clear();
        Identifier gameId = Identifier.fromNamespaceAndPath("myulib", "respawn_game");
        TeamDefinition team = new TeamDefinition(
                Identifier.fromNamespaceAndPath("myulib", "blue"),
                Component.translatable("myulib.test.team.blue"),
                TeamColor.BLUE,
                new EnumMap<>(TeamFlag.class),
                1
        );
        TeamDefinition scoped = TeamManager.register(gameId, team);
        assertEquals(Identifier.fromNamespaceAndPath("myulib", "respawn_game_blue"), scoped.id(), "Scoped team id should include the game path prefix");
        assertEquals(1, TeamManager.all(gameId).size(), "Exactly one team should be registered for the game");
        assertEquals(scoped, TeamManager.get(scoped.id()), "Registered scoped team should be retrievable");
        UUID playerId = UUID.fromString("00000000-0000-0000-0000-000000000999");
        assertTrue(TeamManager.addPlayer(scoped.id(), playerId), "Adding the player to the team should succeed");
        UUID secondPlayer = UUID.fromString("00000000-0000-0000-0000-000000000998");
        assertFalse(TeamManager.addPlayer(scoped.id(), secondPlayer), "Team should reject players after reaching playerLimit");
        assertTrue(TeamManager.members(scoped.id()).contains(playerId), "Player should appear in the team member set");
        TeamManager.forEachMember(scoped.id(), member -> assertEquals(playerId, member, "forEachMember should visit the assigned player"));
        assertEquals(1, TeamManager.unregisterGame(gameId).size(), "Game cleanup should remove exactly one scoped team");
        assertNull(TeamManager.get(scoped.id()), "Removed team should no longer be retrievable");
        assertNull(TeamManager.teamOf(playerId), "Removed player should no longer belong to a team");
    }

    @Test
    void playerLimitZeroMeansUnlimited() {
        TeamManager.clear();
        Identifier teamId = Identifier.fromNamespaceAndPath("myulib", "unlimited");
        TeamManager.register(new TeamDefinition(
                teamId,
                Component.translatable("myulib.test.team.unlimited"),
                TeamColor.BLUE,
                new EnumMap<>(TeamFlag.class),
                0
        ));

        UUID a = UUID.fromString("00000000-0000-0000-0000-000000000901");
        UUID b = UUID.fromString("00000000-0000-0000-0000-000000000902");
        UUID c = UUID.fromString("00000000-0000-0000-0000-000000000903");
        assertTrue(TeamManager.addPlayer(teamId, a));
        assertTrue(TeamManager.addPlayer(teamId, b));
        assertTrue(TeamManager.addPlayer(teamId, c));
    }
}
