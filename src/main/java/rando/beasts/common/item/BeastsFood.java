package rando.beasts.common.item;

import net.minecraft.item.Food;
import rando.beasts.client.init.BeastsItemGroup;

public class BeastsFood extends BeastsItem {
	public BeastsFood(String name, Food food) {
		super(name, new Properties().group(BeastsItemGroup.MAIN).food(food));
	}

	public BeastsFood(String name, Properties properties, Food food) {
		super(name, properties.group(BeastsItemGroup.MAIN).food(food));
	}
}
