package com.myudog.myulib.api;

import com.myudog.myulib.api.core.control.ControlManager;
import com.myudog.myulib.api.core.debug.DebugLogManager;
import com.myudog.myulib.api.framework.field.FieldManager;
import com.myudog.myulib.api.framework.field.FieldVisualizationManager;
import com.myudog.myulib.api.core.hologram.network.HologramNetworking;
import com.myudog.myulib.api.framework.game.core.GameManager;
import com.myudog.myulib.api.framework.rolegroup.RoleGroupManager;
import com.myudog.myulib.api.core.camera.CameraApi;
import com.myudog.myulib.api.framework.permission.PermissionManager;
import com.myudog.myulib.api.framework.team.TeamManager;
import com.myudog.myulib.api.core.timer.TimerManager;
import com.myudog.myulib.api.core.ui.network.ConfigUiNetworking;

public final class MyulibApi {
	private MyulibApi() {
	}

	public static void init() {
		AccessSystems.init();
		DebugLogManager.INSTANCE.install();

		CameraApi.initServer();
		ControlManager.INSTANCE.install();
		HologramNetworking.registerPayloads();
		ConfigUiNetworking.registerPayloads();
		ConfigUiNetworking.registerServerReceivers();

		FieldManager.INSTANCE.install();
		FieldVisualizationManager.INSTANCE.install();
		GameManager.INSTANCE.install();
		// TicTacToeGameDefinition removed - register custom game definitions as needed
		PermissionManager.INSTANCE.install();
		RoleGroupManager.INSTANCE.install();
		TeamManager.INSTANCE.install();
		TimerManager.INSTANCE.install();
	}
}
