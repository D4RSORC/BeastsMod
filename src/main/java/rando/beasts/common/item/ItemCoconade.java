package rando.beasts.common.item;

import java.util.Objects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import rando.beasts.common.entity.projectile.EntityCoconutBomb;

public class ItemCoconade extends BeastsItem {

	public ItemCoconade(String name) {
		super(name);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		if (!playerIn.abilities.isCreativeMode)
			stack.shrink(1);
		worldIn.playSound(null, playerIn.getPosition(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F,
				0.4F / (random.nextFloat() * 0.4F + 0.8F));
		if (!worldIn.isRemote) {
			EntityCoconutBomb egg = new EntityCoconutBomb(playerIn, worldIn);
			egg.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
			worldIn.addEntity(egg);
		}
		playerIn.addStat(Objects.requireNonNull(Stats.ITEM_USED.get(this)));
		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}
}
