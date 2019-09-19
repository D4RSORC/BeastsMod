package rando.beasts.common.init;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.event.world.BlockEvent;
import rando.beasts.common.entity.item.EntityBeastsPainting;
import rando.beasts.common.entity.item.EntityFallingCoconut;
import rando.beasts.common.entity.monster.*;
import rando.beasts.common.entity.passive.EntityHermitTurtle;
import rando.beasts.common.entity.passive.EntityLandwhale;
import rando.beasts.common.entity.passive.EntityPufferfishDog;
import rando.beasts.common.entity.passive.EntityRabbitman;
import rando.beasts.common.entity.projectile.EntityCoconutBomb;
import rando.beasts.common.utils.BeastsReference;

import java.util.*;
import java.util.function.Function;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class BeastsEntities {

	public static final List<EntityType<?>> LIST = new ArrayList<>();

	public static final EntityType<EntityPufferfishDog> PUFFERFISH_DOG = createEntry(EntityPufferfishDog.class, EntityPufferfishDog::new, EntityClassification.CREATURE, 0xFBA70C, 0x429BBA, 0.5f, 0.5f, new SpawnEntry(10, 1, 1, BeastsBiomes.DRIED_REEF));
	public static final EntityType RABBITMAN = createEntry(EntityRabbitman.class, EntityRabbitman::new, EntityClassification.CREATURE, 0x4E362D, 0xE5E5E5, 0.6F, 1.95F, null);
	public static final EntityType<EntityCoconutCrab> COCONUT_CRAB = createEntry(EntityCoconutCrab.class, EntityCoconutCrab::new, EntityClassification.CREATURE, 0x3C1C11, 0xA16745, 0.5f, 0.4f, new SpawnEntry(12, 1, 1, BeastsBiomes.DRIED_REEF));
	public static final EntityType CORAL_BRANCHIE = createBranchie(EntityCoralBranchie.class, EntityCoralBranchie::new, 0xEDEC4C, 0xD6549B, BeastsBlocks.CORAL_PLANTS.values(), EntityCoralBranchie::create);
	public static final EntityType CHORUS_BRANCHIE = createBranchie(EntityChorusBranchie.class, EntityChorusBranchie::new, 0x401A40, 0xEED6EE, Collections.singletonList(Blocks.CHORUS_PLANT), EntityChorusBranchie::create);
	public static final EntityType WOOD_BRANCHIE = createBranchie(EntityWoodBranchie.class, EntityWoodBranchie::new, 0x745A36, 0x57AD3F, EntityWoodBranchie.LogType.getLogBlocks(), EntityWoodBranchie::create);
	public static final EntityType VILE_EEL = createEntry(EntityVileEel.class, EntityVileEel::new, EntityClassification.MONSTER, 0x313337, 0x987CAF, 1.5F, 1.8F, new SpawnEntry(7, 1, 1, BeastsBiomes.DRIED_REEF));
	public static final EntityType LANDWHALE = createEntry(EntityLandwhale.class, EntityLandwhale::new, EntityClassification.CREATURE, 0x587377, 0xE25AA5, 1.8F, 2.0F, new SpawnEntry(6, 1, 1, BeastsBiomes.DRIED_REEF));
	public static final EntityType COCONADE = createEntry(EntityCoconutBomb.class, EntityCoconutBomb::new);
	public static final EntityType GIANT_GARDEN_EEL = createEntry(EntityGiantGardenEel.class, EntityGiantGardenEel::new, EntityClassification.MONSTER, 0xCECEAF, 0x7A745E, 0.5f, 2.7f, new SpawnEntry(10, 4, 8, BeastsBiomes.DRIED_REEF));
	public static final EntityType SKEWER_SHRIMP = createEntry(EntitySkewerShrimp.class, EntitySkewerShrimp::new, EntityClassification.MONSTER, 0xEA4E3C, 0xFFACA3, 0.5f, 0.4f, new SpawnEntry(6, 4, 8, BeastsBiomes.DRIED_REEF));
	public static final EntityType BEASTS_PAINTING = createEntry(EntityBeastsPainting.class, EntityBeastsPainting::new);
	public static final EntityType FALLING_COCONUT = createEntry(EntityFallingCoconut.class, EntityFallingCoconut::new, 0.98F, 0.98F);
	public static final EntityType WHIPPING_BARNACLE = createEntry(EntityWhippingBarnacle.class, EntityWhippingBarnacle::new, EntityClassification.CREATURE, 0, 0, 0.5f, 1.5f, new SpawnEntry(50, 1, 3, BeastsBiomes.DRIED_REEF));
	public static final EntityType HERMIT_TURTLE = createEntry(EntityHermitTurtle.class, EntityHermitTurtle::new, EntityClassification.CREATURE, 0, 0, 0.5f, 0.4f, new SpawnEntry(50, 1, 3, BeastsBiomes.DRIED_REEF));

	private static EntityType createEntry(Class cls, IFactory factory, EntityClassification classification, int prim, int sec, float width, float height, @Nullable SpawnEntry spawn) {
		String entityName = StringUtils.join(cls.getSimpleName().replace("Entity", "").split("(?=[A-Z])"), "_")
				.toLowerCase();
		EntityType type = EntityType.Builder.create(factory, classification).size(width, height).build(BeastsReference.ID + ":" + entityName);
		type.setRegistryName(BeastsReference.ID, entityName);
		if (spawn != null) registerEntityWorldSpawn(type, spawn);
		registerEntitySpawnEgg(type, prim, sec, entityName);
		LIST.add(type);
		return type;
	}

	public static void getEggs() {
	}

	private static void registerEntitySpawnEgg(EntityType type, int prim, int sec, String entityName) {
		SpawnEggItem egg = new SpawnEggItem(type, prim, sec, new Properties().group(ItemGroup.MISC));
		egg.setRegistryName(entityName + "_egg");
		BeastsItems.EGGS.put(type, egg);
		BeastsItems.LIST.add(egg);
	}

	private static void registerEntityWorldSpawn(EntityType entity, SpawnEntry spawn) {
		for (Biome biome : spawn.biomes) {
			if (biome != null)
				biome.getSpawns(entity.getClassification()).add(new SpawnListEntry(entity, spawn.weight, spawn.min, spawn.max));
		}
	}

	private static EntityType createEntry(Class cls, IFactory factory, float width, float height) {
		String entityName = StringUtils.join(cls.getSimpleName().replace("Entity", "").split("(?=[A-Z])"), "_").toLowerCase();
		EntityType type = EntityType.Builder.create(factory, EntityClassification.MISC).size(width, height).build(BeastsReference.ID + ":" + entityName);
		type.setRegistryName(BeastsReference.ID, entityName);
		LIST.add(type);
		return type;
	}
	
	private static EntityType createEntry(Class cls, IFactory factory) {
		String entityName = StringUtils.join(cls.getSimpleName().replace("Entity", "").split("(?=[A-Z])"), "_")
				.toLowerCase();
		EntityType type = EntityType.Builder.create(factory, EntityClassification.MISC)
				.build(BeastsReference.ID + ":" + entityName);
		type.setRegistryName(BeastsReference.ID, entityName);
		LIST.add(type);
		return type;
	}

	private static EntityType createBranchie(Class<? extends EntityBranchieBase> cls, IFactory factory, int prim, int sec, Collection<? extends Block> validBlocks, Function<BlockEvent.BreakEvent, ? extends EntityBranchieBase> create) {
		EntityBranchieBase.TYPES.put(validBlocks, create);
		return createEntry(cls, factory, EntityClassification.CREATURE, prim, sec, 0.2F, 0.9F, null);
	}

	private static class SpawnEntry {
		int weight;
		int min;
		int max;
		Iterable<Biome> biomes;

		private SpawnEntry(int weight, int min, int max, Iterable<Biome> biomes) {
			this.weight = weight;
			this.min = min;
			this.max = max;
			this.biomes = biomes;
		}

		private SpawnEntry(int weight, int min, int max, Biome... biomes) {
			this(weight, min, max, Lists.newArrayList(biomes));
		}
	}
}
