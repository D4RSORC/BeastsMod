package rando.beasts.common.block;

import net.minecraft.block.FenceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import rando.beasts.common.utils.BeastsUtil;

public class BlockJellyWoodFence extends FenceBlock {
	public BlockJellyWoodFence() {
		super(Properties.create(Material.WOOD, Material.WOOD.getColor()));
		BeastsUtil.addToRegistry(this, "jellywood_fence", true, BlockItem::new);
	}
}
