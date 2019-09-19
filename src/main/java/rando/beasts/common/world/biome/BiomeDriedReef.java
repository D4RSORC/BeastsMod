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
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.World;
import rando.beasts.common.block.BlockCoral;
import rando.beasts.common.block.CoralColor;
import rando.beasts.common.entity.monster.EntityWhippingBarnacle;
import rando.beasts.common.init.BeastsBlocks;
import rando.beasts.common.world.gen.feature.WorldGenJellyfishTrees;
import rando.beasts.common.world.gen.feature.WorldGenPalmTrees;

public class BiomeDriedReef extends BeastsBiome {

	private static final WorldGenBlob ROCK_GENERATOR = new WorldGenBlob(Blocks.STONE.getDefaultState());
	private static final WorldGenBlob ANDESITE_GENERATOR = new WorldGenBlob(Blocks.ANDESITE.getDefaultState());
	private static final WorldGenBlob CORAL_BLOCK_GENERATOR = new WorldGenCoralBlock();
	private static final WorldGenCoralPlant CORAL_PLANT_GENERATOR = new WorldGenCoralPlant();
	private static final WorldGenJellyfishTrees JELLYFISH_TREE_GENERATOR = new WorldGenJellyfishTrees(NoFeatureConfig::deserialize, false);
	private static final WorldGenPalmTrees PALM_TREE_GENERATOR = new WorldGenPalmTrees(NoFeatureConfig::deserialize, false);
	private static final Feature[] CORALS = {CORAL_BLOCK_GENERATOR, CORAL_PLANT_GENERATOR};
	private static final WorldGenBlob[] ROCKS = {ROCK_GENERATOR, ANDESITE_GENERATOR};

	public BiomeDriedReef() {
		super("dried_reef", new Biome.Builder().surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.SAND_CONFIG)
				.precipitation(RainType.NONE).category(Category.DESERT).depth(0.125F).scale(0.05F).temperature(2.0F)
				.downfall(0.0F).temperature(2).waterColor(0x00FFFF).waterFogColor(329011).parent((String) null));
	}

	
	public AbstractTreeFeature<NoFeatureConfig> getRandomTreeFeature(Random rand) {
        return rand.nextInt(10) == 0 ? JELLYFISH_TREE_GENERATOR : PALM_TREE_GENERATOR;
    }
		
	@Override
	public void decorate(GenerationStage.Decoration stage, ChunkGenerator<? extends GenerationSettings> chunkGenerator, IWorld worldIn, long seed, SharedSeedRandom rand, BlockPos pos) {
		for (int i = 0; i < rand.nextInt(4); ++i) CORALS[rand.nextInt(CORALS.length)].place(worldIn, chunkGenerator, rand, worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING,pos.add(rand.nextInt(16) + 8, 0, rand.nextInt(16) + 8)), IFeatureConfig.NO_FEATURE_CONFIG);
        for (int i = 0; i < rand.nextInt(2); ++i) ROCKS[rand.nextInt(ROCKS.length)].place(worldIn, chunkGenerator, rand, worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING,pos.add(rand.nextInt(16) + 8, 0, rand.nextInt(16) + 8)), IFeatureConfig.NO_FEATURE_CONFIG);
        if(rand.nextInt(15) == 0) getRandomTreeFeature(rand).place(worldIn, chunkGenerator, rand,  worldIn.getHeight(Heightmap.Type.MOTION_BLOCKING, pos.add(rand.nextInt(16) + 8, 0, rand.nextInt(16) + 8)), IFeatureConfig.NO_FEATURE_CONFIG);
		super.decorate(stage, chunkGenerator, worldIn, seed, rand, pos);
	}

	private static class WorldGenBlob extends Feature<NoFeatureConfig> {
		private BlockState block;
		private int size;

		WorldGenBlob(BlockState blockIn, int size) {
			super(NoFeatureConfig::deserialize);
			this.block = blockIn;
			this.size = size;
		}
		WorldGenBlob(BlockState blockIn) {
			this(blockIn, 0);
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
				int j = rand.nextInt(2 + size);
				int k = rand.nextInt(2 + size);
				int l = rand.nextInt(2 + size);
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
