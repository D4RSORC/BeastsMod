package rando.beasts.common.entity.passive;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.entity.ai.goal.OpenDoorGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.merchant.villager.VillagerTrades.ITrade;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import rando.beasts.common.init.BeastsItems;

public class EntityRabbitman extends AbstractVillagerEntity {

	public static final int VARIANTS = 6;
	private static final Trade[][] TRADES = new Trade[][] {
			{ new MoneyTrade(Items.LEATHER_BOOTS, 3, 1), new MoneyTrade(Items.LEATHER_LEGGINGS, 4, 1),
					new MoneyTrade(Items.LEATHER_CHESTPLATE, 4, 1), new MoneyTrade(Items.LEATHER_HELMET, 3, 1) },
			{ new Trade(Items.CARROT, BeastsItems.CARROT_COIN, 10, 3), new MoneyTrade(Items.CARROT, 3, 10),
					new Trade(Items.WOODEN_HOE, BeastsItems.CARROT_COIN, 1, 2),
					new MoneyTrade(Items.WOODEN_HOE, 2, 1) },
			{ new Trade(Items.GOLDEN_CARROT, BeastsItems.CARROT_COIN, 2, 2), new MoneyTrade(Items.GOLDEN_CARROT, 2, 2),
					new Trade(Items.BLAZE_ROD, BeastsItems.CARROT_COIN, 3, 5),
					new MoneyTrade(Items.GOLDEN_CARROT, 5, 3),
					new MoneyTrade(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.LEAPING), 10,
							1) },
			{ new Trade(Items.PAPER, BeastsItems.CARROT_COIN, 10, 3), new MoneyTrade(Items.PAPER, 3, 10),
					new Trade(Items.BOOK, BeastsItems.CARROT_COIN, 5, 3), new MoneyTrade(Items.BOOK, 3, 5) },
			{ new Trade(Items.RABBIT, BeastsItems.CARROT_COIN, 2, 1), new MoneyTrade(Items.RABBIT, 1, 2),
					new Trade(Items.RABBIT_FOOT, BeastsItems.CARROT_COIN, 1, 5),
					new MoneyTrade(Items.RABBIT_FOOT, 5, 1) },
			{ new MoneyTrade(BeastsItems.DIAMOND_CARROT, 4, 1) } };
	private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(EntityRabbitman.class,
			DataSerializers.VARINT);
	public boolean hasTarget = false;
	private int randomTickDivider;
	private boolean isMating;
	private int timeUntilReset;
	private boolean needsInitialization;
	private boolean isWillingToMate;
	private int wealth;
	private final Inventory inv;
	private int trades = 1;
	private Trade[] variantTrades;
	private int ticksSinceHit = 0;

	public EntityRabbitman(EntityType type, World worldIn) {
		super(type, worldIn);
		this.inv = new Inventory(8);
		((GroundPathNavigator) this.getNavigator()).setBreakDoors(true);
		this.experienceValue = 2;
	}

	@Override
	protected void populateTradeData() {
		ITrade[] trades = TRADES[getVariant()];
		this.addTrades(this.getOffers(), trades, 5);

	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<OcelotEntity>(this, OcelotEntity.class, 8.0F, 0.6D, 0.6D));
		this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, WolfEntity.class, 8.0F, 0.8D, 0.8D));
		// this.goalSelector.addGoal(2, new EntityAIMoveIndoors(this));
		// this.goalSelector.addGoal(3, new EntityAIRestrictOpenDoor(this));
		this.goalSelector.addGoal(4, new OpenDoorGoal(this, true));
		this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 0.6D));
		this.goalSelector.addGoal(6, new AIMate());
		this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(9, new AIInteract());
		this.goalSelector.addGoal(9, new WaterAvoidingRandomWalkingGoal(this, 0.6D));
		this.goalSelector.addGoal(10, new LookAtGoal(this, LivingEntity.class, 8.0F));
	}

	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(VARIANT, 0);
	}

	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficulty, SpawnReason reason,
			@Nullable ILivingEntityData livingdata, @Nullable CompoundNBT dataTag) {
		final int variant = rand.nextInt(Math.round(50 / (difficulty.getAdditionalDifficulty() + 1))) == 0 ? 5
				: this.rand.nextInt(VARIANTS - 1);
		Trade[] trades = TRADES[variant];
		int currentIndex = 3, randomIndex;
		Trade temporaryValue;
		livingdata = super.onInitialSpawn(worldIn, difficulty, reason, livingdata, dataTag);
		this.setVariant(variant);
		while (currentIndex != 0) {
			randomIndex = rand.nextInt(currentIndex);
			currentIndex -= 1;
			if (currentIndex < trades.length - 1) {
				temporaryValue = trades[currentIndex];
				trades[currentIndex] = trades[randomIndex];
				trades[randomIndex] = temporaryValue;
			}
		}
		this.variantTrades = trades;
		for (int i = 0; i < 2; i++)
			if (variantTrades.length - 1 >= i)
				variantTrades[i].addMerchantRecipe(this, this.getOffers(), rand);
		return livingdata;
	}

	@Nullable
	@Override
	public AgeableEntity createChild(@Nonnull AgeableEntity ageable) {
		if (ageable instanceof EntityRabbitman) {
			EntityRabbitman child = new EntityRabbitman(this.getType(), this.world);
			child.onInitialSpawn(world, this.world.getDifficultyForLocation(child.getPosition()), SpawnReason.BREEDING,
					null, null);
			child.setVariant(
					new int[] { this.getVariant(), ((EntityRabbitman) ageable).getVariant() }[rand.nextInt(2)]);
			return child;
		}
		return null;
	}

	@Override
	protected void func_213713_b(MerchantOffer recipe) {
		recipe.func_222219_j();
		this.livingSoundTime = -this.getTalkInterval();
		this.playSound(SoundEvents.ENTITY_RABBIT_JUMP, this.getSoundVolume(), this.getSoundPitch());
		int i = 3 + this.rand.nextInt(4);

		if (recipe.func_222213_g() == 1 || this.rand.nextInt(5) == 0) {
			this.timeUntilReset = 40;
			this.needsInitialization = true;
			this.isWillingToMate = true;
			i += 5;
		}

		if (recipe.func_222200_d().getItem() == BeastsItems.CARROT_COIN)
			this.wealth += recipe.func_222200_d().getCount();
		if (recipe.func_222221_q())
			this.world.addEntity(new ExperienceOrbEntity(this.world, this.posX, this.posY + 0.5D, this.posZ, i));
	}

	@Override
	public void verifySellingItem(@Nonnull ItemStack stack) {
		if (!this.world.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20) {
			this.livingSoundTime = -this.getTalkInterval();
			this.playSound(stack.isEmpty() ? SoundEvents.ENTITY_RABBIT_HURT : SoundEvents.ENTITY_RABBIT_JUMP,
					this.getSoundVolume(), this.getSoundPitch());
		}
	}

	@Nonnull
	@Override
	public World getWorld() {
		return this.world;
	}

	public int getVariant() {
		return this.dataManager.get(VARIANT);
	}

	private void setVariant(int variant) {
		this.dataManager.set(VARIANT, variant);
	}

	private boolean getIsWillingToMate(boolean updateFirst) {
		if (!this.isWillingToMate && updateFirst && this.hasEnoughItems(false)) {
			boolean flag = false;
			for (int i = 0; i < this.inv.getSizeInventory(); ++i) {
				ItemStack itemstack = this.inv.getStackInSlot(i);

				if (!itemstack.isEmpty()) {
					if (itemstack.getItem() == BeastsItems.CARROT_COIN && itemstack.getCount() >= 3) {
						flag = true;
						this.inv.decrStackSize(i, 3);
					}
				}

				if (flag) {
					this.world.setEntityState(this, (byte) 18);
					this.isWillingToMate = true;
					break;
				}
			}
		}

		return this.isWillingToMate;
	}

	private void resetWillingToMate() {
		isWillingToMate = false;
	}

	private boolean isMating() {
		return this.isMating;
	}

	private void setMating(boolean mating) {
		this.isMating = mating;
	}

	private boolean hasEnoughItems(boolean multiple) {
		for (int i = 0; i < this.inv.getSizeInventory(); ++i) {
			ItemStack stack = this.inv.getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() == BeastsItems.CARROT_COIN
					&& stack.getCount() >= (multiple ? 6 : 3))
				return true;
		}
		return false;
	}

	private boolean isFarmItemInInventory() {
		for (int i = 0; i < this.inv.getSizeInventory(); ++i) {
			ItemStack stack = this.inv.getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() == BeastsItems.CARROT_COIN)
				return true;
		}
		return false;
	}

	@Override
	protected void onGrowingAdult() {
		if (getVariant() == 1)
			this.goalSelector.addGoal(8, new AIHarvestCarrots());
		super.onGrowingAdult();
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.18);
	}

	@Override
	protected void updateAITasks() {
		if (--this.randomTickDivider <= 0) {
			BlockPos blockpos = new BlockPos(this);
			this.randomTickDivider = 70 + this.rand.nextInt(50);
		}

		if (getCustomer() == null && this.timeUntilReset > 0) {
			--this.timeUntilReset;
			if (this.timeUntilReset <= 0) {
				if (this.needsInitialization) {
					for (MerchantOffer merchantrecipe : this.offers)
						if (merchantrecipe.func_222217_o())
							merchantrecipe.func_222203_h();
					this.needsInitialization = false;
				}
				this.addPotionEffect(new EffectInstance(Effects.REGENERATION, 200, 0));
				trades++;
				for (int i = 2 * (trades - 1); i < 2 * trades; i++)
					if (variantTrades.length - 1 >= i)
						variantTrades[i].addMerchantRecipe(this, offers, rand);
			}
		}
		super.updateAITasks();
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.world.isRemote && this.getVariant() == 5 && this.world.getDifficulty() == Difficulty.PEACEFUL)
			this.remove();
	}

	@Override
	public void livingTick() {
		super.livingTick();
		if (!world.isRemote) {
			if (!this.isChild() && this.getAttackTarget() == null && this.getVariant() == 5) {
				List<EntityRabbitman> list = world.getEntitiesWithinAABB(EntityRabbitman.class,
						this.getBoundingBox().grow(32), target -> Objects.requireNonNull(target).getVariant() != 5);
				if (list.isEmpty()) {
					PlayerEntity player = null;
					for (PlayerEntity p : world.getEntitiesWithinAABB(PlayerEntity.class,
							this.getBoundingBox().grow(26)))
						if (player == null || (p.isInvulnerableTo(DamageSource.causeMobDamage(this))
								&& p.getDistance(this) < player.getDistance(this)))
							player = p;
					if (player != null) {
						this.setAttackTarget(player);
						this.setHeldItem(Hand.MAIN_HAND, new ItemStack(BeastsItems.DIAMOND_CARROT));
						List<EntityRabbitman> innocents = world.getEntitiesWithinAABB(EntityRabbitman.class,
								this.getBoundingBox().grow(36), target -> target != this);
						for (EntityRabbitman innocent : innocents)
							innocent.goalSelector.addGoal(0, new AvoidEntityGoal<EntityRabbitman>(this,
									EntityRabbitman.class, 48, 0.25, 0.5, input -> input == this));
					}
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
			if (ticksSinceHit != 0 && ticksSinceHit < 20)
				ticksSinceHit++;
			else
				ticksSinceHit = 0;
		}
	}

	@Override
	public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
		return (this.getVariant() != 5 || this.world.getDifficulty() != Difficulty.PEACEFUL)
				&& super.canSpawn(worldIn, spawnReasonIn);
	}
	// TODO opening gui crashes mc
	@Override
	public boolean processInteract(PlayerEntity player, @Nonnull Hand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		boolean flag = itemstack.getItem() == Items.NAME_TAG;
		if (flag) {
			itemstack.interactWithEntity(player, this, hand);
			return true;
		} else if (itemstack.getItem() != BeastsItems.EGGS.get(this.getType()) && this.isAlive()
				&& !this.func_213716_dX() && !this.isChild() && !player.isSneaking()) {
			if (hand == Hand.MAIN_HAND)
				player.addStat(Stats.TALKED_TO_VILLAGER);
			if (this.offers.isEmpty())
				return super.processInteract(player, hand);
			else {
				if (!this.world.isRemote && this.getAttackTarget() == null) {
					this.setCustomer(player);
					this.func_213707_a(player, this.getDisplayName(), 1);
				}
				return true;
			}
		} else
			return super.processInteract(player, hand);
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putInt("variant", this.getVariant());
		compound.putInt("Riches", this.wealth);
		compound.putBoolean("Willing", this.isWillingToMate);
		ListNBT list = new ListNBT();
		for (int i = 0; i < this.inv.getSizeInventory(); ++i) {
			ItemStack stack = this.inv.getStackInSlot(i);
			if (!stack.isEmpty())
				list.add(stack.write(new CompoundNBT()));

		}
		compound.put("Inventory", list);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setVariant(compound.getInt("variant"));
		this.wealth = compound.getInt("Riches");
		this.isWillingToMate = compound.getBoolean("Willing");
		if (compound.contains("Offers", 10)) {
			CompoundNBT nbttagcompound = compound.getCompound("Offers");
		}
		ListNBT list = compound.getList("Inventory", 10);
		for (int i = 0; i < list.size(); ++i) {
			ItemStack stack = ItemStack.read(list.getCompound(i));
			if (!stack.isEmpty())
				this.inv.addItem(stack);
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_RABBIT_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_RABBIT_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_RABBIT_DEATH;
	}

	@Override
	public boolean attackEntityAsMob(@Nonnull Entity entityIn) {
		if (this.getVariant() == 5) {
			this.playSound(SoundEvents.ENTITY_RABBIT_ATTACK, 1.0F,
					(this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 8.0F);
		} else
			return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
	}

	@Override
	@Nonnull
	public SoundCategory getSoundCategory() {
		return this.getVariant() == 5 ? SoundCategory.HOSTILE : SoundCategory.NEUTRAL;
	}

	private class AIMate extends Goal {

		private final EntityRabbitman entity;
		private EntityRabbitman mate;
		private final World world;
		private int matingTimeout;

		AIMate() {
			this.entity = EntityRabbitman.this;
			this.world = entity.world;
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		}

		@Override
		public boolean shouldExecute() {
			EntityRabbitman entity = this.world.getClosestEntityWithinAABB(EntityRabbitman.class,
					new EntityPredicate().setLineOfSiteRequired(), this.entity, this.entity.posX, this.entity.posY,
					this.entity.posZ, this.entity.getBoundingBox().grow(8.0D, 3.0D, 8.0D));
			this.mate = entity;
			return this.entity.getGrowingAge() == 0 && this.entity.getRNG().nextInt(500) == 0
					&& this.entity.getIsWillingToMate(true) && entity != null && this.mate.getGrowingAge() == 0
					&& this.mate.getIsWillingToMate(true);
		}

		@Override
		public void startExecuting() {
			this.matingTimeout = 300;
			this.entity.setMating(true);
		}

		@Override
		public void resetTask() {
			this.mate = null;
			this.entity.setMating(false);
		}

		@Override
		public boolean shouldContinueExecuting() {
			return this.matingTimeout >= 0 && this.canBreed() && this.entity.getGrowingAge() == 0
					&& this.entity.getIsWillingToMate(false);
		}

		@Override
		public void tick() {
			--this.matingTimeout;
			this.entity.getLookController().setLookPositionWithEntity(this.mate, 10.0F, 30.0F);
			if (this.entity.getDistanceSq(this.mate) > 2.25D)
				this.entity.getNavigator().tryMoveToEntityLiving(this.mate, 0.25D);
			else if (this.matingTimeout == 0 && this.mate.isMating())
				this.giveBirth();
			if (this.entity.getRNG().nextInt(35) == 0)
				this.world.setEntityState(this.entity, (byte) 12);
		}

		public boolean canBreed() {
			// TODO
			return true;
		}

		private void giveBirth() {
			AgeableEntity child = this.entity.createChild(this.mate);
			this.mate.setGrowingAge(6000);
			this.entity.setGrowingAge(6000);
			this.mate.resetWillingToMate();
			this.entity.resetWillingToMate();
			final BabyEntitySpawnEvent event = new BabyEntitySpawnEvent(entity, mate, child);
			if (MinecraftForge.EVENT_BUS.post(event) || event.getChild() == null)
				return;
			child = event.getChild();
			child.setGrowingAge(-24000);
			child.setLocationAndAngles(this.entity.posX, this.entity.posY, this.entity.posZ, 0.0F, 0.0F);
			this.world.addEntity(child);
			this.world.setEntityState(child, (byte) 12);
		}
	}

	public class AIInteract extends LookAtGoal {
		private int interactionDelay;
		private final EntityRabbitman rabbitman;

		AIInteract() {
			super(EntityRabbitman.this, EntityRabbitman.class, 3.0F, 0.02F);
			this.rabbitman = (EntityRabbitman) entity;
		}

		@Override
		public void startExecuting() {
			super.startExecuting();

			if (this.rabbitman.hasEnoughItems(true) && this.closestEntity instanceof VillagerEntity
					&& ((VillagerEntity) this.closestEntity).wantsMoreFood()) {
				this.interactionDelay = 10;
			} else {
				this.interactionDelay = 0;
			}
		}

		@Override
		public void tick() {
			super.tick();
			if (this.interactionDelay > 0) {
				--this.interactionDelay;
				if (this.interactionDelay == 0) {
					Inventory inventorybasic = this.rabbitman.inv;
					for (int i = 0; i < inventorybasic.getSizeInventory(); ++i) {
						ItemStack stack = inventorybasic.getStackInSlot(i);
						ItemStack empty = ItemStack.EMPTY;
						if (!stack.isEmpty()) {
							Item item = stack.getItem();
							if (item == BeastsItems.CARROT_COIN && stack.getCount() > 3) {
								int l = stack.getCount() / 2;
								stack.shrink(l);
								empty = new ItemStack(item, l, stack.getTag());
							}
							if (stack.isEmpty())
								inventorybasic.setInventorySlotContents(i, ItemStack.EMPTY);
						}

						if (!empty.isEmpty()) {
							ItemEntity item = new ItemEntity(this.rabbitman.world, this.rabbitman.posX,
									this.rabbitman.posY - 0.3 + this.rabbitman.getEyeHeight(),
									this.rabbitman.posZ, empty);
							float head = this.rabbitman.rotationYawHead;
							float pitch = this.rabbitman.rotationPitch;
							item.setMotion((Math.sin(-Math.toRadians(head)) * Math.toRadians(pitch) * 0.3F),
									(Math.sin(-Math.toRadians(pitch)) * 0.3 + 0.1),
									(Math.cos(Math.toRadians(head)) * Math.toRadians(pitch) * 0.3));
							item.setDefaultPickupDelay();
							this.rabbitman.world.addEntity(item);
							break;
						}
					}
				}
			}
		}
	}

	private class AIHarvestCarrots extends MoveToBlockGoal {
		private final EntityRabbitman entity;
		private boolean hasFarmItem;
		private boolean wantsToReapStuff;
		private int currentTask;

		AIHarvestCarrots() {
			super(EntityRabbitman.this, 0.6, 16);
			this.entity = EntityRabbitman.this;
		}

		@Override
		public boolean shouldExecute() {
			if (this.runDelay <= 0) {
				if (!ForgeEventFactory.getMobGriefingEvent(this.entity.world, this.entity))
					return false;
				this.currentTask = -1;
				this.hasFarmItem = this.entity.isFarmItemInInventory();
				this.wantsToReapStuff = this.entity.hasEnoughItems(false);
			}
			return super.shouldExecute();
		}

		@Override
		public boolean shouldContinueExecuting() {
			return this.currentTask >= 0 && super.shouldContinueExecuting();
		}

		@Override
		public void tick() {
			super.tick();
			this.entity.getLookController().setLookPosition(this.destinationBlock.getX() + 0.5D,
					this.destinationBlock.getY() + 1, this.destinationBlock.getZ() + 0.5D, 10.0F,
					this.entity.getVerticalFaceSpeed());

			if (this.getIsAboveDestination()) {
				World world = this.entity.world;
				BlockPos blockpos = this.destinationBlock.up();
				BlockState state = world.getBlockState(blockpos);
				Block block = state.getBlock();

				if (this.currentTask == 0 && block instanceof CropsBlock && ((CropsBlock) block).isMaxAge(state))
					world.destroyBlock(blockpos, true);
				else if (this.currentTask == 1 && state.getMaterial() == Material.AIR) {
					Inventory inv = this.entity.inv;
					for (int i = 0; i < inv.getSizeInventory(); ++i) {
						ItemStack stack = inv.getStackInSlot(i);
						if (!stack.isEmpty() && stack.getItem() == Items.CARROT) {
							world.setBlockState(blockpos, Blocks.CARROTS.getDefaultState(), 3);
							stack.shrink(1);
							if (stack.isEmpty())
								inv.setInventorySlotContents(i, ItemStack.EMPTY);
							break;
						}
					}
				}
				this.currentTask = -1;
				this.runDelay = 10;
			}
		}

		@Override
		protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
			Block block = worldIn.getBlockState(pos).getBlock();

			if (block == Blocks.FARMLAND) {
				pos = pos.up();
				BlockState BlockState = worldIn.getBlockState(pos);
				block = BlockState.getBlock();

				if (block instanceof CropsBlock && ((CropsBlock) block).isMaxAge(BlockState) && this.wantsToReapStuff
						&& (this.currentTask == 0 || this.currentTask < 0)) {
					this.currentTask = 0;
					return true;
				}

				if (BlockState.getMaterial() == Material.AIR && this.hasFarmItem
						&& (this.currentTask == 1 || this.currentTask < 0)) {
					this.currentTask = 1;
					return true;
				}
			}

			return false;
		}
	}

	public static class Trade implements VillagerTrades.ITrade {
		private Item buyItem;
		Item sell;
		ItemStack buy;
		int price;
		int amount;

		Trade(Item sell, Item buy, int price, int amount) {
			this.price = price;
			this.amount = amount;
			this.sell = sell;
			this.buyItem = buy;
		}

		Trade(Item sell, ItemStack buy, int price, int amount) {
			this(sell, (Item) null, price, amount);
			this.buy = buy;
		}

		public void addMerchantRecipe(@Nonnull IMerchant merchant, @Nonnull MerchantOffers offersIn,
				@Nonnull Random random) {
			offersIn.add(new MerchantOffer(new ItemStack(sell, this.price),
					this.buy == null ? new ItemStack(buyItem, this.amount) : buy, 30, 3, 0.3F));
		}

		@Override
		public MerchantOffer getOffer(Entity entity, Random rand) {
			return new MerchantOffer(new ItemStack(sell, this.price),
					this.buy == null ? new ItemStack(buyItem, this.amount) : buy, 30, 3, 0.3F);
		}
	}

	public static class MoneyTrade extends Trade {

		MoneyTrade(Item buy, int price, int amount) {
			super(BeastsItems.CARROT_COIN, buy, price, amount);
		}

		MoneyTrade(ItemStack buy, int price, int amount) {
			super(BeastsItems.CARROT_COIN, buy, price, amount);
		}
	}
}
