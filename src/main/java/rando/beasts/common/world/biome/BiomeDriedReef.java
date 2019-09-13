package rando.beasts.common.world.biome;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import rando.beasts.common.block.CoralColor;
import rando.beasts.common.init.BeastsBlocks;
import rando.beasts.common.world.gen.feature.WorldGenJellyfishTrees;

public class BiomeDriedReef extends BeastsBiome {

	private static final WorldGenBlob ROCK_GENERATOR = new WorldGenBlob(Blocks.STONE.getDefaultState());
	private static final WorldGenBlob ANDESITE_GENERATOR = new WorldGenBlob(Blocks.ANDESITE.getDefaultState());
	private static final WorldGenBlob CORAL_BLOCK_GENERATOR = new WorldGenCoralBlock();
	private static final WorldGenCoralPlant CORAL_PLANT_GENERATOR = new WorldGenCoralPlant();
	private static final WorldGenJellyfishTrees JELLYFISH_TREE_GENERATOR = new WorldGenJellyfishTrees(
			NoFeatureConfig::deserialize, false);
	private static final Feature[] GENERATORS = { ROCK_GENERATOR, ANDESITE_GENERATOR, CORAL_BLOCK_GENERATOR,
			CORAL_PLANT_GENERATOR };

	public BiomeDriedReef() {
		super("dried_reef", new Biome.Builder().surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.SAND_CONFIG)
				.precipitation(RainType.NONE).category(Category.DESERT).depth(0.125F).scale(0.05F).temperature(2.0F)
				.downfall(0.0F).temperature(2).waterColor(0x00FFFF).waterFogColor(329011).parent((String) null));
	}

	@Override
	public void decorate(GenerationStage.Decoration stage, ChunkGenerator<? extends GenerationSettings> chunkGenerator,
			IWorld worldIn, long seed, SharedSeedRandom rand, BlockPos pos) {
		int i = rand.nextInt(4);
		for (int j = 0; j < i; ++j) {
			int k = rand.nextInt(16) + 8;
			int l = rand.nextInt(16) + 8;
			BlockPos blockpos = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE, pos.add(k, 0, l));
			GENERATORS[rand.nextInt(GENERATORS.length)].place(worldIn, chunkGenerator, rand, pos,
					IFeatureConfig.NO_FEATURE_CONFIG);
		}
		if (rand.nextInt(1000) == 0)
			JELLYFISH_TREE_GENERATOR.place(worldIn, chunkGenerator, rand, pos, IFeatureConfig.NO_FEATURE_CONFIG);
		super.decorate(stage, chunkGenerator, worldIn, seed, rand, pos);
	}

	private static class WorldGenBlob extends Feature<NoFeatureConfig> {
		private BlockState block;

		WorldGenBlob(BlockState blockIn) {
			super(NoFeatureConfig::deserialize);
			this.block = blockIn;
		}

		@Override
		public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand,
				BlockPos position, NoFeatureConfig config) {
			BlockState state = getBlock(rand);
			while (!worldIn.canBlockSeeSky(position) || worldIn.isAirBlock(position)) {
				if (worldIn.isAirBlock(position))
					position = position.down();
				else
					position = position.up();
			}
			final BlockPos pos = position;
			for (int i = 0; i < 3; ++i) {
				int j = rand.nextInt(2);
				int k = rand.nextInt(2);
				int l = rand.nextInt(2);
				float f = (j + k + l) * 0.333F + 0.5F;
				BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))
						.filter(blockpos -> (blockpos.distanceSq(pos) <= f * f
								&& (worldIn.getBlockState(blockpos.down()).getBlock() == Blocks.SAND
										|| worldIn.getBlockState(blockpos.down()).getBlock() == state.getBlock())))
						.forEach(blockpos -> worldIn.setBlockState(blockpos, state, 4));
				position = position.add(rand.nextInt(4) - 1, -rand.nextInt(2), rand.nextInt(4) - 1);
			}
			return true;
		}

		protected BlockState getBlock(Random rand) {
			return block;
		}
	}

	private static class WorldGenCoralBlock extends WorldGenBlob {

		private BlockState[] states = new BlockState[CoralColor.values().length];

		WorldGenCoralBlock() {
			super(null);
		}

		@Override
		protected BlockState getBlock(Random rand) {
			int i = rand.nextInt(states.length);
			if (states[i] == null)
				states[i] = BeastsBlocks.CORAL_BLOCKS.get(CoralColor.values()[i]).getDefaultState();
			return states[i];
		}
	}

	private static class WorldGenCoralPlant extends Feature<NoFeatureConfig> {
		WorldGenCoralPlant() {
			super(NoFeatureConfig::deserialize);
		}

		@Override
		public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand,
				BlockPos position, NoFeatureConfig config) {
			ChunkPos chunkPos = worldIn.getChunk(position).getPos();
			if (BlockPos
					.getAllInBox(new BlockPos(chunkPos.getXStart(), position.getY() - 16, chunkPos.getZStart()),
							new BlockPos(chunkPos.getXEnd(), position.getY() + 16, chunkPos.getZEnd()))
					.anyMatch(pos -> (worldIn.getBlockState(pos).getBlock() instanceof LeavesBlock)))
				return false;
			BeastsBlocks.CORAL_SAPLINGS.get(CoralColor.getRandom(rand)).grow(worldIn, position, rand);
			return true;
		}
	}
}
