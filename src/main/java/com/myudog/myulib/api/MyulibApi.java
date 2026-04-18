package com.myudog.myulib.api;

import com.myudog.myulib.api.control.ControlManager;
import com.myudog.myulib.api.debug.DebugLogManager;
import com.myudog.myulib.api.field.FieldManager;
import com.myudog.myulib.api.field.FieldVisualizationManager;
import com.myudog.myulib.api.hologram.network.HologramNetworking;
import com.myudog.myulib.api.game.core.GameManager;
import com.myudog.myulib.api.game.examples.TicTacToeGameDefinition;
import com.myudog.myulib.api.rolegroup.RoleGroupManager;
import com.myudog.myulib.api.camera.CameraApi;
import com.myudog.myulib.api.permission.PermissionManager;
import com.myudog.myulib.api.team.TeamManager;
import com.myudog.myulib.api.timer.TimerManager;

public final class MyulibApi {
	private MyulibApi() {
	}

	public static void init() {
		AccessSystems.init();
		DebugLogManager.install();

		CameraApi.initServer();
		ControlManager.install();
		HologramNetworking.registerPayloads();

		FieldManager.install();
		FieldVisualizationManager.install();
		GameManager.install();
		GameManager.register(new TicTacToeGameDefinition(TicTacToeGameDefinition.GAME_ID));
		PermissionManager.install();
		RoleGroupManager.install();
		TeamManager.install();
		TimerManager.install();
	}
}
