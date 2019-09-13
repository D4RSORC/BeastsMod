package rando.beasts.common.utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import rando.beasts.client.init.BeastsItemGroup;
import rando.beasts.common.init.BeastsBlocks;
import rando.beasts.common.init.BeastsItems;

public class BeastsUtil {

	public static void addToRegistry(Item item, String name) {
		item.setRegistryName(name);
		BeastsItems.LIST.add(item);
	}

	public static void addToRegistry(Block block, String name, boolean tab, ItemFactory<?> item) {
		block.setRegistryName(name);
		BeastsBlocks.LIST.add(block);
		if (item != null && tab)
			BeastsItems.LIST.add(item.apply(block, new Properties().group(BeastsItemGroup.MAIN)).setRegistryName(name));
	}
}
