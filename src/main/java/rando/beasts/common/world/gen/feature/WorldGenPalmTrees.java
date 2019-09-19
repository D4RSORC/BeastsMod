package rando.beasts.common.world.gen.feature;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import rando.beasts.common.block.BeastsBlock;
import rando.beasts.common.entity.monster.EntityCoconutCrab;
import rando.beasts.common.init.BeastsBlocks;
import rando.beasts.common.init.BeastsEntities;

import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

public class WorldGenPalmTrees extends AbstractTreeFeature<NoFeatureConfig> {
	public WorldGenPalmTrees(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i49920_1_, boolean doBlockNofityOnPlace) {
		super(p_i49920_1_, doBlockNofityOnPlace);
	}

	@Override
	protected boolean place(Set<BlockPos> changedBlocks, IWorldGenerationReader world, Random rand, BlockPos position, MutableBoundingBox boundsIn) {
		if (world.hasBlockState(position.down(), blockstate -> blockstate.getBlock() == Blocks.SAND)) {
            BlockState log = Blocks.JUNGLE_LOG.getDefaultState();
            BlockState leaves = Blocks.JUNGLE_LEAVES.getDefaultState();
            int height = rand.nextInt(4) + 7;
            int radius = rand.nextInt(4) + 2;
            if (radius % 2 == 0) radius += 1;
            for (int i = -1; i < 2; i++) for (int j = -1; j < 2; j++) setBlockState(world, position.add(i, height, j), leaves);
            for (int i = 1; i < radius; i++) {
                int h = height - i + 1;
                setBlockState(world, position.add(0, h, i + 1), leaves);
                setBlockState(world, position.add(0, h, -i - 1), leaves);
                setBlockState(world, position.add(i + 1, h, 0), leaves);
                setBlockState(world, position.add(-i - 1, h, 0), leaves);
            }

            BlockPos pos = position;
            setBlockState(world, pos, log);
           
            for (; world.hasBlockState(pos.up(), blockstate -> blockstate.getBlock() == Blocks.AIR); ) {
                BlockPos higher = pos.up();
                setBlockState(world, higher, log);
                pos = higher;
            }

            for (int i = -5; i < 5; i++) {
                if(i != 0) {
                    pos = position.add(i, 0, i);
                    if (rand.nextInt(3) == 0) {
                        if (rand.nextBoolean()) {
                        	EntityCoconutCrab crab = BeastsEntities.COCONUT_CRAB.create(Minecraft.getInstance().world.getWorld());
                            crab.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
                            crab.onInitialSpawn(Minecraft.getInstance().world.getWorld(), Minecraft.getInstance().world.getWorld().getDifficultyForLocation(pos),SpawnReason.CHUNK_GENERATION , null, null);
                            world.addEntity(crab);
                        } else world.setBlockState(pos, BeastsBlocks.COCONUT.getDefaultState(), 1);
                    }
                }
            }
            return true;
        }
        return false;
	}
}
