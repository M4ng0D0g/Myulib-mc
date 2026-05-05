# Game State Contracts

State runtime is represented by:
- `GameState`
- `GameStateMachine`
- `BasicGameStateMachine`
- `GameStateChangeEvent`

## Runtime behavior
- Initial state `onEnter` is called when `GameInstance` is created/reset.
- `GameInstance.transition(to)` enforces `canTransition` and dispatches `GameStateChangeEvent`.
- `GameInstance.clean()` resets the state machine back to initial state.

## Subclass guidance
- Define transitions in `GameDefinition.createStateMachine(config)`.
- Trigger state changes from `onStart`, game behaviors, or event listeners.
- Keep irreversible round-complete transitions near `onEnd` or final scoring logic.
