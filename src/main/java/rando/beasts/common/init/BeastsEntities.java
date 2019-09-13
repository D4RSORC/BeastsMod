package rando.beasts.common.init;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.IFactory;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import rando.beasts.common.entity.item.EntityBeastsPainting;
import rando.beasts.common.entity.monster.EntityBranchie;
import rando.beasts.common.entity.monster.EntityCoconutCrab;
import rando.beasts.common.entity.monster.EntityGiantGardenEel;
import rando.beasts.common.entity.monster.EntitySkewerShrimp;
import rando.beasts.common.entity.monster.EntityVileEel;
import rando.beasts.common.entity.passive.EntityLandwhale;
import rando.beasts.common.entity.passive.EntityPufferfishDog;
import rando.beasts.common.entity.passive.EntityRabbitman;
import rando.beasts.common.entity.projectile.EntityCoconutBomb;
import rando.beasts.common.utils.BeastsReference;

@SuppressWarnings("unused")
public class BeastsEntities {

	public static final List<EntityType<?>> LIST = new ArrayList<>();

	public static final EntityType<EntityPufferfishDog> PUFFERFISH_DOG = createEntry(EntityPufferfishDog.class,
			EntityPufferfishDog::new, 0xFBA70C, 0x429BBA,
			new SpawnEntry(EntityClassification.CREATURE, 10, 1, 1, BeastsBiomes.DRIED_REEF), 0.5f, 0.5f);
	public static final EntityType RABBITMAN = createEntry(EntityRabbitman.class, EntityRabbitman::new, 0x4E362D,
			0xE5E5E5, null, 0.6F, 1.95F);
	public static final EntityType COCONUT_CRAB = createEntry(EntityCoconutCrab.class, EntityCoconutCrab::new, 0x3C1C11,
			0xA16745, new SpawnEntry(EntityClassification.CREATURE, 12, 1, 1, BeastsBiomes.DRIED_REEF), 0.5f, 0.4f);
	public static final EntityType BRANCHIE = createEntry(EntityBranchie.class, EntityBranchie::new, 0xEDEC4C, 0xD6549B,
			new SpawnEntry(EntityClassification.CREATURE, 15, 1, 1, BeastsBiomes.DRIED_REEF), 0.2F, 0.9F);
	public static final EntityType VILE_EEL = createEntry(EntityVileEel.class, EntityVileEel::new, 0x313337, 0x987CAF,
			new SpawnEntry(EntityClassification.MONSTER, 7, 1, 1, BeastsBiomes.DRIED_REEF), 1.5F, 1.8F);
	public static final EntityType LANDWHALE = createEntry(EntityLandwhale.class, EntityLandwhale::new, 0x587377,
			0xE25AA5, new SpawnEntry(EntityClassification.CREATURE, 6, 1, 1, BeastsBiomes.DRIED_REEF), 1.8F, 2.0F);
	public static final EntityType COCONADE = createEntry(EntityCoconutBomb.class, EntityCoconutBomb::new);
	public static final EntityType GIANT_GARDEN_EEL = createEntry(EntityGiantGardenEel.class, EntityGiantGardenEel::new,
			0xCECEAF, 0x7A745E, new SpawnEntry(EntityClassification.CREATURE, 10, 4, 8, BeastsBiomes.DRIED_REEF), 0.5f,
			2.7f);
	public static final EntityType SKEWER_SHRIMP = createEntry(EntitySkewerShrimp.class, EntitySkewerShrimp::new,
			0xEA4E3C, 0xFFACA3, new SpawnEntry(EntityClassification.CREATURE, 6, 4, 8, BeastsBiomes.DRIED_REEF), 0.5f,
			0.4f);
	public static final EntityType BEASTS_PAINTING = createEntry(EntityBeastsPainting.class, EntityBeastsPainting::new);

	private static EntityType createEntry(Class cls, IFactory factory, int prim, int sec, SpawnEntry spawn, float width,
			float height) {
		String entityName = StringUtils.join(cls.getSimpleName().replace("Entity", "").split("(?=[A-Z])"), "_")
				.toLowerCase();
		EntityType type = EntityType.Builder.create(factory, spawn == null ? EntityClassification.CREATURE : spawn.type)
				.size(width, height).build(BeastsReference.ID + ":" + entityName);
		type.setRegistryName(BeastsReference.ID, entityName);
		if (spawn != null)
			registerEntityWorldSpawn(type, spawn);
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
				biome.getSpawns(spawn.type).add(new SpawnListEntry(entity, spawn.weight, spawn.min, spawn.max));
		}
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

	private static class SpawnEntry {
		EntityClassification type;
		int weight;
		int min;
		int max;
		Iterable<Biome> biomes;

		private SpawnEntry(EntityClassification type, int weight, int min, int max, Iterable<Biome> biomes) {
			this.type = type;
			this.weight = weight;
			this.min = min;
			this.max = max;
			this.biomes = biomes;
		}

		private SpawnEntry(EntityClassification type, int weight, int min, int max, Biome... biomes) {
			this(type, weight, min, max, Lists.newArrayList(biomes));
		}
	}
}
