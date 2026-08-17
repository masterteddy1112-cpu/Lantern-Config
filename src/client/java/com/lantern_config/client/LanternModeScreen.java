package com.lantern_config.client;

import com.lantern_config.LanternMode;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class LanternModeScreen extends Screen {
	private static final int BUTTON_WIDTH = 60;
	private static final int BUTTON_HEIGHT = 20;
	private static final int BUTTON_GAP = 8;
	private static final int MARGIN_LEFT = 20;
	private static final int MARGIN_TOP = 20;

	private final BlockPos pos;

	public LanternModeScreen(BlockPos pos) {
		super(Component.translatable("gui.lanternconfig.title"));
		this.pos = pos;
	}

	@Override
	protected void init() {
		super.init();

		addModeButton(MARGIN_LEFT, LanternMode.DEFAULT, "gui.lanternconfig.mode.default");
		addModeButton(MARGIN_LEFT + (BUTTON_WIDTH + BUTTON_GAP), LanternMode.FLICKER, "gui.lanternconfig.mode.flicker");
		addModeButton(MARGIN_LEFT + (BUTTON_WIDTH + BUTTON_GAP) * 2, LanternMode.OFF, "gui.lanternconfig.mode.off");
	}

	private void addModeButton(int x, LanternMode mode, String translationKey) {
		Component label = Component.translatable(translationKey);
		Button button = Button.builder(label, b -> {
			ModNetworkingClient.sendModeUpdate(pos, mode);
			this.onClose();
		}).bounds(x, MARGIN_TOP, BUTTON_WIDTH, BUTTON_HEIGHT).build();
		button.setTooltip(Tooltip.create(Component.translatable(translationKey + ".tooltip")));
		this.addRenderableWidget(button);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		// Dims the world behind the menu, same as chest/inventory-style screens.
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTick);
		guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 6, 0xFFFFFF);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
