package rando.beasts.common.utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;

public interface ItemFactory<T extends Item> {
	T apply(Block block, Properties properties);
}
