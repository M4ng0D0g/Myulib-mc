package com.myudog.myulib.api.game.state;

public final class GameStateContracts {
    private GameStateContracts() {
    }

    public static <S extends Enum<S>> GameStateContext<S> context(net.minecraft.resources.Identifier gameId, int instanceId, S from, S to) {
        return new GameStateContext<>(gameId, instanceId, from, to);
    }

    public static <S extends Enum<S>> GameTransition<S> transition(S from, S to, boolean allowed) {
        return new GameTransition<>(from, to, allowed);
    }
}

