package rando.beasts.common.block;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rando.beasts.common.init.BeastsBlocks;
import rando.beasts.common.init.BeastsItems;
import rando.beasts.common.tileentity.TileEntityCoconut;

@SuppressWarnings("deprecation")
public class BlockCoconut extends ContainerBlock {

	private static final VoxelShape SHAPE = Block.makeCuboidShape(0.1, 0, 0.25, 0.6, 0.2, 0.75);

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

public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
    worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(worldIn));
 }

 public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
    worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, this.tickRate(worldIn));
    return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
 }

 public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
    if (!worldIn.isRemote) {
       this.checkFallable(worldIn, pos);
    }

 }

 private void checkFallable(World worldIn, BlockPos pos) {
    if (worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down())) && pos.getY() >= 0) {
       if (!worldIn.isRemote) {
          FallingBlockEntity fallingblockentity = new FallingBlockEntity(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos));
          this.onStartFalling(fallingblockentity);
          worldIn.addEntity(fallingblockentity);
       }

    }
 }

 protected void onStartFalling(FallingBlockEntity fallingEntity) {
 }

 public int tickRate(IWorldReader worldIn) {
    return 2;
 }

 public static boolean canFallThrough(BlockState state) {
    Block block = state.getBlock();
    Material material = state.getMaterial();
    return state.isAir() || block == Blocks.FIRE || material.isLiquid() || material.isReplaceable();
 }

 @OnlyIn(Dist.CLIENT)
 public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    if (rand.nextInt(16) == 0) {
       BlockPos blockpos = pos.down();
       if (worldIn.isAirBlock(blockpos) || canFallThrough(worldIn.getBlockState(blockpos))) {
          double d0 = (double)((float)pos.getX() + rand.nextFloat());
          double d1 = (double)pos.getY() - 0.05D;
          double d2 = (double)((float)pos.getZ() + rand.nextFloat());
          worldIn.addParticle(new BlockParticleData(ParticleTypes.FALLING_DUST, stateIn), d0, d1, d2, 0.0D, 0.0D, 0.0D);
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
		spawnDrops(state, worldIn, pos, null);
		spawnAsEntity(worldIn, pos, new ItemStack(BeastsItems.COCONUT));
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new TileEntityCoconut();
	}
}
