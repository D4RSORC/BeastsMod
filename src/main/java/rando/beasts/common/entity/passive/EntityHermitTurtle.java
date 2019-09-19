package rando.beasts.common.entity.passive;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.item.Item;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import rando.beasts.common.init.BeastsBlocks;

public class EntityHermitTurtle extends CreatureEntity {
    private static final DataParameter<Boolean> OUT = EntityDataManager.createKey(EntityHermitTurtle.class, DataSerializers.BOOLEAN);

    public EntityHermitTurtle(EntityType type, World worldIn) {
        super(type, worldIn);
        this.goalSelector.addGoal(0, new RandomWalkingGoal(this, 0.2, 200) {
            @Override
            public boolean shouldExecute() {
                return isOut() && super.shouldExecute();
            }
        });
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(OUT, false);
    }

    public boolean isOut() {
        return this.dataManager.get(OUT);
    }

    private void setOut(boolean out) {
        this.dataManager.set(OUT, out);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1F);
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12);
    }
}
