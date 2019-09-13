package rando.beasts.common.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import rando.beasts.common.init.BeastsBlocks;

@SuppressWarnings("deprecation")
public class BlockCoralSapling extends BeastsSapling {

	private final BlockCoralPlant coralPlant;
	private final CoralColor color;

	public BlockCoralSapling(CoralColor color) {
		super("coral_sapling_" + color.getName(), BlockItem::new);
		this.coralPlant = BeastsBlocks.CORAL_PLANTS.get(color);
		this.color = color;
	}

	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		if (!state.isValidPosition(worldIn, pos)) {
			worldIn.destroyBlock(pos, true);
		} else {
			BlockPos blockpos = pos.up();
			if (worldIn.isAirBlock(blockpos) && blockpos.getY() < worldIn.getDimension().getHeight()) {
				int i = state.get(STAGE);
				if (i < 1 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, blockpos, state, true)) {
					boolean flag = false;
					boolean flag1 = false;
					BlockState blockstate = worldIn.getBlockState(pos.down());
					Block block = blockstate.getBlock();
					if (block == Blocks.SAND) {
						flag = true;
					} else if (block == this.coralPlant) {
						int j = 1;

						for (int k = 0; k < 4; ++k) {
							Block block1 = worldIn.getBlockState(pos.down(j + 1)).getBlock();
							if (block1 != this.coralPlant) {
								if (block1 == Blocks.SAND) {
									flag1 = true;
								}
								break;
							}

							++j;
						}

						if (j < 2 || j <= random.nextInt(flag1 ? 5 : 4)) {
							flag = true;
						}
					} else if (blockstate.isAir(worldIn, pos.down())) {
						flag = true;
					}

					if (flag && areAllNeighborsEmpty(worldIn, blockpos, (Direction) null)
							&& worldIn.isAirBlock(pos.up(2))) {
						worldIn.setBlockState(pos, this.coralPlant.makeConnections(worldIn, pos), 2);
						this.placeGrownFlower(worldIn, blockpos, i);
					} else if (i < 4) {
						int l = random.nextInt(4);
						if (flag1) {
							++l;
						}

						boolean flag2 = false;

						for (int i1 = 0; i1 < l; ++i1) {
							Direction direction = Direction.Plane.HORIZONTAL.random(random);
							BlockPos blockpos1 = pos.offset(direction);
							if (worldIn.isAirBlock(blockpos1) && worldIn.isAirBlock(blockpos1.down())
									&& areAllNeighborsEmpty(worldIn, blockpos1, direction.getOpposite())) {
								this.placeGrownFlower(worldIn, blockpos1, i + 1);
								flag2 = true;
							}
						}

						if (flag2) {
							worldIn.setBlockState(pos, this.coralPlant.makeConnections(worldIn, pos), 2);
						} else {
							this.placeDeadFlower(worldIn, pos);
						}
					} else {
						this.placeDeadFlower(worldIn, pos);
					}
					net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
				}
			}
		}
	}

	private void placeGrownFlower(World worldIn, BlockPos pos, int age) {
		worldIn.setBlockState(pos, this.getDefaultState().with(STAGE, Integer.valueOf(age)), 2);
		worldIn.playEvent(1033, pos, 0);
	}

	private void placeDeadFlower(World worldIn, BlockPos pos) {
		worldIn.setBlockState(pos, this.getDefaultState().with(STAGE, 1), 2);
		worldIn.playEvent(1034, pos, 0);
	}

	private static boolean areAllNeighborsEmpty(IWorldReader worldIn, BlockPos pos, @Nullable Direction excludingSide) {
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			if (direction != excludingSide && !worldIn.isAirBlock(pos.offset(direction))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Update the provided state given the provided neighbor facing and neighbor
	 * state, returning a new state. For example, fences make their connections to
	 * the passed in state if possible, and wet concrete powder immediately returns
	 * its solidified counterpart. Note that this method should ideally consider
	 * only the specific face passed in.
	 */
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) {
		if (facing != Direction.UP && !stateIn.isValidPosition(worldIn, currentPos)) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
		}

		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockState blockstate = worldIn.getBlockState(pos.down());
		Block block = blockstate.getBlock();
		if (block != this.coralPlant && block != Blocks.SAND) {
			if (!blockstate.isAir(worldIn, pos.down())) {
				return false;
			} else {
				boolean flag = false;

				for (Direction direction : Direction.Plane.HORIZONTAL) {
					BlockState blockstate1 = worldIn.getBlockState(pos.offset(direction));
					if (blockstate1.getBlock() == this.coralPlant) {
						if (flag) {
							return false;
						}

						flag = true;
					} else if (!blockstate1.isAir(worldIn, pos.offset(direction))) {
						return false;
					}
				}

				return flag;
			}
		} else {
			return true;
		}
	}

	/**
	 * Gets the render layer this block will render on. SOLID for solid blocks,
	 * CUTOUT or CUTOUT_MIPPED for on-off transparency (glass, reeds), TRANSLUCENT
	 * for fully blended transparency (stained glass)
	 */
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	public void grow(IWorld worldIn, BlockPos pos, Random rand) {
		generateTree(worldIn.getWorld(), pos, this.getDefaultState(), rand);
	}

	@Override
	protected void generateTree(World worldIn, BlockPos pos, BlockState state, Random rand) {
		worldIn.setBlockState(pos,
				BeastsBlocks.CORAL_PLANTS.get(this.color).makeConnections(worldIn, pos), 2);
		growTreeRecursive(worldIn, pos, rand, pos, 8, 0);
	}

	private void growTreeRecursive(IWorld worldIn, BlockPos p_185601_1_, Random rand, BlockPos p_185601_3_,
			int p_185601_4_, int p_185601_5_) {
		BlockCoralPlant coralplantblock = BeastsBlocks.CORAL_PLANTS.get(this.color);
		int i = rand.nextInt(4) + 1;
		if (p_185601_5_ == 0) {
			++i;
		}

		for (int j = 0; j < i; ++j) {
			BlockPos blockpos = p_185601_1_.up(j + 1);
			if (!areAllNeighborsEmpty(worldIn, blockpos, (Direction) null)) {
				return;
			}

			worldIn.setBlockState(blockpos, coralplantblock.makeConnections(worldIn, blockpos), 2);
			worldIn.setBlockState(blockpos.down(), coralplantblock.makeConnections(worldIn, blockpos.down()), 2);
		}

		boolean flag = false;
		if (p_185601_5_ < 4) {
			int l = rand.nextInt(4);
			if (p_185601_5_ == 0) {
				++l;
			}

			for (int k = 0; k < l; ++k) {
				Direction direction = Direction.Plane.HORIZONTAL.random(rand);
				BlockPos blockpos1 = p_185601_1_.up(i).offset(direction);
				if (Math.abs(blockpos1.getX() - p_185601_3_.getX()) < p_185601_4_
						&& Math.abs(blockpos1.getZ() - p_185601_3_.getZ()) < p_185601_4_
						&& worldIn.isAirBlock(blockpos1) && worldIn.isAirBlock(blockpos1.down())
						&& areAllNeighborsEmpty(worldIn, blockpos1, direction.getOpposite())) {
					flag = true;
					worldIn.setBlockState(blockpos1, coralplantblock.makeConnections(worldIn, blockpos1), 2);
					worldIn.setBlockState(blockpos1.offset(direction.getOpposite()),
							coralplantblock.makeConnections(worldIn, blockpos1.offset(direction.getOpposite())), 2);
					growTreeRecursive(worldIn, blockpos1, rand, p_185601_3_, p_185601_4_, p_185601_5_ + 1);
				}
			}
		}

		if (!flag) {
			worldIn.setBlockState(p_185601_1_.up(i),
					BeastsBlocks.CORAL_SAPLINGS.get(this.color).getDefaultState().with(STAGE, 1), 2);
		}

	}

	@Override
	public void onProjectileCollision(World worldIn, BlockState state, BlockRayTraceResult hit, Entity projectile) {
		BlockPos blockpos = hit.getPos();
		spawnAsEntity(worldIn, blockpos, new ItemStack(this));
		worldIn.destroyBlock(blockpos, true);
	}
}
