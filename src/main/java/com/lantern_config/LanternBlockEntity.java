package com.lantern_config;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LanternBlockEntity extends BlockEntity {

	/**
	 * Flicker mode cycles through three phases instead of a flat random toggle every tick:
	 * STEADY - fully lit, holds for a few seconds.
	 * BURST  - a run of abrupt on/dim/frame changes in quick succession.
	 * PAUSE  - light goes out completely for a short moment before the next burst.
	 * Each transition is an instant blockstate change (texture + light snap together, no fading).
	 */
	private enum FlickerPhase {
		STEADY, BURST, PAUSE
	}

	private FlickerPhase phase = FlickerPhase.STEADY;
	private boolean flickerStarted = false;
	private int timer;
	private int burstFlickersLeft;

	public LanternBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.LANTERN_BLOCK_ENTITY, pos, state);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, LanternBlockEntity be) {
		if (state.getValue(ConfigurableLanternBlock.MODE) != LanternMode.FLICKER) {
			be.flickerStarted = false;
			be.phase = FlickerPhase.STEADY;
			be.timer = 0;
			return;
		}

		if (!be.flickerStarted) {
			// Just switched into Flicker mode: hold steady for a bit before the first burst,
			// rather than immediately flickering.
			be.flickerStarted = true;
			be.phase = FlickerPhase.STEADY;
			be.timer = randomSteadyDuration(level);
			return;
		}

		if (be.timer > 0) {
			be.timer--;
			return;
		}

		BlockState newState = state;

		switch (be.phase) {
			case STEADY -> {
				// Steady period over: kick off a burst of rapid flickers.
				be.phase = FlickerPhase.BURST;
				be.burstFlickersLeft = 3 + level.random.nextInt(4); // 3-6 flickers this burst
				newState = randomFlickerBeat(level, state);
				be.timer = 2 + level.random.nextInt(4); // ~0.1-0.3s between beats
			}
			case BURST -> {
				if (be.burstFlickersLeft > 0) {
					newState = randomFlickerBeat(level, state);
					be.burstFlickersLeft--;
					be.timer = 2 + level.random.nextInt(4);
				} else {
					// Burst finished: go fully dark for a short pause.
					be.phase = FlickerPhase.PAUSE;
					newState = state.setValue(ConfigurableLanternBlock.FLICKER_LIT, false);
					be.timer = 20 + level.random.nextInt(20); // 1-2s dark
				}
			}
			case PAUSE -> {
				// Pause over: resume steady, fully lit, until the next burst.
				be.phase = FlickerPhase.STEADY;
				newState = state.setValue(ConfigurableLanternBlock.FLICKER_LIT, true)
						.setValue(ConfigurableLanternBlock.FLAME_FRAME, 0);
				be.timer = randomSteadyDuration(level);
			}
		}

		if (newState != state) {
			level.setBlock(pos, newState, Block.UPDATE_ALL);
		}
	}

	private static int randomSteadyDuration(Level level) {
		return 40 + level.random.nextInt(60); // ~2-5s at 20 ticks/sec
	}

	private static BlockState randomFlickerBeat(Level level, BlockState state) {
		boolean lit = level.random.nextBoolean();
		int frame = level.random.nextInt(3);
		return state.setValue(ConfigurableLanternBlock.FLICKER_LIT, lit)
				.setValue(ConfigurableLanternBlock.FLAME_FRAME, frame);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = super.getUpdateTag();
		saveAdditional(tag);
		return tag;
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
}
