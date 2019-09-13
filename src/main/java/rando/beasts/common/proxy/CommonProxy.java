package rando.beasts.common.proxy;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import rando.beasts.common.init.BeastsTriggers;
import rando.beasts.common.utils.handlers.LootHandler;

public class CommonProxy {

	public void preInit() {
		// StructureRabbitVillagePieces.register();
		// GameRegistry.registerWorldGenerator((random, chunkX, chunkZ, world,
		// chunkGenerator, chunkProvider) -> BeastsMod.RABBIT_VILLAGE.generate(world,
		// random, world.getHeight(new BlockPos((chunkX * 16) + 8, 0, (chunkZ * 16) +
		// 8))), 0);
		for (ICriterionTrigger<? extends ICriterionInstance> trigger : BeastsTriggers.LIST)
			CriteriaTriggers.register(trigger);
		// ForgeModContainer.logCascadingWorldGeneration = false;
		LootHandler.registerLootTables();
	}

	public BipedModel getArmorModel(Item armorItem, EquipmentSlotType armorSlot) {
		return null;
	}
}
