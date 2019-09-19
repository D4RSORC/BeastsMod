package rando.beasts.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SixWayBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import rando.beasts.common.init.BeastsBlocks;
import rando.beasts.common.utils.BeastsUtil;

@SuppressWarnings("deprecation")
public class BlockCoralPlant extends SixWayBlock {
	private static final BooleanProperty NORTH = BlockStateProperties.NORTH;
	private static final BooleanProperty EAST = BlockStateProperties.EAST;
	private static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
	private static final BooleanProperty WEST = BlockStateProperties.WEST;
	private static final BooleanProperty UP = BlockStateProperties.UP;
	private static final BooleanProperty DOWN = BlockStateProperties.DOWN;
	private CoralColor color;

	public BlockCoralPlant(CoralColor color) {
		super(0.3125F,
				Properties.create(Material.PLANTS, color.mapColor).hardnessAndResistance(2.0F).sound(SoundType.PLANT));
		this.color = color;
		this.setDefaultState(this.stateContainer.getBaseState().with(NORTH, false).with(EAST, false).with(SOUTH, false)
				.with(WEST, false).with(UP, false).with(DOWN, false));
		BeastsUtil.addToRegistry(this, "coral_plant_" + color.getName(), false, null);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.makeConnections(context.getWorld(), context.getPos());
	}

	public BlockState makeConnections(IBlockReader worldIn, BlockPos pos) {
		Block block = worldIn.getBlockState(pos.down()).getBlock();
		Block block1 = worldIn.getBlockState(pos.up()).getBlock();
		Block block2 = worldIn.getBlockState(pos.north()).getBlock();
		Block block3 = worldIn.getBlockState(pos.east()).getBlock();
		Block block4 = worldIn.getBlockState(pos.south()).getBlock();
		Block block5 = worldIn.getBlockState(pos.west()).getBlock();
		return this.getDefaultState().with(DOWN, block == this || block instanceof BlockCoral || block == Blocks.SAND)
				.with(UP, block1 == this || block1 instanceof BlockCoral)
				.with(NORTH, block2 == this || block2 instanceof BlockCoral)
				.with(EAST, block3 == this || block3 instanceof BlockCoral)
				.with(SOUTH, block4 == this || block4 instanceof BlockCoral)
				.with(WEST, block5 == this || block5 instanceof BlockCoral);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		state = state.getExtendedState(worldIn, pos);
		float f = 0.1875F;
		float f1 = state.get(WEST) ? 0.0F : f;
		float f2 = state.get(DOWN) ? 0.0F : f;
		float f3 = state.get(NORTH) ? 0.0F : f;
		float f4 = state.get(EAST) ? 1.0F : 0.8125F;
		float f5 = state.get(UP) ? 1.0F : 0.8125F;
		float f6 = state.get(SOUTH) ? 1.0F : 0.8125F;
		return VoxelShapes.create(f1, f2, f3, f4, f5, f6);
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) {
		if (!stateIn.isValidPosition(worldIn, currentPos)) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
			return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		} else {
			Block block = facingState.getBlock();
			boolean flag = block == this || BeastsBlocks.CORAL_SAPLINGS.containsValue(block)
					|| facing == Direction.DOWN && block == Blocks.SAND;
			return stateIn.with(FACING_TO_PROPERTY_MAP.get(facing), Boolean.valueOf(flag));
		}
	}

	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		if (!state.isValidPosition(worldIn, pos)) {
			worldIn.destroyBlock(pos, true);
		}

	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockState blockstate = worldIn.getBlockState(pos.down());
		boolean flag = !worldIn.getBlockState(pos.up()).isAir() && !blockstate.isAir();

		for (Direction direction : Direction.Plane.HORIZONTAL) {
			BlockPos blockpos = pos.offset(direction);
			Block block = worldIn.getBlockState(blockpos).getBlock();
			if (block == this) {
				if (flag) {
					return false;
				}

				Block block1 = worldIn.getBlockState(blockpos.down()).getBlock();
				if (block1 == this || block1 == Blocks.SAND) {
					return true;
				}
			}
		}

		Block block2 = blockstate.getBlock();
		return block2 == this || block2 == Blocks.SAND;
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
	}

	@Override
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}
	
	public CoralColor getColor() {
		return this.color;
	}
}
