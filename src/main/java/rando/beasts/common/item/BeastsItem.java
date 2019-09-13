package rando.beasts.common.item;

import net.minecraft.item.Item;
import rando.beasts.client.init.BeastsItemGroup;
import rando.beasts.common.utils.BeastsUtil;

public class BeastsItem extends Item {

	public BeastsItem(String name, boolean tab) {
		super(getProperties(tab));
		BeastsUtil.addToRegistry(this, name);
	}

	public BeastsItem(String name, Properties properties) {
		super(properties);
		BeastsUtil.addToRegistry(this, name);
	}

	public BeastsItem(String name) {
		this(name, true);
	}

	private static Properties getProperties(boolean tab) {
		return tab ? new Item.Properties().group(BeastsItemGroup.MAIN) : new Item.Properties();
	}
}
