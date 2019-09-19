package rando.beasts.common.entity.passive;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.network.NetworkHooks;
import rando.beasts.client.gui.GuiLandwhaleInventory;
import rando.beasts.client.init.BeastsSounds;
import rando.beasts.common.block.CoralColor;
import rando.beasts.common.entity.IDriedAquatic;
import rando.beasts.common.init.BeastsBlocks;
import rando.beasts.common.init.BeastsItems;
import rando.beasts.common.inventory.ContainerLandwhaleInventory;


public class EntityLandwhale extends TameableEntity implements IShearable, IDriedAquatic, INamedContainerProvider {
	
	private static final DataParameter<Boolean> SHEARED = EntityDataManager.createKey(EntityLandwhale.class, DataSerializers.BOOLEAN);
    private static final DataParameter<ItemStack> SADDLE = EntityDataManager.createKey(EntityLandwhale.class, DataSerializers.ITEMSTACK);
    private int ticksSinceSheared = 0;
    public Inventory inventory;
	
	public EntityLandwhale(EntityType type, World worldIn) {
		super(type, worldIn);
		if(this.inventory == null) this.inventory = new Inventory(1) {
            @Override
            public int getInventoryStackLimit() {
                return 1;
            }           
        };
	}

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(SHEARED, false);
        this.dataManager.register(SADDLE, ItemStack.EMPTY);
    }

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 2.0D));
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		if (isTamed()) this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60);
		else this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.18D);
	} 

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60);
        else this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40);
    }

    public void travel(Vec3d motion) {
        if (this.getControllingPassenger() != null && this.canBeSteered() && !getSaddle().isEmpty()) {
            LivingEntity entitylivingbase = (LivingEntity) this.getControllingPassenger();
            this.rotationYaw = entitylivingbase.rotationYaw;
            this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = entitylivingbase.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.renderYawOffset;
            float strafe = entitylivingbase.moveStrafing * 0.5F;
            float forward = entitylivingbase.moveForward;

            if (forward <= 0.0F) forward *= 0.25F;

            if (this.canPassengerSteer()) {
                this.setAIMoveSpeed((float) this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
                super.travel(new Vec3d(strafe, motion.y, forward));
            } else if (entitylivingbase instanceof PlayerEntity) {
            	this.setMotion(0, 0, 0);
            }

            this.prevLimbSwingAmount = this.limbSwingAmount;
            double d1 = this.posX - this.prevPosX;
            double d0 = this.posZ - this.prevPosZ;
            float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;

            if (f2 > 1.0F) f2 = 1.0F;
            this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        } else super.travel(motion);
    }

    @Override
    public boolean processInteract(PlayerEntity player, @Nonnull Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if(this.isTamed() && (this.isOwner(player) || this.getControllingPassenger() != null)) {
            if(player.isSneaking()) {
                if (!this.world.isRemote) NetworkHooks.openGui((ServerPlayerEntity)player, (INamedContainerProvider)this, (buf) -> buf.writeInt(this.getEntityId()));
                return true;
            }
            if (!player.isPassenger(this) && this.getPassengers().size() < 2) player.startRiding(this);
            return true;
        }
        else if (!this.isTamed() && stack.getItem() == BeastsItems.COCONUT_JUICE) {
            if (!player.abilities.isCreativeMode) stack.shrink(1);
            if (this.rand.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                this.isJumping = false;
                this.navigator.clearPath();
                this.setTamedBy(player);
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
    public void livingTick() {
        super.livingTick();
        if(getSheared()) {
            if (getSaddle().isEmpty() && ticksSinceSheared > 48000) setSheared(false);
            ticksSinceSheared++;
        } else ticksSinceSheared = 0;
    }

    protected boolean canFitPassenger(Entity passenger) {
        return this.getPassengers().size() < 2;
    }

    @Nullable
    public Entity getControllingPassenger()
    {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem() == BeastsItems.REEF_MIXTURE;
    }

	@Override
	protected SoundEvent getAmbientSound() {
		return BeastsSounds.LANDWHALE_AMBIENT;
	}

	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(SoundEvents.ENTITY_COW_STEP, 0.15F, 1.0F);
	}

	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	@Override
	public EntityLandwhale createChild(AgeableEntity ageable) {
		return new EntityLandwhale(this.getType(), this.world);
	}

	@Override
    public float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return this.isChild() ? this.getHeight() : 2.0F;
    }

    public boolean getSheared() {
        return this.dataManager.get(SHEARED);
    }

    public void setSheared(boolean sheared) {
        this.dataManager.set(SHEARED, sheared);
    }

    public ItemStack getSaddle() {
        return this.dataManager.get(SADDLE);
    }

    public void setSaddle(ItemStack item) {      
        inventory.setInventorySlotContents(0, item);
        if(!world.isRemote) this.dataManager.set(SADDLE, item);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("sheared", getSheared());
        compound.putInt("shearTicks", ticksSinceSheared);
        if(!getSaddle().isEmpty()) compound.put("saddle", getSaddle().write(new CompoundNBT()));
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        setSheared(compound.getBoolean("sheared"));
        this.ticksSinceSheared = compound.getInt("shearTicks");
        if(compound.contains("saddle")) setSaddle(ItemStack.read(compound.getCompound("saddle")));
    }

    @Override
    public boolean isShearable(@Nonnull ItemStack item, IWorldReader world, BlockPos pos) {
        return !this.getSheared() && !this.isChild();
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack item, IWorld world, BlockPos pos, int fortune) {
        setSheared(true);
        int i = 1 + this.rand.nextInt(3);
        List<ItemStack> ret = new ArrayList<>();
        for (int j = 0; j < i; ++j) ret.add(new ItemStack(Item.getItemFromBlock(BeastsBlocks.CORAL_BLOCKS.get(CoralColor.getRandom(this.rand))), 1));
        this.playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
        return ret;
    }
    
    @Override
    public ITextComponent getDisplayName() {
    	return this.hasCustomName() ? this.getCustomName() : super.getDisplayName();
    }

	@Override
	public Container createMenu(int id, PlayerInventory inv, PlayerEntity player) {
		return new ContainerLandwhaleInventory(id, this, player);
	}
}