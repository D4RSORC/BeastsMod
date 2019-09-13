package rando.beasts.common.item;

import rando.beasts.common.block.CoralColor;

public class ItemCoralEssence extends BeastsItem {
	public ItemCoralEssence(CoralColor color) {
		super("coral_essence_" + color.getName());
	}
}
