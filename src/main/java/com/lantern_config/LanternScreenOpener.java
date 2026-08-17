package com.lantern_config;

import net.minecraft.core.BlockPos;

/**
 * Bridge interface so main-sourceset code (server-agnostic logic) can trigger
 * a client-only screen without directly referencing the client sourceset,
 * which Loom does not allow main to compile against.
 *
 * The client entrypoint sets {@link #INSTANCE} during onInitializeClient().
 */
public interface LanternScreenOpener {
	LanternScreenOpener[] INSTANCE = new LanternScreenOpener[1];

	void open(BlockPos pos);

	static void set(LanternScreenOpener opener) {
		INSTANCE[0] = opener;
	}

	static void openIfPresent(BlockPos pos) {
		if (INSTANCE[0] != null) {
			INSTANCE[0].open(pos);
		}
	}
}
