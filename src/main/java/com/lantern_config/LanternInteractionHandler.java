package com.lantern_config;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Right-clicking a VANILLA lantern/soul lantern swaps it in-place for our
 * ConfigurableLanternBlock (preserving hanging/waterlogged) and opens the
 * mode GUI. Once swapped, later right-clicks are handled normally by
 * ConfigurableLanternBlock#use, so this only ever needs to fire once per block.
 */
public final class LanternInteractionHandler {

	private LanternInteractionHandler() {
	}

	public static void register() {
		UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
			if (hand != InteractionHand.MAIN_HAND || player.isShiftKeyDown()) {
				return InteractionResult.PASS;
			}

			BlockPos pos = hitResult.getBlockPos();
			BlockState state = level.getBlockState(pos);
			Block block = state.getBlock();

			if (block instanceof ConfigurableLanternBlock) {
				// Already converted - let ConfigurableLanternBlock#use handle it untouched.
				return InteractionResult.PASS;
			}

			ConfigurableLanternBlock replacement;
			if (block == Blocks.LANTERN) {
				replacement = ModBlocks.LANTERN;
			} else if (block == Blocks.SOUL_LANTERN) {
				replacement = ModBlocks.SOUL_LANTERN;
			} else {
				return InteractionResult.PASS;
			}

			if (level.isClientSide) {
				LanternScreenOpener.openIfPresent(pos);
			} else {
				BlockState newState = replacement.defaultBlockState()
						.setValue(LanternBlock.HANGING, state.getValue(LanternBlock.HANGING))
						.setValue(LanternBlock.WATERLOGGED, state.getValue(LanternBlock.WATERLOGGED));
				level.setBlock(pos, newState, Block.UPDATE_ALL);
			}

			return InteractionResult.SUCCESS;
		});
	}
}
