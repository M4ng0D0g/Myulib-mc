package com.myudog.myulib.api.game;

import com.myudog.myulib.api.game.components.ComponentManager;
import com.myudog.myulib.api.game.timer.TimerManager;

public final class Game {
    private Game() {
    }

    public static void init() {
        GameManager.install();
        ComponentManager.install();
        TimerManager.install();
    }
}
