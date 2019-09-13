package rando.beasts.common.item;

import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.item.TallBlockItem;

public class ItemJellyWoodDoor extends TallBlockItem {
	public ItemJellyWoodDoor(Block block, Properties properties) {
		super(block, properties);
		String name = Objects.requireNonNull(block.getRegistryName()).getPath();
	}
}
