# GameConfig

`GameConfig` defines static game setup data consumed during `init()`.

## Base responsibilities

- Provide object blueprints via `gameObjects()`.
- Provide team alias map via `teams()` (**required**).
- Provide playable team definitions via `additionalTeamDefinitions()` (**required**).
- Provide subsystem definitions via:
  - `fieldDefinitions()`
  - `timerDefinitions()`
  - `roleGroupDefinitions()`
- Implement `validate()` (**required**).

## Validation pattern

- Preferred implementation:
  - `return GameConfig.validateDefinitions(this);`
- This helper performs manager-level checks (`manager.validate(definition)`) and common invariants.

## Current required constraints

- `maxPlayer() > 0`
- `teams()` must contain `spectator` mapped to `myulib:spectator`
- at least one playable non-spectator team is required
- config definitions should use scoped token path identifiers (example: `myulib:chess/white`)

## Scoped token recommendation

- Use `GameScopeTokens.scoped(...)` to generate ids for teams/fields/timers/object definitions.

## Timer placement guidance

- Define **timer blueprints** in `timerDefinitions()`.
- Start/stop **timer instances** in `bindBehavior(...)` (or events registered there).
- Reason: blueprints are static config, but running timers are round-scoped runtime behavior.

## Full example

```java
record ChessConfig(
        Map<Identifier, IGameObject> gameObjects,
        List<FieldDefinition> fields,
        List<TimerDefinition> timers
) implements GameConfig {

    @Override
    public boolean validate() {
        return GameConfig.validateDefinitions(this);
    }

    @Override
    public Map<String, Identifier> teams() {
        return Map.of(
                GameConfig.SPECTATOR_TEAM_KEY, GameConfig.SPECTATOR_TEAM_ID,
                "white", Identifier.fromNamespaceAndPath(Myulib.MOD_ID, "chess/white"),
                "black", Identifier.fromNamespaceAndPath(Myulib.MOD_ID, "chess/black")
        );
    }

    @Override
    public List<TeamDefinition> additionalTeamDefinitions() {
        return List.of(
                new TeamDefinition(Identifier.fromNamespaceAndPath(Myulib.MOD_ID, "chess/white"), Component.literal("White"), TeamColor.WHITE, Map.of(), 16),
                new TeamDefinition(Identifier.fromNamespaceAndPath(Myulib.MOD_ID, "chess/black"), Component.literal("Black"), TeamColor.BLACK, Map.of(), 16)
        );
    }

    @Override
    public List<FieldDefinition> fieldDefinitions() {
        return fields;
    }

    @Override
    public List<TimerDefinition> timerDefinitions() {
        return timers;
    }
}
```
