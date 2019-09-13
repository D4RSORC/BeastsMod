package rando.beasts.common.entity.passive;

import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import rando.beasts.client.init.BeastsSounds;
import rando.beasts.common.init.BeastsItems;

public class EntityPufferfishDog extends TameableEntity {

	private static final DataParameter<Integer> COLLAR_COLOR = EntityDataManager.createKey(EntityPufferfishDog.class,
			DataSerializers.VARINT);
	private static final DataParameter<Float> THREAT_TIME = EntityDataManager.createKey(EntityPufferfishDog.class,
			DataSerializers.FLOAT);
	private int bounces = 0;
	private BlockPos jukeboxPosition;
	private boolean partyPufferfishDog;

	public EntityPufferfishDog(EntityType type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.sitGoal = new SitGoal(this);
		this.goalSelector.addGoal(2, sitGoal);
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(2, new SwimGoal(this));
		this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 0.5, 2f, 5f) {
			@Override
			public boolean shouldExecute() {
				return !getInflated() && !isPartying() && super.shouldExecute();
			}
		});
		this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 0.5, 50) {
			@Override
			public boolean shouldExecute() {
				return !isSitting() && !isPartying() && super.shouldExecute();
			}
		});
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
	}

	@Override
	public AgeableEntity createChild(@Nonnull AgeableEntity ageable) {
		EntityPufferfishDog child = new EntityPufferfishDog(this.getType(), this.world);
		UUID uuid = this.getOwnerId();
		if (uuid != null) {
			child.setOwnerId(uuid);
			child.setTamed(true);
		}
		return child;
	}

	@Override
	public void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(16.0);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4);
	}

	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(COLLAR_COLOR, DyeColor.RED.getId());
		this.dataManager.register(THREAT_TIME, 0.0f);
	}

	protected void playStepSound(BlockPos pos, Block blockIn) {
		this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15F, 1.0F);
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putInt("collarColor", this.getCollarColor().getId());
		compound.putBoolean("sitting", this.isSitting());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setCollarColor(DyeColor.byId(compound.getInt("collarColor")));
		this.setSitting(compound.getBoolean("sitting"));
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.rand.nextInt(3) == 0
				? (this.isTamed() && getHealth() < 10.0F ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT)
				: SoundEvents.ENTITY_WOLF_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_WOLF_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_WOLF_DEATH;
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	@Override
	public void livingTick() {

		if (this.jukeboxPosition == null || !this.jukeboxPosition.withinDistance(this.getPositionVec(), 3.46D)
				|| this.world.getBlockState(this.jukeboxPosition).getBlock() != Blocks.JUKEBOX) {
			this.partyPufferfishDog = false;
			this.jukeboxPosition = null;
		}

		if (!world.isRemote) {
			if (isInWater())
				this.setAir(300);
			if (getInflated()) {
				setThreatTime(getThreatTime() + 1);
				if (onGround) {
					if (bounces == 0)
						bounces = 1;
					else
						this.addVelocity(0, 0.25 / bounces++, 0);
				} else
					this.addVelocity(0, -0.01, 0);
				if (getThreatTime() > 140)
					setInflated(false);
				for (Entity entity : this.world.getEntitiesWithinAABBExcludingEntity(this,
						this.getBoundingBox().grow(1)))
					if (entity != this.getOwner())
						entity.attackEntityFrom(DamageSource.CACTUS, 1.0F);
			} else
				bounces = 0;
		}
		super.livingTick();
	}

	@Override
	public boolean processInteract(PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (this.isTamed()) {
			if (!stack.isEmpty() && stack.getItem() instanceof DyeItem) {
				DyeColor color = ((DyeItem) stack.getItem()).getDyeColor();
				if (color != this.getCollarColor()) {
					this.setCollarColor(color);
					if (!player.abilities.isCreativeMode)
						stack.shrink(1);
					return true;
				}
			}
			if (this.isOwner(player) && !this.world.isRemote && stack.isEmpty()) {
				this.setSitting(!this.isSitting());
				this.isJumping = false;
				this.navigator.clearPath();
				return true;
			}
		} else if (stack.getItem() == BeastsItems.LEAFY_BONE) {
			if (!player.abilities.isCreativeMode)
				stack.shrink(1);
			if (this.rand.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
				this.isJumping = false;
				this.navigator.clearPath();
				this.setMotion(0, this.getMotion().y, 0);
				this.setTamedBy(player);
				this.setHealth(16.0F);
				this.setSitting(true);
				this.playTameEffect(true);
				this.world.setEntityState(this, (byte) 7);
			} else {
				this.playTameEffect(false);
				this.world.setEntityState(this, (byte) 6);
			}
			return true;
		}
		return super.processInteract(player, hand);
	}

	@Override
	public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
		if (source != DamageSource.FALL) {
			if (source.getImmediateSource() != null)
				setInflated(true);
			return super.attackEntityFrom(source, amount);
		}
		return false;
	}

	@Override
	public boolean canMateWith(@Nonnull AnimalEntity animal) {
		if (animal == this || !this.isTamed() || !(animal instanceof EntityPufferfishDog)) {
			return false;
		} else {
			EntityPufferfishDog entity = (EntityPufferfishDog) animal;
			return !(!entity.isTamed() || entity.isSitting()) && this.isInLove() && entity.isInLove();
		}
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == Items.SPIDER_EYE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void setPartying(BlockPos pos, boolean p_191987_2_) {
		this.jukeboxPosition = pos;
		this.partyPufferfishDog = p_191987_2_;
	}

	@OnlyIn(Dist.CLIENT)
	public boolean isPartying() {
		return this.partyPufferfishDog;
	}

	public DyeColor getCollarColor() {
		return DyeColor.byId(this.dataManager.get(COLLAR_COLOR) & 15);
	}

	private void setCollarColor(DyeColor color) {
		this.dataManager.set(COLLAR_COLOR, color.getId());
	}

	private float getThreatTime() {
		return this.dataManager.get(THREAT_TIME);
	}

	private void setThreatTime(float time) {
		this.dataManager.set(THREAT_TIME, time);
	}

	public boolean getInflated() {
		return getThreatTime() > 0;
	}

	private void setInflated(boolean inflated) {
		playSound(inflated ? BeastsSounds.PUFFERFISH_BLOW_UP : BeastsSounds.PUFFERFISH_BLOW_OUT, getSoundVolume(),
				getSoundPitch());
		setSitting(false);
		setNoGravity(inflated);
		setThreatTime(inflated ? 1 : 0);
		this.addVelocity(0, inflated ? 0.5 : 0, 0);
	}
}
