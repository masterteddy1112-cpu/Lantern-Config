package com.lantern_config;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
	public static BlockEntityType<LanternBlockEntity> LANTERN_BLOCK_ENTITY;

	public static void register() {
		LANTERN_BLOCK_ENTITY = Registry.register(
				BuiltInRegistries.BLOCK_ENTITY_TYPE,
				LanternConfig.id("lantern_block_entity"),
				FabricBlockEntityTypeBuilder.create(LanternBlockEntity::new,
						ModBlocks.LANTERN, ModBlocks.SOUL_LANTERN).build()
		);
	}
}