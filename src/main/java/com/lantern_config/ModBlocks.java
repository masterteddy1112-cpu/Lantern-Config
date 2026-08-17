package com.lantern_config;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class ModBlocks {
	public static final ConfigurableLanternBlock LANTERN = register("lantern",
			new ConfigurableLanternBlock(BlockBehaviour.Properties.of()
					.mapColor(MapColor.METAL)
					.forceSolidOn()
					.lightLevel(ModBlocks::lanternLight)
					.sound(SoundType.LANTERN)
					.strength(3.5F, 3.5F)
					.noOcclusion()));

	public static final ConfigurableLanternBlock SOUL_LANTERN = register("soul_lantern",
			new ConfigurableLanternBlock(BlockBehaviour.Properties.of()
					.mapColor(MapColor.METAL)
					.forceSolidOn()
					.lightLevel(ModBlocks::soulLanternLight)
					.sound(SoundType.LANTERN)
					.strength(3.5F, 3.5F)
					.noOcclusion()));

	private static int lanternLight(net.minecraft.world.level.block.state.BlockState state) {
		LanternMode mode = state.getValue(ConfigurableLanternBlock.MODE);
		if (mode == LanternMode.OFF) return 0;
		if (mode == LanternMode.FLICKER) return state.getValue(ConfigurableLanternBlock.FLICKER_LIT) ? 15 : 0;
		return 15;
	}

	private static int soulLanternLight(net.minecraft.world.level.block.state.BlockState state) {
		LanternMode mode = state.getValue(ConfigurableLanternBlock.MODE);
		if (mode == LanternMode.OFF) return 0;
		if (mode == LanternMode.FLICKER) return state.getValue(ConfigurableLanternBlock.FLICKER_LIT) ? 10 : 0;
		return 10;
	}

	private static <T extends Block> T register(String path, T block) {
		Registry.register(BuiltInRegistries.BLOCK, LanternConfig.id(path), block);
		Registry.register(BuiltInRegistries.ITEM, LanternConfig.id(path), new BlockItem(block, new Item.Properties()));
		return block;
	}

	public static void register() {
		// forces static init of the fields above
	}
}