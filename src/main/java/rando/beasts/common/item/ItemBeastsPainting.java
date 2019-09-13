package rando.beasts.common.item;

import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rando.beasts.common.entity.item.EntityBeastsPainting;

public class ItemBeastsPainting extends BeastsItem {
	public ItemBeastsPainting() {
		super("beasts_painting");
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		Direction direction = context.getFace();
		BlockPos blockpos = context.getPos().offset(direction);
		PlayerEntity playerentity = context.getPlayer();
		ItemStack itemstack = context.getItem();
		if (direction != Direction.DOWN && direction != Direction.UP
				&& playerentity.canPlayerEdit(blockpos, direction, itemstack)) {
			World world = context.getWorld();
			HangingEntity entityhanging = this.createHangingEntity(world, blockpos, direction);

			if (entityhanging.onValidSurface()) {
				if (!world.isRemote) {
					world.addEntity(entityhanging);
					entityhanging.playPlaceSound();
				}
				itemstack.shrink(1);
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.FAIL;
	}

	private HangingEntity createHangingEntity(World worldIn, BlockPos pos, Direction clickedSide) {
		return new EntityBeastsPainting(worldIn, pos, clickedSide);
	}
}