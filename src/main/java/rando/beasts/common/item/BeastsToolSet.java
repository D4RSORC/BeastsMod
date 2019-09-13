package rando.beasts.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;

public class BeastsToolSet {
	public final float DAMAGES[] = { 8.0F, 1.0F, 1.5F, 3.0F };
	public final float SPEEDS[] = { -3.2F, -2.8F, -3.0F, -3.0F, 1.0F };

	public final AxeItem axe;
	public final PickaxeItem pickaxe;
	public final ShovelItem shovel;
	public final SwordItem sword;
	public final HoeItem hoe;

	public BeastsToolSet(IItemTier tier, String name) {
		this.axe = new BeastsAxe(tier, name + "_axe", DAMAGES[0], SPEEDS[0], this);
		this.pickaxe = new BeastsPickaxe(tier, name + "_pickaxe", (int) DAMAGES[1], SPEEDS[1], this);
		this.shovel = new BeastsShovel(tier, name + "_shovel", DAMAGES[2], SPEEDS[2], this);
		this.sword = new BeastsSword(tier, name + "_sword", (int) DAMAGES[3], SPEEDS[3], this);
		this.hoe = new BeastsHoe(tier, name + "_hoe", tier.getAttackDamage() + SPEEDS[4], this);
	}

	boolean damageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		return false;
	}
}
