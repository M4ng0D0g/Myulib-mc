package com.myudog.myulib.api.game.core;

import com.myudog.myulib.api.game.state.GameState;

public interface GameBehavior<C extends GameConfig, D extends GameData, S extends GameState> {
    void onBind(GameInstance<C, D, S> instance);

    default void onUnbind(GameInstance<C, D, S> instance) {
    }
}

