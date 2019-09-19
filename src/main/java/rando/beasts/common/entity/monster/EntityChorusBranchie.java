package rando.beasts.common.entity.monster;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import rando.beasts.common.init.BeastsEntities;

public class EntityChorusBranchie extends EntityBranchieBase {
	public EntityChorusBranchie(EntityType<EntityBranchieBase> type, World worldIn) {
        super(type, worldIn);
    }

    public static EntityChorusBranchie create(BlockEvent.BreakEvent event) {
    	IWorld world = event.getWorld();
        EntityChorusBranchie entity = new EntityChorusBranchie(BeastsEntities.CHORUS_BRANCHIE,world.getWorld());
        BlockPos pos = event.getPos();
        entity.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
        entity.onInitialSpawn(world,world.getDifficultyForLocation(pos), SpawnReason.EVENT, null, null);
        return entity;
    }
}
