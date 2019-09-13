package rando.beasts.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;

@SuppressWarnings("deprecation")
public class BlockCoral extends BeastsBlock {

	public BlockCoral(CoralColor color) {
		super(Properties.create(Material.PLANTS).hardnessAndResistance(0.6F).sound(SoundType.PLANT),
				"coral_block_" + color.getName(), true, BlockItem::new);
	}

}
