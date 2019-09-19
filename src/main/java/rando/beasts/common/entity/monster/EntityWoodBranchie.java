package rando.beasts.common.entity.monster;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import rando.beasts.common.init.BeastsEntities;

public class EntityWoodBranchie extends EntityBranchieBase {

    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityWoodBranchie.class, DataSerializers.VARINT);

    public EntityWoodBranchie(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(VARIANT, 0);
    }

    @Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, ILivingEntityData spawnDataIn, CompoundNBT dataTag) {
    	spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setVariant(LogType.getRandom(rand));
        return spawnDataIn;
	}

	private void setVariant(LogType variant) {
        this.dataManager.set(VARIANT, variant.ordinal());
    }

    public LogType getVariant() {
        return LogType.values()[this.dataManager.get(VARIANT)];
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("variant", this.getVariant().ordinal());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setVariant(LogType.values()[compound.getInt("variant")]);
    }

    public static EntityWoodBranchie create(BlockEvent.BreakEvent event) {
        EntityWoodBranchie entity = new EntityWoodBranchie(BeastsEntities.WOOD_BRANCHIE, event.getWorld().getWorld());
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        entity.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
        entity.onInitialSpawn(event.getWorld(), event.getWorld().getDifficultyForLocation(pos), SpawnReason.EVENT, null, entity.getPersistantData());
        entity.setVariant(LogType.fromBlock(state.getBlock()));
        return entity;
    }
    
    public static enum LogType{
    	ACACIA(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG),
    	BIRCH(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG),
    	DARK_OAK(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG),
    	JUNGLE(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG),
    	OAK(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG),
    	SPRUCE(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG);
    	private Block log;
    	private Block stripped_log;
    	
    	private LogType(Block log, Block stripped_log) {
    		this.log = log;
    		this.stripped_log = stripped_log;
    	}
    	
    	public static LogType fromBlock(Block block) {
    		Optional<LogType> types = Arrays.stream(LogType.values()).filter(type -> type.log == block || type.stripped_log == block).findFirst();
    		return types.isPresent() ? types.get() : ACACIA;
    	}
    	
    	public static LogType getRandom(Random rand) {
    		return values()[rand.nextInt(LogType.values().length)];
    	}
    	
    	public static List<Block> getLogBlocks() {
    		List blocks = Arrays.stream(LogType.values()).map(type -> type.log).collect(Collectors.toList());
    		blocks.addAll(Arrays.stream(LogType.values()).map(type -> type.stripped_log).collect(Collectors.toList()));
    		return blocks;
    	}

		public String getName() {
			return name().toLowerCase();
		}
    }
}
