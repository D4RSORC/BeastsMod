package rando.beasts.common.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rando.beasts.client.init.BeastsSounds;
import rando.beasts.common.entity.IDriedAquatic;
import rando.beasts.common.entity.passive.EntityLandwhale;

public class EntityVileEel extends MonsterEntity implements IDriedAquatic {
	public EntityVileEel(EntityType type, World worldIn) {
		super(type, worldIn);
	}

	protected void initEntityAI() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> !(entity instanceof EntityLandwhale)));
		this.goalSelector.addGoal(11, new MeleeAttackGoal(this, 1.1D, true));
		this.goalSelector.addGoal(12, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(13, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(14, new LookRandomlyGoal(this));
	}
	
	public void tick() {
        super.tick();
        if (!this.world.isRemote && this.world.isDaytime()) this.remove();
    }
	
	 @Override
    public void livingTick() {
        if(this.getRidingEntity() != null) getRidingEntity().attackEntityFrom(DamageSource.causeMobDamage(this), 1);
        super.livingTick();
    }

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return BeastsSounds.VILE_EEL_AMBIENT;
	}

	protected void playStepSound(BlockPos pos, Block blockIn) {
		this.playSound(SoundEvents.ENTITY_ZOMBIE_STEP, 0.15F, 1.0F);
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return this.isChild() ? this.getHeight() : 1.3F;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        entityIn.startRiding(this, true);
        return super.attackEntityAsMob(entityIn);
    }
}