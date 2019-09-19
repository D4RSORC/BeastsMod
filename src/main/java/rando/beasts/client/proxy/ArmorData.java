package rando.beasts.client.proxy;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import rando.beasts.client.model.ModelHermitHelm;
import rando.beasts.client.model.ModelPufferArmor;
import rando.beasts.client.model.ModelSpartapodArmor;
import rando.beasts.common.init.BeastsItems;

class ArmorData {
    static final Map<EquipmentSlotType, Map<Item, BipedModel>> MODELS = new HashMap<>();
    static final Map<EquipmentSlotType, Map<Item, String>> TEXTURES = new HashMap<>();

    static {
        MODELS.put(EquipmentSlotType.FEET, new HashMap<>());
        MODELS.put(EquipmentSlotType.LEGS, new HashMap<>());
        MODELS.put(EquipmentSlotType.CHEST, new HashMap<>());
        MODELS.put(EquipmentSlotType.HEAD, new HashMap<>());
        TEXTURES.put(EquipmentSlotType.FEET, new HashMap<>());
        TEXTURES.put(EquipmentSlotType.LEGS, new HashMap<>());
        TEXTURES.put(EquipmentSlotType.CHEST, new HashMap<>());
        TEXTURES.put(EquipmentSlotType.HEAD, new HashMap<>());

        //Setup all armor models and textures here

        MODELS.get(EquipmentSlotType.HEAD).put(BeastsItems.SPARTAPOD_HELMET, new ModelSpartapodArmor());
        MODELS.get(EquipmentSlotType.HEAD).put(BeastsItems.HERMIT_HELM, new ModelHermitHelm());

        BipedModel pufferModel = new ModelPufferArmor();

        MODELS.get(EquipmentSlotType.FEET).put(BeastsItems.PUFFER_BOOTS, pufferModel);
        MODELS.get(EquipmentSlotType.LEGS).put(BeastsItems.PUFFER_LEGS, pufferModel);
        MODELS.get(EquipmentSlotType.CHEST).put(BeastsItems.PUFFER_CHEST, pufferModel);
        MODELS.get(EquipmentSlotType.HEAD).put(BeastsItems.PUFFER_HELMET, pufferModel);

        TEXTURES.get(EquipmentSlotType.HEAD).put(BeastsItems.PUFFER_BOOTS, "puffer_boots");
        TEXTURES.get(EquipmentSlotType.HEAD).put(BeastsItems.PUFFER_LEGS, "puffer_legs");
        TEXTURES.get(EquipmentSlotType.HEAD).put(BeastsItems.PUFFER_CHEST, "puffer_chest");
        TEXTURES.get(EquipmentSlotType.HEAD).put(BeastsItems.PUFFER_HELMET, "puffer_helmet");
    }
}
