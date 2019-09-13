package rando.beasts.common.item;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import rando.beasts.client.init.BeastsItemGroup;
import rando.beasts.common.utils.BeastsUtil;

public class BeastsSword extends SwordItem {

	private BeastsToolSet kit;

	public BeastsSword(IItemTier material, String name, int attackDamageIn, float attackSpeedIn, Properties properties,
			@Nullable BeastsToolSet kit) {
		super(material, attackDamageIn, attackSpeedIn, properties.group(BeastsItemGroup.MAIN));
		this.kit = kit;
		BeastsUtil.addToRegistry(this, name);
	}

	public BeastsSword(IItemTier material, String name, int attackDamageIn, float attackSpeedIn,
			@Nullable BeastsToolSet kit) {
		this(material, name, attackDamageIn, attackSpeedIn, new Properties(), kit);
	}

	public BeastsSword(IItemTier material, String name, int attackDamageIn, float attackSpeedIn) {
		this(material, name, attackDamageIn, attackSpeedIn, null);
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		return (kit != null && kit.damageEntity(stack, target, attacker)) || super.hitEntity(stack, target, attacker);
	}
}
