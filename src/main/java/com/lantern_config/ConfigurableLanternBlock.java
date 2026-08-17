package com.lantern_config;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class ConfigurableLanternBlock extends LanternBlock implements EntityBlock {

	public static final EnumProperty<LanternMode> MODE = EnumProperty.create("mode", LanternMode.class);
	// Only meaningful while MODE == FLICKER: whether the flame is currently in its "bright" beat.
	public static final BooleanProperty FLICKER_LIT = BooleanProperty.create("flicker_lit");
	// Only meaningful while FLICKER_LIT == true: which of the 3 flame textures to show, for visual variety.
	public static final IntegerProperty FLAME_FRAME = IntegerProperty.create("flame_frame", 0, 2);

	public ConfigurableLanternBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(HANGING, false)
				.setValue(WATERLOGGED, false)
				.setValue(MODE, LanternMode.DEFAULT)
				.setValue(FLICKER_LIT, true)
				.setValue(FLAME_FRAME, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(MODE, FLICKER_LIT, FLAME_FRAME);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new LanternBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (level.isClientSide || type != ModBlockEntities.LANTERN_BLOCK_ENTITY) {
			return null;
		}
		return (lvl, pos, st, be) -> LanternBlockEntity.serverTick(lvl, pos, st, (LanternBlockEntity) be);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!level.isClientSide) {
			return InteractionResult.CONSUME;
		}
		LanternScreenOpener.openIfPresent(pos);
		return InteractionResult.SUCCESS;
	}
}
