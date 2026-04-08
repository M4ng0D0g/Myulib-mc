package com.myudog.myulib.api.game;

import net.minecraft.resources.Identifier;

@Deprecated(forRemoval = false)
public abstract class GameDefinition<S extends Enum<S>> extends com.myudog.myulib.api.game.state.GameDefinition<S> {
	protected GameDefinition(Identifier id) {
		super(id);
	}
}
