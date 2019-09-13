package rando.beasts.common.world.gen.structure;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.MarginedStructureStart;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class RabbitVillageGenerator extends Structure<NoFeatureConfig> {
//TODO
	public RabbitVillageGenerator(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
		super(configFactoryIn);
	}

	private static ChunkPos[] structureCoords = new ChunkPos[128];
	private static int generated = 0;

	@Override
	protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> chunkGenerator, Random random, int x, int z,
			int spacingOffsetsX, int spacingOffsetsZ) {
		int i = chunkGenerator.getSettings().getVillageDistance();
		int j = chunkGenerator.getSettings().getVillageSeparation();
		int k = x + i * spacingOffsetsX;
		int l = z + i * spacingOffsetsZ;
		int i1 = k < 0 ? k - i + 1 : k;
		int j1 = l < 0 ? l - i + 1 : l;
		int k1 = i1 / i;
		int l1 = j1 / i;
		((SharedSeedRandom) random).setLargeFeatureSeedWithSalt(chunkGenerator.getSeed(), k1, l1, 10387312);
		k1 = k1 * i;
		l1 = l1 * i;
		k1 = k1 + random.nextInt(i - j);
		l1 = l1 + random.nextInt(i - j);
		return new ChunkPos(k1, l1);
	}

	@Override
	public boolean hasStartAt(ChunkGenerator<?> chunkGen, Random rand, int chunkPosX, int chunkPosZ) {
		ChunkPos chunkpos = this.getStartPositionForPosition(chunkGen, rand, chunkPosX, chunkPosZ, 0, 0);
		if (chunkPosX == chunkpos.x && chunkPosZ == chunkpos.z) {
			Biome biome = chunkGen.getBiomeProvider()
					.getBiome(new BlockPos((chunkPosX << 4) + 9, 0, (chunkPosZ << 4) + 9));
			return chunkGen.hasStructure(biome, Feature.VILLAGE);
		} else {
			return false;
		}
	}

	@Override
	public IStartFactory getStartFactory() {
		return RabbitVillageGenerator.Start::new;
	}

	@Override
	public String getStructureName() {
		return "RabbitVillage";
	}

	@Override
	public int getSize() {
		return 8;
	}

	public static class Start extends MarginedStructureStart {
		private boolean hasMoreThanTwoComponents;

		@SuppressWarnings("unused")
		public Start(Structure<?> structureIn, int chunkX, int chunkZ, Biome biomeIn, MutableBoundingBox boundsIn,
				int referenceIn, long seed) {
			super(structureIn, chunkX, chunkZ, biomeIn, boundsIn, referenceIn, seed);
		}

//        Start(World worldIn, Random rand, int x, int z) {
//        	
//            super(x, z);
//            List<StructureRabbitVillagePieces.PieceWeight> list = StructureRabbitVillagePieces.getStructureVillageWeightedPieceList(rand);
//            StructureRabbitVillagePieces.Start start = new StructureRabbitVillagePieces.Start(worldIn.getChunkProvider(), rand, (x << 4) + 2, (z << 4) + 2, list, 2);
//            this.components.add(start);
//            start.buildComponent(start, this.components, rand);
//            List<StructureComponent> list1 = start.pendingRoads;
//            List<StructureComponent> list2 = start.pendingHouses;
//
//            while (!list1.isEmpty() || !list2.isEmpty()) {
//                if (list1.isEmpty()) {
//                    int i = rand.nextInt(list2.size());
//                    StructureComponent component = list2.remove(i);
//                    component.buildComponent(start, this.components, rand);
//                } else {
//                    int j = rand.nextInt(list1.size());
//                    StructureComponent component = list1.remove(j);
//                    component.buildComponent(start, this.components, rand);
//                }
//            }
//
//            this.updateBoundingBox();
//            int k = 0;
//            for (StructureComponent component : this.components) if (!(component instanceof StructureRabbitVillagePieces.Path)) ++k;
//            this.hasMoreThanTwoComponents = k > 2;
//        }

		@Override
		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ,
				Biome biomeIn) {

		}
	}
}
