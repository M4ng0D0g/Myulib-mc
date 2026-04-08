package com.myudog.myulib.api.game.state;

import net.minecraft.resources.Identifier;

public record GameStateContext<S extends Enum<S>>(Identifier gameId, int instanceId, S from, S to) {
}



