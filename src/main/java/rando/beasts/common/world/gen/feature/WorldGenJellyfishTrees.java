package rando.beasts.common.world.gen.feature;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import rando.beasts.common.block.BlockTentacle;
import rando.beasts.common.init.BeastsBlocks;

public class WorldGenJellyfishTrees extends AbstractTreeFeature<NoFeatureConfig> {

	public WorldGenJellyfishTrees(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i49920_1_,
			boolean doBlockNofityOnPlace) {
		super(p_i49920_1_, doBlockNofityOnPlace);
	}

	private static final BlockState LOG = BeastsBlocks.JELLY_WOOD.getDefaultState();
	private static final BlockState LEAF = BeastsBlocks.JELLY_LEAVES.getDefaultState();

	@Override
	public boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader worldIn, Random rand, BlockPos position, MutableBoundingBox boundsIn) {
		if (worldIn.hasBlockState(position, (state) -> state.getBlock() == Blocks.SAND)) {
			int height = rand.nextInt(3) + 4;
			int radius = rand.nextInt(5) + 8;
			if (radius % 2 == 0)
				radius += 1;
			int hs = height - Math.abs(((radius - height) / 2) - 2) - 1;
			BlockPos pos = position;
			for (int i = 0; i < height + 1; i++) {
				setBlock(worldIn, rand, pos, LOG);
				pos = pos.up();
			}

			position = position.add(radius / -2, 0, radius / -2);
			int h;
			for (int i = 0; i < 2; i++) {
				h = hs + i;
				for (int j = 2; j < radius - 2; j++) {
					setBlockWithTentacle(worldIn, rand, position.add(j, h, 0), LEAF);
					setBlockWithTentacle(worldIn, rand, position.add(j, h, radius - 1), LEAF);
					setBlockWithTentacle(worldIn, rand, position.add(0, h, j), LEAF);
					setBlockWithTentacle(worldIn, rand, position.add(radius - 1, h, j), LEAF);
				}
				setBlockWithTentacle(worldIn, rand, position.add(radius - 2, h, radius - 2), LEAF);
				setBlockWithTentacle(worldIn, rand, position.add(1, h, radius - 2), LEAF);
				setBlockWithTentacle(worldIn, rand, position.add(radius - 2, h, 1), LEAF);
				setBlockWithTentacle(worldIn, rand, position.add(1, h, 1), LEAF);
			}

			h = hs + 2;
			for (int i = 2; i < radius - 2; i++) {
				setBlock(worldIn, rand, position.add(i, h, 1), LEAF);
				setBlock(worldIn, rand, position.add(i, h, radius - 2), LEAF);
				setBlock(worldIn, rand, position.add(1, h, i), LEAF);
				setBlock(worldIn, rand, position.add(radius - 2, h, i), LEAF);
			}
			setBlock(worldIn, rand, position.add(radius - 3, h, radius - 3), LEAF);
			setBlock(worldIn, rand, position.add(2, h, radius - 3), LEAF);
			setBlock(worldIn, rand, position.add(radius - 3, h, 2), LEAF);
			setBlock(worldIn, rand, position.add(2, h, 2), LEAF);
			h++;
			for (int i = 3; i < radius - 3; i++) {
				setBlock(worldIn, rand, position.add(i, h, 2), LEAF);
				setBlock(worldIn, rand, position.add(i, h, radius - 3), LEAF);
				setBlock(worldIn, rand, position.add(2, h, i), LEAF);
				setBlock(worldIn, rand, position.add(radius - 3, h, i), LEAF);
			}
			for (int i = 3; i < radius - 3; i++)
				for (int j = 3; j < radius - 3; j++) {
					BlockPos p = position.add(i, h, j);
					setBlock(worldIn, rand, p, LEAF);
				}

			return true;
		}
		return false;
	}

	private void generateTentacles(BlockPos pos, IWorldGenerationReader world, Random rand) {
		BlockPos lower = pos.down();
		if (world.hasBlockState(lower, (state) -> state.getBlock() == Blocks.AIR) && rand.nextInt(3) == 0) {
			int size = rand.nextInt(16) + 1;
			boolean full = size > 8;
			BlockState state = BeastsBlocks.TENTACLE.getDefaultState();
			setBlockState(world, lower,
					full ? state.with(BlockTentacle.FULL, true) : state.with(BlockTentacle.SIZE, 8));
			setBlockState(world, lower.down(),
					full ? state.with(BlockTentacle.FULL, true) : state.with(BlockTentacle.SIZE, size));
			if (full)
				setBlockState(world, lower.down().down(), state.with(BlockTentacle.SIZE, size - 8));
		}
	}

	@Override
	protected void setBlockState(IWorldWriter worldIn, BlockPos pos, BlockState state) {
		super.setBlockState(worldIn, pos, state);
	}

	private void setBlockWithTentacle(IWorldGenerationReader worldIn, Random rand, BlockPos pos, BlockState state) {
		this.setBlockWithTentacle(worldIn, rand, pos, state, false);
	}

	private void setBlock(IWorldGenerationReader worldIn, Random rand, BlockPos pos, BlockState state) {
		this.setBlockWithTentacle(worldIn, rand, pos, state, state.getBlock() == BeastsBlocks.JELLY_LEAVES);
	}

	private void setBlockWithTentacle(IWorldGenerationReader worldIn, Random rand, BlockPos pos, BlockState state,
			boolean tentacle) {
		super.setBlockState(worldIn, pos, state);
		if (tentacle)
			generateTentacles(pos, worldIn, rand);
	}
}
