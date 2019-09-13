package rando.beasts.client.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import rando.beasts.common.init.BeastsItems;
import rando.beasts.common.utils.BeastsReference;

public class BeastsItemGroup {
	public static final ItemGroup MAIN = new ItemGroup(BeastsReference.ID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(BeastsItems.ICON);
		}
	};
}
