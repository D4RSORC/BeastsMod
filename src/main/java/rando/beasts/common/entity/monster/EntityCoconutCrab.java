package rando.beasts.common.entity.monster;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import rando.beasts.common.init.BeastsBlocks;

public class EntityCoconutCrab extends MonsterEntity {

	private static final DataParameter<Boolean> OUT = EntityDataManager.createKey(EntityCoconutCrab.class,
			DataSerializers.BOOLEAN);
	private static final float defaultHeight = 0.4f;
	private boolean hasTarget = false;
	private int ticksSinceHit = 0;

	public EntityCoconutCrab(EntityType<EntityCoconutCrab> type, World worldIn) {
		super(type, worldIn);
		this.goalSelector.addGoal(0, new RandomWalkingGoal(this, 0.5, 50) {
			@Override
			public boolean shouldExecute() {
				return isOut() && super.shouldExecute();
			}
		});
		this.goalSelector.addGoal(0, new NearestAttackableTargetGoal<PlayerEntity>(this, PlayerEntity.class, true) {
			@Override
			public boolean shouldExecute() {
				return isOut() && super.shouldExecute();
			}
		});
		this.setNoAI(true);
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2);
		getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2);
		getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10);
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
		this.setNoAI(!out);
	}

	@Override
	public EntitySize getSize(Pose poseIn) {
		return isOut() ? new EntitySize(this.getWidth(), defaultHeight + 0.2f, false)
				: super.getSize(poseIn).scale(this.getRenderScale());
	}

	@Override
	public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {
		if (isOut())
			super.knockBack(entityIn, strength, xRatio, zRatio);
	}

	@Override
	public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
		if ((isOut() || source == DamageSource.OUT_OF_WORLD)) {
			if (source.getImmediateSource() != null)
				attackEntityAsMob(source.getImmediateSource());
			return super.attackEntityFrom(source, amount);
		}
		return false;
	}

	@Override
	public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
		if (rand.nextInt(4) == 0)
			return this.getBlockPathWeight(new BlockPos(this.posX, this.getBoundingBox().minY, this.posZ)) >= 0.0F
					&& world.getBlockState(getPosition()).canEntitySpawn(worldIn, getPosition(), this.getType())
					&& this.world.getDifficulty() != Difficulty.PEACEFUL;
		world.setBlockState(getPosition(), BeastsBlocks.COCONUT.getDefaultState());
		return false;
	}

	@Override
	protected boolean processInteract(PlayerEntity player, Hand hand) {
		if (!isOut()) {
			setOut(true);
			if (!player.abilities.isCreativeMode) {
				setAttackTarget(player);
				attackEntityAsMob(player);
			}
			return true;
		}
		return super.processInteract(player, hand);
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putBoolean("out", this.isOut());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setOut(compound.getBoolean("out"));
	}

	@Override
	public void tick() {
		if (isOut()) {
			super.tick();
			if (world.getBlockState(getPosition().down()).getBlock() == Blocks.SAND && rand.nextInt(500) == 0) {
				setOut(false);
				setFire(0);
				for (int i = 0; i < 9; ++i) {
					double d0 = this.posX + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
					double d1 = this.posY + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
					double d2 = this.posZ + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
					this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, Blocks.SAND.getDefaultState()),
							d0, d1, d2, this.rand.nextDouble() * 0.1, this.rand.nextDouble() * 0.1,
							this.rand.nextDouble() * 0.1);
				}
				return;
			}

			if (this.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
				List<ItemEntity> list = this.world.getEntitiesWithinAABB(ItemEntity.class,
						this.getBoundingBox().grow(8.0D));
				double d0 = Double.MAX_VALUE;
				ItemEntity item = null;

				for (ItemEntity itm : list)
					if (itm.getItem().getItem() instanceof SwordItem) {
						if (this.getDistanceSq(itm) < d0) {
							item = itm;
							d0 = this.getDistanceSq(itm);
						}
					}

				if (item != null && item.isAlive()) {
					this.setHeldItem(Hand.MAIN_HAND, item.getItem());
					item.remove();
				}
			}

			if (this.getAttackTarget() != null) {
				if (getDistance(getAttackTarget()) < 1.2 && ticksSinceHit == 0) {
					attackEntityAsMob(getAttackTarget());
					if (!getAttackTarget().isAlive()) {
						ticksSinceHit = 0;
						hasTarget = false;
						setAttackTarget(null);
					} else
						ticksSinceHit = 1;
				} else if (!hasTarget || getDistance(getAttackTarget()) > 1.3) {
					this.navigator.tryMoveToXYZ(getAttackTarget().posX, getAttackTarget().posY, getAttackTarget().posZ,
							2.2);
					this.hasTarget = true;
				}
			}
		} else {
			if (!this.world.isRemote && this.world.getDifficulty() == Difficulty.PEACEFUL)
				this.remove();
			if (this.newPosRotationIncrements > 0 && !this.canPassengerSteer())
				this.setPosition(posX, this.posY + (this.interpTargetY - this.posY), posZ);
			this.moveStrafing *= 0.98F;
			this.moveForward *= 0.98F;
			this.travel(new Vec3d(this.moveStrafing, this.moveVertical, this.moveForward));
			this.addVelocity(0, -0.02, 0);
		}
	}
}
