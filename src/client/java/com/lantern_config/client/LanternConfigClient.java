package com.lantern_config.client;

import com.lantern_config.LanternScreenOpener;
import com.lantern_config.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

public class LanternConfigClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		LanternScreenOpener.set(LanternModeScreenOpener::open);

		// Default block rendering (the "solid" layer) ignores the alpha channel entirely,
		// which is why the glass overlay was showing up fully opaque instead of see-through.
		// Cutout respects alpha as an on/off mask, matching vanilla's lantern glass.
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LANTERN, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SOUL_LANTERN, RenderType.cutout());
	}
}
