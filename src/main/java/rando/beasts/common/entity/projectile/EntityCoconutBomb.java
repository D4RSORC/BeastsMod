package rando.beasts.common.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rando.beasts.common.init.BeastsItems;

public class EntityCoconutBomb extends ProjectileItemEntity {
	public EntityCoconutBomb(EntityType type, World worldIn) {
		super(EntityType.SNOWBALL, worldIn);
	}

	public EntityCoconutBomb(LivingEntity throwerIn, World worldIn) {
		super(EntityType.SNOWBALL, throwerIn, worldIn);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 3)
			for (int i = 0; i < 8; ++i)
				this.world.addParticle(ParticleTypes.ITEM_SNOWBALL, this.posX, this.posY, this.posZ,
						(this.rand.nextFloat() - 0.5D) * 0.08D,
						(this.rand.nextFloat() - 0.5D) * 0.08D,
						(this.rand.nextFloat() - 0.5D) * 0.08D);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote && result.getType() != RayTraceResult.Type.MISS) {
			BlockPos pos = result.getType() == RayTraceResult.Type.BLOCK ? ((BlockRayTraceResult) result).getPos()
					: ((EntityRayTraceResult) result).getEntity().getPosition();
			if (result.getType() == RayTraceResult.Type.ENTITY)
				((EntityRayTraceResult) result).getEntity()
						.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
			this.world.createExplosion(this, pos.getX(), pos.getY(), pos.getZ(), 1.0F, Mode.DESTROY);
			this.world.setEntityState(this, (byte) 3);
			this.remove();
		}
	}

	@Override
	protected void registerData() {
	}

	@Override
	protected Item func_213885_i() {
		return BeastsItems.COCONADE;
	}
}
