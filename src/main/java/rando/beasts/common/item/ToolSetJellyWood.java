package rando.beasts.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ToolSetJellyWood extends BeastsToolSet {
	public final float DAMAGES[] = { 8.0F, 1.0F, 1.5F, 3.0F };
	public final float SPEEDS[] = { -3.2F, -2.8F, -3.0F, -3.0F, 1.0F };

	public ToolSetJellyWood() {
		super(ItemTier.WOOD, "jelly");
	}

	@Override
	boolean damageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if (!target.isPotionActive(Effects.POISON)) {
			EffectInstance effect = new EffectInstance(Effects.POISON, 100);
			if (target.isPotionApplicable(effect))
				target.addPotionEffect(effect);
			return true;
		}
		return super.damageEntity(stack, target, attacker);
	}
}
