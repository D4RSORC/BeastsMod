package rando.beasts.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import rando.beasts.common.init.BeastsItems;

public class BeastsCoconutBowl extends BeastsFood {

	private final EffectInstance[] effects;

	public BeastsCoconutBowl(String name, Food food, EffectInstance... effects) {
		super(name, new Properties().maxStackSize(2), food);
		this.effects = effects;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		super.onItemUseFinish(stack, worldIn, entityLiving);
		for (EffectInstance effect : effects)
			entityLiving.addPotionEffect(effect);
		return new ItemStack(BeastsItems.COCONUT_BOWL);
	}
}
