package rando.beasts.common.entity.monster;

import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import rando.beasts.common.entity.IDriedAquatic;
import rando.beasts.common.init.BeastsItems;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class EntityWhippingBarnacle extends MonsterEntity implements IDriedAquatic {

    private static final DataParameter<Integer> FACING = EntityDataManager.createKey(EntityWhippingBarnacle.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityWhippingBarnacle.class, DataSerializers.VARINT);
    private static final Iterable<ItemStack> EMPTY = new ArrayList<>();
    public boolean impaling;
    public boolean ready;
    
    private EntitySize size;

    public EntityWhippingBarnacle(EntityType type, World worldIn) {
        super(type, worldIn);
        this.size = type.getSize();
        setNoGravity(true);
    }

    @Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, ILivingEntityData spawnDataIn, CompoundNBT dataTag) {
    	spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    	setColor(rand.nextInt(2));
        for(int i = Direction.values().length - 1; i >= 0; --i) {
        	Direction facing = Direction.values()[i];
            boolean flag = world.getBlockState(getPosition().offset(facing.getOpposite())).getBlock() == Blocks.STONE;
            for(int j = 1; j <= 3; j++) flag &= world.getBlockState(getPosition().offset(facing, i)).getBlock() == Blocks.AIR;
            if(flag) {
                setFacing(facing);
                break;
            }
        }
        if(getFacing().getHorizontalIndex() != -1) {
            rotationYaw = getFacing().getHorizontalAngle();
            setSize(1.5f, 0.5f);
        }
        return spawnDataIn;
	}

	private void setSize(float width, float height) {
		this.size = EntitySize.flexible(width, height);
		recalculateSize();
	}
	
	public EntitySize getSize(Pose poseIn) {
		return this.size;
	}

	@Override
    protected void registerAttributes() {
        super.registerAttributes();
        getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4);
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(FACING, 0);
        this.dataManager.register(COLOR, 0);
    }

    @Override
    public void tick() {
        this.world.getProfiler().startSection("entityBaseTick");
        LivingEntity[] entities = world.getEntitiesWithinAABB(LivingEntity.class, getBoundingBox().grow(5)).stream().filter(e -> !(e instanceof IDriedAquatic)).toArray(LivingEntity[]::new);
        if(entities.length == 0) {
            ready = false;
            impaling = false;
        } else for (LivingEntity entity : entities) {
            if(!ready) ready = true;
            if(getDistanceSq(entity) < 2) {
                Direction facing = getFacing();
                Direction.Axis axis = facing.getAxis();
                double relativePos;
                if (axis == Direction.Axis.Z) relativePos = entity.posZ - this.posZ;
                else relativePos = entity.posX - this.posX;
                if (Math.abs(relativePos) < 0.5f) impaling = true;
                else if (MathHelper.cos(limbSwing) < relativePos) {
                    impaling = false;
                    attackEntityAsMob(entity);
                }
            }
        }
        this.prevDistanceWalkedModified = this.distanceWalkedModified;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;

        this.spawnRunningParticles();
        this.handleWaterMovement();

        if (this.isInLava()) {
            this.setOnFireFromLava();
            this.fallDistance *= 0.5F;
        }

        if (this.posY < -64.0D) this.outOfWorld();
        this.firstUpdate = false;
        this.world.getProfiler().endSection();
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {}

    @Override
    public void applyEntityCollision(Entity entityIn) {}

    @Override
    public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {}

    @Override
	public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
    	boolean flag = false;
        for(Direction facing : Direction.values()) if(world.getBlockState(getPosition().offset(facing)).getBlock() == Blocks.STONE) {
            flag = true;
            break;
        }
        return flag;
	}

	public void setFacing(Direction facing) {
        this.dataManager.set(FACING, facing.getIndex());
    }

    public Direction getFacing() {
        return Direction.values()[this.dataManager.get(FACING)];
    }

    public void setColor(int color) {
        this.dataManager.set(COLOR, color);
    }

    public int getColor() {
        return this.dataManager.get(COLOR);
    }

	public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.ARTHROPOD;
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return EMPTY;
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {}

    @Override
    public HandSide getPrimaryHand() {
        return HandSide.LEFT;
    }


    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setFacing(Direction.values()[compound.getInt("facing")]);
        this.setColor(compound.getInt("color"));
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("facing", this.getFacing().getIndex());
        compound.putInt("color", this.getColor());
    }
}
