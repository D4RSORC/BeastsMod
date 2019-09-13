package rando.beasts.common.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rando.beasts.client.init.BeastsSounds;
import rando.beasts.common.entity.passive.EntityPufferfishDog;

public class EntityVileEel extends MonsterEntity {
	public EntityVileEel(EntityType type, World worldIn) {
		super(type, worldIn);
	}

	protected void initEntityAI() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, CowEntity.class, true));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PigEntity.class, true));
		this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, SheepEntity.class, true));
		this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, ChickenEntity.class, true));
		this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, HorseEntity.class, true));
		this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, MooshroomEntity.class, true));
		this.targetSelector.addGoal(8, new NearestAttackableTargetGoal<>(this, EntityPufferfishDog.class, true));
		this.targetSelector.addGoal(9, new NearestAttackableTargetGoal<>(this, EntityCoconutCrab.class, true));
		this.goalSelector.addGoal(11, new MeleeAttackGoal(this, 1.1D, true));
		this.goalSelector.addGoal(12, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(13, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(14, new LookRandomlyGoal(this));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
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
}