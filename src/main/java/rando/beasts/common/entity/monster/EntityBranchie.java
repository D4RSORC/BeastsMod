package rando.beasts.common.entity.monster;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import rando.beasts.client.init.BeastsSounds;
import rando.beasts.common.block.CoralColor;

public class EntityBranchie extends CreatureEntity {

	private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityBranchie.class,
			DataSerializers.VARINT);

	public EntityBranchie(EntityType type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new WaterAvoidingRandomWalkingGoal(this, 0.5D));
		this.goalSelector.addGoal(2, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
	}

	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(VARIANT, 0);
	}

	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason,
			@Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
		this.setVariant(CoralColor.getRandom(rand));
		return spawnDataIn;
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(12.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
	}

	public void setVariant(CoralColor variant) {
		this.dataManager.set(VARIANT, variant.ordinal());
	}

	public CoralColor getVariant() {
		return CoralColor.values()[this.dataManager.get(VARIANT)];
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(SoundEvents.ENTITY_CHICKEN_STEP, 0.15F, 1.0F);
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		scream();
		return super.attackEntityAsMob(entityIn);
	}

	public void scream() {
		playSound(BeastsSounds.BRANCHIE_SCREAM, getSoundVolume(), getSoundPitch());
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putInt("variant", this.getVariant().ordinal());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setVariant(CoralColor.values()[compound.getInt("variant")]);
	}
}
