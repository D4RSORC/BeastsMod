package rando.beasts.common.world.gen.feature;

import java.util.Random;

import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.AbstractTreeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class PalmTree extends Tree{

	@Override
	protected AbstractTreeFeature<NoFeatureConfig> getTreeFeature(Random random) {
		return new WorldGenPalmTrees(NoFeatureConfig::deserialize, true);
	}

}
