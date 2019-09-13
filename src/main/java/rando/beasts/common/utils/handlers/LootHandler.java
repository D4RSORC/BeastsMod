package rando.beasts.common.utils.handlers;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.TableLootEntry;
import rando.beasts.common.utils.BeastsReference;

public class LootHandler {
	private static final String[] TABLES = { "inject/fish" };

	// TODO
	public static void registerLootTables() {
		// for(String s : TABLES) LootTables.register(new
		// ResourceLocation(BeastsReference.ID, s));
	}

	public static LootPool getInjectPool(String entryName) {
		return LootPool.builder().build();
		// return LootPool.builder().addEntry(LootEntry.Builder())(new LootEntry[] {
		// getInjectEntry(entryName, 1) }, new ILootCondition[0], new ILootFunction[0],
		// new RandomValueRange(1), new RandomValueRange(0), "beasts_inject_pool");
	}

	public static LootEntry getInjectEntry(String name, int weight) {
		return TableLootEntry.builder(new ResourceLocation(BeastsReference.ID, "inject/" + name)).build();
		// return new TableLootEntry(new ResourceLocation(BeastsReference.ID, "inject/"
		// + name), weight, 0, new ILootCondition[0], "beasts_inject_entry");
	}

}
