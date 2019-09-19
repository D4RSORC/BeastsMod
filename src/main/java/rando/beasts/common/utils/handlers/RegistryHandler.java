package rando.beasts.common.utils.handlers;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import rando.beasts.client.init.BeastsSounds;
import rando.beasts.common.entity.passive.EntityLandwhale;
import rando.beasts.common.init.BeastsBlocks;
import rando.beasts.common.init.BeastsContainers;
import rando.beasts.common.init.BeastsEntities;
import rando.beasts.common.init.BeastsItems;
import rando.beasts.common.inventory.ContainerLandwhaleInventory;
import rando.beasts.common.main.BeastsMod;
import rando.beasts.common.tileentity.TileEntityCoconut;
import rando.beasts.common.utils.BeastsReference;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = BeastsReference.ID, bus = Bus.MOD)
public class RegistryHandler {

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(BeastsBlocks.LIST.toArray(new Block[0]));
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		BeastsEntities.getEggs();
		event.getRegistry().registerAll(BeastsItems.LIST.toArray(new Item[0]));
		// TODO: add json recipes
//        FurnaceRecipes.instance().addSmelting(BeastsItems.CRAB_LEG, new ItemStack(BeastsItems.COOKED_CRAB_LEG), 0.35f);
//        FurnaceRecipes.instance().addSmelting(BeastsItems.EEL_CHOP, new ItemStack(BeastsItems.COOKED_EEL_CHOP), 0.50f);
//        FurnaceRecipes.instance().addSmelting(BeastsItems.BARNACLE_TONGUE, new ItemStack(BeastsItems.COOKED_BARNACLE_TONGUE), 0.35f);
//        FurnaceRecipes.instance().addSmelting(BeastsItems.SHRIMP, new ItemStack(BeastsItems.COOKED_SHRIMP), 0.35f);
//        FurnaceRecipes.instance().addSmelting(BeastsItems.RAW_KEBAB, new ItemStack(BeastsItems.COOKED_KEBAB), 0.35f);
//        FurnaceRecipes.instance().addSmelting(BeastsItems.DAGGERFISH, new ItemStack(BeastsItems.COOKED_DAGGERFISH), 0.50f);
//        OreDictionary.registerOre("logWood", BeastsBlocks.JELLY_WOOD);
//        OreDictionary.registerOre("plankWood", BeastsBlocks.JELLY_WOOD_PLANKS);
//        OreDictionary.registerOre("doorWood", Items.ACACIA_DOOR);
	}

	@SubscribeEvent
	public static void registerBiomes(RegistryEvent.Register<Biome> event) {
		// event.getRegistry().registerAll(BeastsBiomes.LIST.toArray(new Biome[0]));
		// BeastsBiomes.addTypes(BeastsBiomes.DRIED_REEF, BiomeManager.BiomeType.WARM,
		// 10, BEACH, HOT, DRY, SANDY);
	}

	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().registerAll(BeastsSounds.LIST.toArray(new SoundEvent[0]));
	}

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
		event.getRegistry().registerAll(BeastsEntities.LIST.toArray(new EntityType[0]));
	}

	@SubscribeEvent
	public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
		event.getRegistry()
				.register(TileEntityType.Builder
						.create((Supplier<TileEntity>) TileEntityCoconut::new, BeastsBlocks.COCONUT).build(null)
						.setRegistryName(BeastsBlocks.COCONUT.getRegistryName()));
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		// TODO
		// event.getRegistry().registerAll(BeastsRecipes.LIST.toArray(new
		// IRecipeSerializer<?>[0]));
	}
	
	@SubscribeEvent
	public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event)
    {
        event.getRegistry().register(BeastsContainers.LANDWHALE.setRegistryName("landwhale_inventory"));
    }
}
