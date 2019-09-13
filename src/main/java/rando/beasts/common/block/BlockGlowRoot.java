package rando.beasts.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import rando.beasts.common.init.BeastsBlocks;

@SuppressWarnings("deprecation")
public class BlockGlowRoot extends BeastsBlock {

	private boolean isTop;

	public BlockGlowRoot(boolean top) {
		super(Properties.create(Material.ORGANIC, MaterialColor.GRASS).sound(SoundType.PLANT).lightValue(1)
				.doesNotBlockMovement(), "glow_root_" + (top ? "top" : "bottom"), false, null);
		this.isTop = top;
	}

	private boolean canBlockStay(World worldIn, BlockPos pos) {
		BlockPos up = pos.up();
		if (isTop)
			return worldIn.getBlockState(up).getBlock() != Blocks.AIR;
		return worldIn.getBlockState(up).getBlock() == BeastsBlocks.GLOW_ROOT_TOP;
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
			boolean isMoving) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		this.checkAndDropBlock(worldIn, pos, state);
	}

	@Override
	public void animateTick(BlockState state, World worldIn, BlockPos pos, Random rand) {
		this.checkAndDropBlock(worldIn, pos, state);
	}

	private void checkAndDropBlock(World worldIn, BlockPos pos, BlockState state) {
		if (!this.canBlockStay(worldIn, pos)) {
			Block.spawnDrops(state, worldIn, pos);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		if (isTop) {
			if (player.abilities.isCreativeMode)
				worldIn.removeBlock(pos.down(), false);
			else
				worldIn.destroyBlock(pos.down(), true);
		} else
			worldIn.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 2);
		super.onBlockHarvested(worldIn, pos, state, player);
	}

	@Override
	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
		return false;
	}

	public boolean isOpaqueCube(BlockState state) {
		return false;
	}

	public boolean isFullCube(BlockState state) {
		return false;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return VoxelShapes.empty();
	}

}
