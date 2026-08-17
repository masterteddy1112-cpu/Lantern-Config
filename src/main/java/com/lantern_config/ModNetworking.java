package com.lantern_config;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ModNetworking {
	public static final ResourceLocation SET_MODE_PACKET = LanternConfig.id("set_mode");

	public static void registerServerReceivers() {
		ServerPlayNetworking.registerGlobalReceiver(SET_MODE_PACKET, (server, player, handler, buf, responseSender) -> {
			BlockPos pos = buf.readBlockPos();
			int modeId = buf.readVarInt();
			server.execute(() -> {
				Level level = player.level();
				if (!level.isLoaded(pos)) {
					return;
				}
				BlockState state = level.getBlockState(pos);
				if (state.getBlock() instanceof ConfigurableLanternBlock) {
					LanternMode mode = LanternMode.byId(modeId);
					// Updating the blockstate (not just block-entity NBT) is what makes
					// Minecraft actually relight and re-render the block.
					level.setBlock(pos, state.setValue(ConfigurableLanternBlock.MODE, mode), Block.UPDATE_ALL);
				}
			});
		});
	}
}
