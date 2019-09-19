package rando.beasts.common.entity.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rando.beasts.common.init.BeastsBlocks;

public class EntityFallingCoconut extends Entity {

    private BlockState fallTile;
    private int fallTime;
    private boolean shouldDropItem = true;

    public EntityFallingCoconut(EntityType type, World worldIn) {
        super(type, worldIn);
        this.fallTile = BeastsBlocks.COCONUT.getDefaultState();
        this.preventEntitySpawning = true;
        this.setMotion(0, 0, 0);
    }

    public EntityFallingCoconut(EntityType type, World worldIn, BlockPos pos) {
        this(type, worldIn);
        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;
        this.setPosition(x, y + (double)((1.0F - this.getHeight()) / 2.0F), z);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }

    public boolean canBeAttackedWithItem()
    {
        return false;
    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit() {}

    public boolean canBeCollidedWith()
    {
        return !this.removed;
    }

    public void tick() {
        Block block = this.fallTile.getBlock();
        if (this.fallTile.getMaterial() == Material.AIR) this.remove();
        else {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;

            if (this.fallTime++ == 0) {
                BlockPos blockpos = new BlockPos(this);
                if (this.world.getBlockState(blockpos).getBlock() == block) this.world.removeBlock(blockpos, true);
                else if (!this.world.isRemote) {
                    this.remove();
                    return;
                }
            }

            if (!this.hasNoGravity()) this.addVelocity(0, -0.03999999910593033D, 0);
            this.move(MoverType.SELF, this.getMotion());

            if (!this.world.isRemote) {
                BlockPos blockpos1 = new BlockPos(this);
                if (!this.onGround) {
                    if (this.fallTime > 100 && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.fallTime > 600) {
                        if (this.shouldDropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) this.entityDropItem(new ItemStack(block, 1), 0.0F);
                        this.remove();
                    }
                } else {
                    BlockState iblockstate = this.world.getBlockState(blockpos1);

                    if (this.world.isAirBlock(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ))) //Forge: Don't indent below.
                        if (FallingBlock.canFallThrough(this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ)))) {
                            this.onGround = false;
                            return;
                        }

                    this.setMotion(this.getMotion().mul(0.699999988079071D, -0.5D, 0.699999988079071D));

                    if (iblockstate.getBlock() != Blocks.PISTON_HEAD) {
                        this.remove();
                        if (!(!FallingBlock.canFallThrough(this.world.getBlockState(blockpos1.down())) && this.world.setBlockState(blockpos1, this.fallTile, 3)) && (this.shouldDropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS))) this.entityDropItem(new ItemStack(block, 1), 0.0F);
                    }
                }
            }
            
            this.setMotion(this.getMotion().mul(0.9800000190734863D, 0.9800000190734863D, 0.9800000190734863D));
        }
    }

    public void fall(float distance, float damageMultiplier) {

        int i = MathHelper.ceil(distance - 1.0F);

        if (i > 0) {
            List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox());
            DamageSource damagesource = DamageSource.FALLING_BLOCK;
            for (Entity entity : list) entity.attackEntityFrom(damagesource, 6.0F);
        }
    }

    protected void writeAdditional(CompoundNBT compound)
    {
        compound.putInt("Time", this.fallTime);
        compound.putBoolean("DropItem", this.shouldDropItem);
    }

    protected void readAdditional(CompoundNBT compound) {
        this.fallTime = compound.getInt("Time");
        if (compound.contains("DropItem", 99)) this.shouldDropItem = compound.getBoolean("DropItem");
    }

    @Override
    public void fillCrashReport(CrashReportCategory category) {
        super.fillCrashReport(category);
        if (this.fallTile != null) {
            BlockState block = this.fallTile;
            category.addDetail("Immitating block ID", Block.BLOCK_STATE_IDS.get(block));
        }
    }



	@OnlyIn(Dist.CLIENT)
    public boolean canRenderOnFire() {
        return false;
    }

    public boolean ignoreItemEntityData() {
        return true;
    }

	@Override
	protected void registerData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		// TODO Auto-generated method stub
		return null;
	}
}
