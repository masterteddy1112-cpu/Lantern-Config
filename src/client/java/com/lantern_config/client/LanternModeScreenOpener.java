package com.lantern_config.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public class LanternModeScreenOpener {
	public static void open(BlockPos pos) {
		Minecraft.getInstance().setScreen(new LanternModeScreen(pos));
	}
}