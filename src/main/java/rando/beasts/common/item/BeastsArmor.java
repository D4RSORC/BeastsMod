package rando.beasts.common.item;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rando.beasts.client.init.BeastsItemGroup;
import rando.beasts.common.main.BeastsMod;
import rando.beasts.common.utils.BeastsUtil;

public class BeastsArmor extends ArmorItem {

	public BeastsArmor(String name, IArmorMaterial material, EquipmentSlotType armorType) {
		super(material, armorType, new Properties().group(BeastsItemGroup.MAIN));
		BeastsUtil.addToRegistry(this, name);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public BipedModel getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot,
			BipedModel defaultModel) {
		BipedModel armorModel = BeastsMod.proxy.getArmorModel(this, armorSlot);
		if (armorModel != null) {
			armorModel.isSneak = defaultModel.isSneak;
			armorModel.isSitting = defaultModel.isSitting;
			armorModel.isChild = defaultModel.isChild;
			armorModel.rightArmPose = defaultModel.rightArmPose;
			armorModel.leftArmPose = defaultModel.leftArmPose;
		}
		return armorModel;
	}
}