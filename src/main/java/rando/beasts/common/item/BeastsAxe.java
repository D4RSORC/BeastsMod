package rando.beasts.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import rando.beasts.client.init.BeastsItemGroup;
import rando.beasts.common.utils.BeastsUtil;

public class BeastsAxe extends AxeItem {

	private BeastsToolSet kit;

	public BeastsAxe(IItemTier tier, String name, float attackDamageIn, float attackSpeedIn, BeastsToolSet kit) {
		super(tier, attackDamageIn, attackSpeedIn, new Properties().group(BeastsItemGroup.MAIN));
		this.kit = kit;
		BeastsUtil.addToRegistry(this, name);
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		return (kit != null && kit.damageEntity(stack, target, attacker)) || super.hitEntity(stack, target, attacker);
	}
}
