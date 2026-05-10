# GameData

`GameData` is per-instance mutable runtime state.

## Base responsibilities

- Keep session identity via `setupId(...)` / `getId()`.
- Maintain participant -> ECS entity O(1) map:
  - `bindParticipant(UUID)` (allocates ECS entity id internally)
  - `getParticipantEntity(UUID)`
  - `removeParticipant(UUID)`
- Track runtime object copies and registered subsystem resources.
- Register configured subsystem definitions through manager validate/register flow.
- Manage team member sets and team migration rules.
- Perform cleanup in `reset(instance)`.

## Initialization and rollback

- Use `init(config)` as setup entrypoint.
- `init(config)` guarantees rollback:
  - if any registration stage fails, already-registered resources are unregistered.

## Team behavior provided by base class

- Keeps spectator as the default fallback team for no-team join.
- Supports team switching while respecting team limits.
- Prevents removing spectator team.

## Subclass checklist

- Add game-specific runtime fields (scores, rounds, win condition state).
- Bind ECS components/entities when players or mobs enter the game.
- Keep game-specific cleanup logic in fields that are reset-safe.

## Full example

```java
final class ChessData extends GameData {
    private final Map<UUID, Integer> score = new LinkedHashMap<>();

    int bindPlayer(UUID playerId) {
        // No external entityId input: ECS id is created internally.
        int entityId = bindParticipant(playerId);
        score.putIfAbsent(playerId, 0);
        return entityId;
    }

    OptionalInt entityOf(UUID playerId) {
        Integer entityId = getParticipantEntity(playerId);
        return entityId == null ? OptionalInt.empty() : OptionalInt.of(entityId);
    }

    int scoreOf(UUID playerId) {
        return score.getOrDefault(playerId, 0);
    }

    void addScore(UUID playerId, int delta) {
        score.merge(playerId, delta, Integer::sum);
    }
}
```
