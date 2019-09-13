package rando.beasts.common.entity.monster;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class EntitySkewerShrimp extends MonsterEntity {

	public EntitySkewerShrimp(EntityType type, World worldIn) {
		super(type, worldIn);
		this.goalSelector.addGoal(0, new RandomWalkingGoal(this, 0.6F));
		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.6F, true));
		this.goalSelector.addGoal(2, new PanicGoal(this, 0.0D));
		this.goalSelector.addGoal(3, new SwimGoal(this));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1F);
		getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6F);
		getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.ARTHROPOD;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SPIDER_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_SPIDER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SPIDER_DEATH;
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}
}
