# GameObjectKind

`GameObjectKind` classifies gameplay intent for runtime object handling.

## Enum values
- `LOGIC`
- `MINEABLE`
- `ATTACKABLE`
- `INTERACTABLE`
- `PROXIMITY_TRIGGER`
- `DECORATIVE`
- `CUSTOM`

## Usage
- Use this value when defining object templates in `GameConfig.gameObjects()`.
- Use it in behavior routing when interaction events are processed.
