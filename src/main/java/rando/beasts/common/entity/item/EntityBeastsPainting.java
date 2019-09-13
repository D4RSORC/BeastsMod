package rando.beasts.common.entity.item;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import rando.beasts.common.init.BeastsItems;
import rando.beasts.common.network.BeastsSpawnPaintingPacket;

//TODO: fix the painting
public class EntityBeastsPainting extends HangingEntity implements IEntityAdditionalSpawnData {
	public BeastsPainting art;

	public EntityBeastsPainting(EntityType type, World worldIn) {
		super(EntityType.PAINTING, worldIn);
	}

	public EntityBeastsPainting(World worldIn, BlockPos pos, Direction side) {
		super(EntityType.PAINTING, worldIn, pos);
		ArrayList<BeastsPainting> arraylist = new ArrayList<>();
		BeastsPainting[] aenumart = BeastsPainting.values();

		for (BeastsPainting enumart : aenumart) {
			this.art = enumart;
			this.updateFacingWithBoundingBox(side);
			if (this.onValidSurface())
				arraylist.add(enumart);
		}

		if (!arraylist.isEmpty())
			this.art = arraylist.get(this.rand.nextInt(arraylist.size()));
		this.updateFacingWithBoundingBox(side);
	}

	public EntityBeastsPainting(World worldIn, BlockPos pos, Direction side, BeastsPainting art) {
		this(worldIn, pos, side);
		this.art = art;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(BeastsItems.BEASTS_PAINTING, 1);
	}

	@OnlyIn(Dist.CLIENT)
	public EntityBeastsPainting(World worldIn, BlockPos pos, Direction side, String name) {
		this(worldIn, pos, side);
		BeastsPainting[] aenumart = BeastsPainting.values();
		for (BeastsPainting enumart : aenumart) {
			if (enumart.title.equals(name)) {
				this.art = enumart;
				break;
			}
		}

		this.updateFacingWithBoundingBox(side);
	}

	@Override
	public void writeAdditional(CompoundNBT tagCompound) {
		tagCompound.putString("Motive", this.art.title);
		super.writeAdditional(tagCompound);
	}

	@Override
	public void readAdditional(CompoundNBT tagCompund) {
		String s = tagCompund.getString("Motive");
		BeastsPainting[] aenumart = BeastsPainting.values();
		for (BeastsPainting enumart : aenumart)
			if (enumart.title.equals(s))
				this.art = enumart;
		if (this.art == null)
			this.art = BeastsPainting.WHALE;
		super.readAdditional(tagCompund);
	}

	@Override
	public int getWidthPixels() {
		return this.art.sizeX;
	}

	@Override
	public int getHeightPixels() {
		return this.art.sizeY;
	}

	@Override
	public void onBroken(Entity entity) {
		if (entity instanceof PlayerEntity) {
			PlayerEntity entityplayer = (PlayerEntity) entity;
			if (entityplayer.abilities.isCreativeMode)
				return;
		}

		this.entityDropItem(new ItemStack(BeastsItems.BEASTS_PAINTING), 0.0F);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
		BlockPos pos = this.hangingPosition.add(new BlockPos(x - this.posX, y - this.posY, z - this.posZ));
		this.setPosition(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch,
			int posRotationIncrements, boolean teleport) {
		BlockPos blockpos = this.hangingPosition.add(x - this.posX, y - this.posY, z - this.posZ);
		this.setPosition(blockpos.getX(), blockpos.getY(), blockpos.getZ());
	}

	@Override
	public void playPlaceSound() {
	}

	public enum BeastsPainting {
		WHALE("Whale", 32, 16), CRAB("Crab"), TURTLE("Turtle"), PUFFER("Puffer"), SHRIMP("Shrimp"),
		GARDEN_EEL("Garden Eel", 16, 32);

		public final String title;
		public int sizeX;
		public int sizeY;

		BeastsPainting(String name, int x, int y) {
			this.title = name;
			this.sizeX = x;
			this.sizeY = y;
		}

		BeastsPainting(String name) {
			this(name, 16, 16);
		}
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		buffer.writeInt(this.art.ordinal());

		buffer.writeInt(this.hangingPosition.getX());
		buffer.writeInt(this.hangingPosition.getY());
		buffer.writeInt(this.hangingPosition.getZ());
		buffer.writeByte(this.getHorizontalFacing().getIndex());

	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {
		BeastsPainting[] aenumart = BeastsPainting.values();
		this.art = aenumart[additionalData.readInt()];
		int x = additionalData.readInt();
		int y = additionalData.readInt();
		int z = additionalData.readInt();
		this.hangingPosition = new BlockPos(x, y, z);
		this.updateFacingWithBoundingBox(Direction.byIndex((additionalData.readByte())));

	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return new BeastsSpawnPaintingPacket(this);
	}
}