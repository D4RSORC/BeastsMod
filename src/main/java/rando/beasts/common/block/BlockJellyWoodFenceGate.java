package rando.beasts.common.block;

import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import rando.beasts.common.utils.BeastsUtil;

public class BlockJellyWoodFenceGate extends FenceGateBlock {
	public BlockJellyWoodFenceGate() {
		super(Properties.create(Material.WOOD));
		BeastsUtil.addToRegistry(this, "jellywood_gate", true, BlockItem::new);
	}
}
