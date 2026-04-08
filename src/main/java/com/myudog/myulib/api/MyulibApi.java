package com.myudog.myulib.api;

import com.myudog.myulib.api.field.FieldManager;
import com.myudog.myulib.api.identity.IdentityManager;
import com.myudog.myulib.api.permission.PermissionManager;
import com.myudog.myulib.api.team.TeamManager;
import com.myudog.myulib.api.game.Game;

public final class MyulibApi {
	private MyulibApi() {
	}

	public static void init() {
		Game.init();
		AccessSystems.init();
		FieldManager.install();
		TeamManager.install();
		IdentityManager.install();
		PermissionManager.install();
	}
}
