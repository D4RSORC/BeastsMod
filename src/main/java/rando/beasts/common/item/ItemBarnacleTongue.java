package rando.beasts.common.item;

import net.minecraft.item.Food;

public class ItemBarnacleTongue extends BeastsFood {

	public ItemBarnacleTongue(boolean cooked, Food food) {
		super((cooked ? "cooked_" : "") + "barnacle_tongue", food);
	}
}
