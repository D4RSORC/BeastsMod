package rando.beasts.common.item;

import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import rando.beasts.common.init.BeastsBlocks;

public class ItemGlowRoot extends BeastsItem {

	public ItemGlowRoot() {
		super("glow_root");
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if (context.getFace() != Direction.DOWN
				|| context.getWorld().getBlockState(context.getPos()).getBlock() == BeastsBlocks.GLOW_ROOT_BOTTOM
				|| context.getWorld().getBlockState(context.getPos()).getBlock() == BeastsBlocks.GLOW_ROOT_TOP)
			return super.onItemUse(context);
		BlockPos down = context.getPos().down();
		if (context.getWorld().getBlockState(down) == Blocks.AIR.getDefaultState()
				&& context.getWorld().getBlockState(context.getPos().down().down()) == Blocks.AIR.getDefaultState()) {
			context.getWorld().setBlockState(down, BeastsBlocks.GLOW_ROOT_TOP.getDefaultState());
			context.getWorld().setBlockState(down.down(), BeastsBlocks.GLOW_ROOT_BOTTOM.getDefaultState());
			SoundType soundtype = context.getWorld().getBlockState(down).getBlock().getSoundType(
					context.getWorld().getBlockState(context.getPos().down()), context.getWorld(), context.getPos(),
					context.getPlayer());
			context.getWorld().playSound(context.getPlayer(), context.getPos(), soundtype.getPlaceSound(),
					SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
			if (!context.getPlayer().abilities.isCreativeMode)
				context.getPlayer().getHeldItem(context.getHand()).shrink(1);
			return ActionResultType.SUCCESS;
		}
		return super.onItemUse(context);
	}
}
