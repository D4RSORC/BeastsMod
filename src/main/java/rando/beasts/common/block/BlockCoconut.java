package rando.beasts.common.block;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import rando.beasts.common.init.BeastsBlocks;
import rando.beasts.common.init.BeastsItems;
import rando.beasts.common.tileentity.TileEntityCoconut;

@SuppressWarnings("deprecation")
public class BlockCoconut extends FallingBlock {

	private static final VoxelShape SHAPE = Block.makeCuboidShape(0.25, 0, 0.25, 0.75, 0.2, 0.75);

	public BlockCoconut() {
		super(Properties.create(Material.WOOD));
		String name = "coconut";
		setRegistryName(name);
		BeastsBlocks.LIST.add(this);
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof TileEntityCoconut) {
				worldIn.removeTileEntity(pos);
			}

		}
	}

	@Override
	public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity != null && tileentity.receiveClientEvent(id, param);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public boolean isSolid(BlockState state) {
		return false;
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		Random rand = player.getRNG();
		for (int i = 0; i < 4; ++i)
			worldIn.addParticle(new BlockParticleData(ParticleTypes.BLOCK, Blocks.SAND.getDefaultState()),
					hit.getHitVec().x + rand.nextDouble(), hit.getHitVec().y + rand.nextDouble(),
					hit.getHitVec().z + rand.nextDouble(), (rand.nextDouble() - 0.5D) * 0.5D,
					(rand.nextDouble() - 0.5D) * 0.5D, (rand.nextDouble() - 0.5D) * 0.5D);
		removedByPlayer(state, worldIn, pos, player, true, null);
		spawnAsEntity(worldIn, pos, new ItemStack(BeastsItems.COCONUT));
		return true;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityCoconut();
	}
}
