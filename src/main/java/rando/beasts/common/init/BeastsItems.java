package rando.beasts.common.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemTier;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import rando.beasts.client.init.BeastsItemGroup;
import rando.beasts.common.block.CoralColor;
import rando.beasts.common.item.BeastsArmor;
import rando.beasts.common.item.BeastsCoconutBowl;
import rando.beasts.common.item.BeastsFood;
import rando.beasts.common.item.BeastsItem;
import rando.beasts.common.item.BeastsSword;
import rando.beasts.common.item.BeastsToolSet;
import rando.beasts.common.item.ItemBarnacleTongue;
import rando.beasts.common.item.ItemBeastsPainting;
import rando.beasts.common.item.ItemCoconade;
import rando.beasts.common.item.ItemCoralEssence;
import rando.beasts.common.item.ItemGlowRoot;
import rando.beasts.common.item.ItemIcon;
import rando.beasts.common.item.ToolSetJellyWood;

public class BeastsItems {

	public static final List<Item> LIST = new ArrayList<>();

	public static final Item ICON = new ItemIcon();
	public static final Item BEASTS_PAINTING = new ItemBeastsPainting();
	public static final Item GLOW_ROOT = new ItemGlowRoot();
	public static final Item LEAFY_BONE = new BeastsItem("leafy_bone");
	public static final Item CARROT_COIN = new BeastsItem("carrot_coin");
	public static final Item COCONADE = new ItemCoconade("coconade");
	public static final Item COCONUT_BOWL = new BeastsItem("coconut_bowl",
			new Properties().group(BeastsItemGroup.MAIN).maxStackSize(2));
	public static final Item DAGGERFISH = new BeastsFood("daggerfish", BeastsFoods.DAGGERFISH);
	public static final Item FISHSTAR = new BeastsItem("fishstar");
	public static final Item ATHAPOD_CHITIN = new BeastsItem("athapod_chitin");
	public static final Item SPARTAPOD_CHITIN = new BeastsItem("spartapod_chitin");
	public static final Item SPARTAPOD_CREST = new BeastsItem("spartapod_crest");
	public static final Item ICE_CRAB_CHITIN = new BeastsItem("ice_crab_chitin");
	public static final Item WORM_TOOTH = new BeastsItem("worm_tooth");
	public static final Item COCONUT_MUSHROOM = new BeastsCoconutBowl("coconut_mushroom", BeastsFoods.COCONUT_MUSHROOM);
	public static final Item COCONUT_RABBIT_STEW = new BeastsCoconutBowl("coconut_rabbit_stew",
			BeastsFoods.COCONUT_RABBIT_STEW);
	public static final Item REEF_MIXTURE = new BeastsCoconutBowl("reef_mixture", BeastsFoods.REEF_MIXTURE,
			new EffectInstance(Effects.REGENERATION, 100, 0), new EffectInstance(Effects.WEAKNESS, 100, 0));
	public static final Item CRAB_LEG = new BeastsFood("crab_leg", BeastsFoods.CRAB_LEG);
	public static final Item COOKED_CRAB_LEG = new BeastsFood("cooked_crab_leg", BeastsFoods.COOKED_CRAB_LEG);
	public static final Item COCONUT = new BeastsFood("coconut", BeastsFoods.COCONUT);
	public static final Item BARNACLE_TONGUE = new ItemBarnacleTongue(false, BeastsFoods.BARNACLE_TONGUE);
	public static final Item COOKED_BARNACLE_TONGUE = new ItemBarnacleTongue(true, BeastsFoods.COOKED_BARNACLE_TONGUE);
	public static final Item SHRIMP = new BeastsFood("shrimp", BeastsFoods.SHRIMP);
	public static final Item COOKED_SHRIMP = new BeastsFood("cooked_shrimp", BeastsFoods.COOKED_SHRIMP);
	public static final Item RAW_KEBAB = new BeastsFood("raw_kebab", BeastsFoods.RAW_KEBAB);
	public static final Item COOKED_KEBAB = new BeastsFood("cooked_kebab", BeastsFoods.COOKED_KEBAB);
	public static final Item EEL_CHOP = new BeastsFood("eel_chop", BeastsFoods.EEL_CHOP);
	public static final Item COOKED_EEL_CHOP = new BeastsFood("cooked_eel_chop", BeastsFoods.COOKED_EEL_CHOP);
	public static final Item COCONUT_JUICE = new BeastsCoconutBowl("coconut_juice", BeastsFoods.COCONUT_JUICE);
	public static final ArmorItem SPARTAPOD_HELMET = new BeastsArmor("spartapod_helmet", BeastsArmorMaterial.SPARTAPOD,
			EquipmentSlotType.HEAD);
	public static final ArmorItem SPARTAPOD_CHEST = new BeastsArmor("spartapod_chest", BeastsArmorMaterial.SPARTAPODA,
			EquipmentSlotType.CHEST);
	public static final ArmorItem SPARTAPOD_LEGS = new BeastsArmor("spartapod_legs", BeastsArmorMaterial.SPARTAPODA,
			EquipmentSlotType.LEGS);
	public static final ArmorItem SPARTAPOD_BOOTS = new BeastsArmor("spartapod_boots", BeastsArmorMaterial.SPARTAPODA,
			EquipmentSlotType.FEET);
	public static final SwordItem COOKED_DAGGERFISH = new BeastsSword(BeastsItemTier.SWORDFISH_TIER,
			"cooked_daggerfish", 3, -3.0F, new Properties().food(BeastsFoods.COOKED_DAGGERFISH), null);
	public static final SwordItem DIAMOND_CARROT = new BeastsSword(ItemTier.DIAMOND, "diamond_carrot", 3, -3.0F);
	public static final BeastsToolSet JELLY_TOOLS = new ToolSetJellyWood();
	public static final Map<EntityType, Item> EGGS = new HashMap<EntityType, Item>();
	public static final Map<CoralColor, Item> CORAL_ESSENCES = new HashMap<CoralColor, Item>();

	static {
		for (CoralColor color : CoralColor.values())
			CORAL_ESSENCES.put(color, new ItemCoralEssence(color));
	}
}
