package rando.beasts.common.init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import rando.beasts.common.entity.item.EntityBeastsPainting;
import rando.beasts.common.entity.item.EntityFallingCoconut;
import rando.beasts.common.entity.monster.EntityBranchieBase;
import rando.beasts.common.entity.monster.EntityChorusBranchie;
import rando.beasts.common.entity.monster.EntityCoconutCrab;
import rando.beasts.common.entity.monster.EntityCoralBranchie;
import rando.beasts.common.entity.monster.EntityGiantGardenEel;
import rando.beasts.common.entity.monster.EntitySkewerShrimp;
import rando.beasts.common.entity.monster.EntityVileEel;
import rando.beasts.common.entity.monster.EntityWhippingBarnacle;
import rando.beasts.common.entity.monster.EntityWoodBranchie;
import rando.beasts.common.entity.passive.EntityHermitTurtle;
import rando.beasts.common.entity.passive.EntityLandwhale;
import rando.beasts.common.entity.passive.EntityPufferfishDog;
import rando.beasts.common.entity.passive.EntityRabbitman;
import rando.beasts.common.entity.projectile.EntityCoconutBomb;
import rando.beasts.common.utils.BeastsReference;

@SuppressWarnings("unused")
public class BeastsEntities {

	public static final List<EntityEntry> LIST = new ArrayList<>();

	private static int entityId = 0;
	private static final EntityEntry PUFFERFISH_DOG = createEntry(EntityPufferfishDog.class, 0xFBA70C, 0x429BBA, new SpawnEntry(EnumCreatureType.CREATURE, 10, 1, 1, BeastsBiomes.DRIED_REEF));
	private static final EntityEntry RABBITMAN = createEntry(EntityRabbitman.class, 0x4E362D, 0xE5E5E5);
	private static final EntityEntry COCONUT_CRAB = createEntry(EntityCoconutCrab.class, 0x3C1C11, 0xA16745);
	private static final EntityEntry CORAL_BRANCHIE = createBranchie(EntityCoralBranchie.class, 0xEDEC4C, 0xD6549B, BeastsBlocks.CORAL_PLANTS.values(), EntityCoralBranchie::create);
	private static final EntityEntry CHORUS_BRANCHIE = createBranchie(EntityChorusBranchie.class, 0x401A40, 0xEED6EE, Collections.singletonList(Blocks.CHORUS_PLANT), EntityChorusBranchie::create);
	private static final EntityEntry WOOD_BRANCHIE = createBranchie(EntityWoodBranchie.class, 0x745A36, 0x57AD3F, Arrays.asList(Blocks.LOG, Blocks.LOG2), EntityWoodBranchie::create);
	private static final EntityEntry VILE_EEL = createEntry(EntityVileEel.class, 0x313337, 0x987CAF, new SpawnEntry(EnumCreatureType.MONSTER, 7, 1, 1, BeastsBiomes.DRIED_REEF));
	private static final EntityEntry LANDWHALE = createEntry(EntityLandwhale.class, 0x587377, 0xE25AA5, new SpawnEntry(EnumCreatureType.CREATURE, 6, 1, 1, BeastsBiomes.DRIED_REEF));
	private static final EntityEntry COCONADE = createEntry(EntityCoconutBomb.class);
	private static final EntityEntry GIANT_GARDEN_EEL = createEntry(EntityGiantGardenEel.class, 0xCECEAF, 0x7A745E, new SpawnEntry(EnumCreatureType.CREATURE, 10, 4, 8, BeastsBiomes.DRIED_REEF));
	private static final EntityEntry SKEWER_SHRIMP = createEntry(EntitySkewerShrimp.class, 0xEA4E3C, 0xFFACA3, new SpawnEntry(EnumCreatureType.CREATURE, 6, 4, 8, BeastsBiomes.DRIED_REEF));
	private static final EntityEntry BEASTS_PAINTING = createEntry(EntityBeastsPainting.class);
	private static final EntityEntry FALLING_COCONUT = createEntry(EntityFallingCoconut.class);
	private static final EntityEntry WHIPPING_BARNACLE = createEntry(EntityWhippingBarnacle.class, 0x278E6C, 0xEA8F9E, new SpawnEntry(EnumCreatureType.CREATURE, 50, 1, 3, BeastsBiomes.DRIED_REEF));
	private static final EntityEntry HERMIT_TURTLE = createEntry(EntityHermitTurtle.class, 0x8F8F8F, 0x63862E, new SpawnEntry(EnumCreatureType.CREATURE, 50, 1, 3, BeastsBiomes.DRIED_REEF));

	private static EntityEntry createBranchie(Class<? extends EntityBranchieBase> cls, int prim, int sec, Collection<? extends Block> validBlocks, Function<BlockEvent.BreakEvent, ? extends EntityBranchieBase> create) {
		EntityBranchieBase.TYPES.put(validBlocks, create);
		return createEntry(cls, prim, sec);
	}

	private static EntityEntry createEntry(Class<? extends Entity> cls, int prim, int sec, SpawnEntry... spawns) {
		EntityEntryBuilder builder = getBuilder(cls).egg(prim, sec);
		for(SpawnEntry spawn : spawns) builder.spawn(spawn.type, spawn.weight, spawn.min, spawn.max, spawn.biomes);
		EntityEntry entry = builder.build();
		LIST.add(entry);
		return entry;
	}

	private static EntityEntry createEntry(Class<? extends Entity> cls) {
		EntityEntry entry = getBuilder(cls).build();
		LIST.add(entry);
		return entry;
	}

	private static EntityEntryBuilder getBuilder(Class<? extends Entity> cls) {
		String entityName = StringUtils.join(cls.getSimpleName().replace("Entity", "").split("(?=[A-Z])"), "_").toLowerCase();
		return EntityEntryBuilder.create().entity(cls).id(new ResourceLocation(BeastsReference.ID, entityName), BeastsEntities.entityId++).name(entityName).tracker(90, 1, true);
	}

	private static class SpawnEntry {
		EnumCreatureType type;
		int weight;
		int min;
		int max;
		Iterable<Biome> biomes;

		private SpawnEntry(EnumCreatureType type, int weight, int min, int max, Iterable<Biome> biomes) {
			this.type = type;
			this.weight = weight;
			this.min = min;
			this.max = max;
			this.biomes = biomes;
		}

		private SpawnEntry(EnumCreatureType type, int weight, int min, int max, Biome... biomes) {
			this(type, weight, min, max, Arrays.asList(biomes));
		}
	}
}
