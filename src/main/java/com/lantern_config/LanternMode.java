package com.lantern_config;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum LanternMode implements StringRepresentable {
	DEFAULT,
	FLICKER,
	OFF;

	public static LanternMode byId(int id) {
		LanternMode[] values = values();
		return id >= 0 && id < values.length ? values[id] : DEFAULT;
	}

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}
}
