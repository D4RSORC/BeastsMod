package rando.beasts.common.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.util.EnumHelper;
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
import rando.beasts.common.item.ItemDaggerfish;
import rando.beasts.common.item.ItemGlowRoot;
import rando.beasts.common.item.ItemIcon;
import rando.beasts.common.item.ItemPufferArmor;
import rando.beasts.common.item.ToolSetJellyWood;

public class BeastsItems {
	
	public static final List<Item> LIST = new ArrayList<>();
	private static final Item.ToolMaterial SWORDFISH_MAT = EnumHelper.addToolMaterial("daggerfish", 0, 3, 8.0F, 4.0F, 0);
	private static final ItemArmor.ArmorMaterial SPARTAPOD = EnumHelper.addArmorMaterial("spartapod", "beasts:spartapod", 15, new int[]{2, 5, 6, 2}, 12, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0F);
	private static final ItemArmor.ArmorMaterial SPARTAPODA = EnumHelper.addArmorMaterial("spartapoda", "beasts:spartapoda", 15, new int[]{2, 5, 6, 2}, 12, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0F);

	public static final Item ICON = new ItemIcon();
	public static final Item BEASTS_PAINTING = new ItemBeastsPainting();
	public static final Item GLOW_ROOT = new ItemGlowRoot();
	public static final Item LEAFY_BONE = new BeastsItem("leafy_bone");
	public static final Item CARROT_COIN = new BeastsItem("carrot_coin");
	public static final Item COCONADE = new ItemCoconade("coconade");
	public static final Item CORAL_ESSENCE = new ItemCoralEssence();
	public static final Item COCONUT_BOWL = new BeastsItem("coconut_bowl").setMaxStackSize(2);
	public static final Item FISHSTAR = new BeastsItem("fishstar");
	public static final Item ATHAPOD_CHITIN = new BeastsItem("athapod_chitin"); 
	public static final Item SPARTAPOD_CHITIN = new BeastsItem("spartapod_chitin"); 
	public static final Item SPARTAPOD_CREST = new BeastsItem("spartapod_crest"); 
	public static final Item ICE_CRAB_CHITIN = new BeastsItem("ice_crab_chitin"); 
	public static final Item WORM_TOOTH = new BeastsItem("worm_tooth");
	public static final Item PUFFER_SCALE = new BeastsItem("puffer_scale");
	public static final Item HERMIT_HELM = new BeastsArmor("hermit_helm", ItemArmor.ArmorMaterial.IRON, EntityEquipmentSlot.HEAD).setMaxDamage(0);
	public static final ItemFood DAGGERFISH = new BeastsFood("daggerfish", 2, 0.1F);
	public static final ItemFood COCONUT_MUSHROOM = new BeastsCoconutBowl("coconut_mushroom", 6, 0.7F);
	public static final ItemFood COCONUT_RABBIT_STEW = new BeastsCoconutBowl("coconut_rabbit_stew", 8, 0.7F);
	public static final ItemFood REEF_MIXTURE = new BeastsCoconutBowl("reef_mixture", 10, 0.9F, new PotionEffect(MobEffects.REGENERATION, 100, 0), new PotionEffect(MobEffects.WEAKNESS, 100, 0));
	public static final ItemFood CRAB_LEG = new BeastsFood("crab_leg", 2, 0.1F);
	public static final ItemFood COOKED_CRAB_LEG = new BeastsFood("cooked_crab_leg", 6, 0.6F);
	public static final ItemFood COCONUT = new BeastsFood("coconut", 2, 0.4F);
	public static final ItemFood BARNACLE_TONGUE = new ItemBarnacleTongue(false, 2, 0.1F, 200);
	public static final ItemFood COOKED_BARNACLE_TONGUE = new ItemBarnacleTongue(true, 5, 0.6F, 100);
	public static final ItemFood SHRIMP = new BeastsFood("shrimp", 2, 0.2F).setPotionEffect(new PotionEffect(MobEffects.HUNGER, 600, 0), 0.3f);
	public static final ItemFood COOKED_SHRIMP = new BeastsFood("cooked_shrimp", 6, 0.5F);
	public static final ItemFood RAW_KEBAB = new BeastsFood("raw_kebab", 4, 0.3F).setPotionEffect(new PotionEffect(MobEffects.HUNGER, 600, 0), 0.3f);
	public static final ItemFood COOKED_KEBAB = new BeastsFood("cooked_kebab", 8, 0.7F); 
	public static final ItemFood EEL_CHOP = new BeastsFood("eel_chop", 3, 0.3F); 
	public static final ItemFood COOKED_EEL_CHOP = new BeastsFood("cooked_eel_chop", 8, 0.5F);
	public static final ItemFood COCONUT_JUICE = new BeastsCoconutBowl("coconut_juice", 4, 0.5F);
	//todo do armor sets like with the tool set with the jelly tools
	public static final ItemArmor SPARTAPOD_HELMET = new BeastsArmor("spartapod_helmet", SPARTAPOD, EntityEquipmentSlot.HEAD);
	public static final ItemArmor SPARTAPOD_CHEST = new BeastsArmor("spartapod_chest", SPARTAPODA, EntityEquipmentSlot.CHEST);
	public static final ItemArmor SPARTAPOD_LEGS = new BeastsArmor("spartapod_legs", SPARTAPODA, EntityEquipmentSlot.LEGS);
	public static final ItemArmor SPARTAPOD_BOOTS = new BeastsArmor("spartapod_boots", SPARTAPODA, EntityEquipmentSlot.FEET);
	public static final ItemArmor PUFFER_HELMET = new ItemPufferArmor("helmet", EntityEquipmentSlot.HEAD);
	public static final ItemArmor PUFFER_CHEST = new ItemPufferArmor("chest", EntityEquipmentSlot.CHEST);
	public static final ItemArmor PUFFER_LEGS = new ItemPufferArmor("legs", EntityEquipmentSlot.LEGS);
	public static final ItemArmor PUFFER_BOOTS = new ItemPufferArmor("boots", EntityEquipmentSlot.FEET);
	public static final ItemSword COOKED_DAGGERFISH = new ItemDaggerfish(SWORDFISH_MAT, "cooked_daggerfish");
	public static final ItemSword DIAMOND_CARROT = new BeastsSword(Item.ToolMaterial.DIAMOND, "diamond_carrot");
	public static final BeastsToolSet JELLY_TOOLS = new ToolSetJellyWood();
}
