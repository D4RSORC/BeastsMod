package rando.beasts.common.item;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import rando.beasts.client.init.BeastsItemGroup;
import rando.beasts.common.utils.BeastsUtil;

public class BeastsPickaxe extends PickaxeItem {

	private BeastsToolSet kit;

	public BeastsPickaxe(IItemTier material, String name, int attackDamageIn, float attackSpeedIn,
			@Nullable BeastsToolSet kit) {
		super(material, attackDamageIn, attackSpeedIn, new Properties().group(BeastsItemGroup.MAIN));
		this.kit = kit;
		BeastsUtil.addToRegistry(this, name);
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		return (kit != null && kit.damageEntity(stack, target, attacker)) || super.hitEntity(stack, target, attacker);
	}
}
