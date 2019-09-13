package rando.beasts.common.init;

import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class BeastsFoods {
	public static final Food DAGGERFISH = (new Food.Builder()).hunger(2).saturation(0.1F).build();
	public static final Food COOKED_DAGGERFISH = (new Food.Builder()).hunger(6).saturation(0.8F).build();
	public static final Food COCONUT_MUSHROOM = (new Food.Builder()).hunger(6).saturation(0.7F).build();
	public static final Food COCONUT_RABBIT_STEW = new Food.Builder().hunger(8).saturation(0.7F).build();
	public static final Food REEF_MIXTURE = new Food.Builder().hunger(10).saturation(0.9F).build();
	public static final Food CRAB_LEG = new Food.Builder().hunger(2).saturation(0.1F).build();
	public static final Food COOKED_CRAB_LEG = new Food.Builder().hunger(6).saturation(0.6F).build();
	public static final Food COCONUT = new Food.Builder().hunger(2).saturation(0.4F).build();
	public static final Food BARNACLE_TONGUE = new Food.Builder().hunger(2).saturation(0.1F)
			.effect(new EffectInstance(Effects.REGENERATION, 100, 0), 1)
			.effect(new EffectInstance(Effects.WEAKNESS, 200, 0), 1).build();
	public static final Food COOKED_BARNACLE_TONGUE = new Food.Builder().hunger(5).saturation(0.6F)
			.effect(new EffectInstance(Effects.REGENERATION, 100, 0), 1)
			.effect(new EffectInstance(Effects.WEAKNESS, 100, 0), 1).build();
	public static final Food SHRIMP = new Food.Builder().hunger(2).saturation(0.2F)
			.effect(new EffectInstance(Effects.HUNGER, 600, 0), 0.3f).build();
	public static final Food COOKED_SHRIMP = new Food.Builder().hunger(6).saturation(0.5F).build();
	public static final Food RAW_KEBAB = new Food.Builder().hunger(4).saturation(0.3F)
			.effect(new EffectInstance(Effects.HUNGER, 600, 0), 0.3f).build();
	public static final Food COOKED_KEBAB = new Food.Builder().hunger(8).saturation(0.7F).build();
	public static final Food EEL_CHOP = new Food.Builder().hunger(3).saturation(0.3F).build();
	public static final Food COOKED_EEL_CHOP = new Food.Builder().hunger(8).saturation(0.5F).build();
	public static final Food COCONUT_JUICE = new Food.Builder().hunger(4).saturation(0.5F).build();

}
