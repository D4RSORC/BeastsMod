package rando.beasts.common.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import rando.beasts.common.block.BeastsBlock;
import rando.beasts.common.block.BeastsSapling;
import rando.beasts.common.block.BlockCoconut;
import rando.beasts.common.block.BlockCoral;
import rando.beasts.common.block.BlockCoralPlant;
import rando.beasts.common.block.BlockCoralSapling;
import rando.beasts.common.block.BlockGlowRoot;
import rando.beasts.common.block.BlockJellyWoodDoor;
import rando.beasts.common.block.BlockJellyWoodFence;
import rando.beasts.common.block.BlockJellyWoodFenceGate;
import rando.beasts.common.block.BlockJellyfisLog;
import rando.beasts.common.block.BlockJellyfishLeaves;
import rando.beasts.common.block.BlockTentacle;
import rando.beasts.common.block.CoralColor;
import rando.beasts.common.world.gen.feature.JellyFishTree;

public class BeastsBlocks {

	public static final List<Block> LIST = new ArrayList<>();

	public static final Block COCONUT = new BlockCoconut();
	public static final Block GLOW_ROOT_TOP = new BlockGlowRoot(true);
	public static final Block GLOW_ROOT_BOTTOM = new BlockGlowRoot(false);
	public static final Block JELLYFISH_SAPLING = new BeastsSapling("jellyfish_sapling", new JellyFishTree());
	public static final Block TENTACLE = new BlockTentacle();
	public static final Block JELLY_WOOD = new BlockJellyfisLog();
	public static final Block JELLY_LEAVES = new BlockJellyfishLeaves();
	public static final Block JELLY_WOOD_PLANKS = new BeastsBlock(Material.WOOD, "jellywood_planks");
	public static final Block JELLY_WOOD_DOOR = new BlockJellyWoodDoor();
	public static final Block JELLY_WOOD_FENCE = new BlockJellyWoodFence();
	public static final Block JELLY_WOOD_GATE = new BlockJellyWoodFenceGate();
	// this is a map so any coral plant can be gotten by the color or index/ordinal
	public static final Map<CoralColor, BlockCoralPlant> CORAL_PLANTS = new HashMap<>();
	public static final Map<CoralColor, BlockCoral> CORAL_BLOCKS = new HashMap<>();
	public static final Map<CoralColor, BlockCoralSapling> CORAL_SAPLINGS = new HashMap<>();

	static {
		for (CoralColor color : CoralColor.values()) {
			CORAL_PLANTS.put(color, new BlockCoralPlant(color));
			CORAL_BLOCKS.put(color, new BlockCoral(color));
			CORAL_SAPLINGS.put(color, new BlockCoralSapling(color));
		}
	}
}
