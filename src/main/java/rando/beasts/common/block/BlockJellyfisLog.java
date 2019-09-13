package rando.beasts.common.block;

import net.minecraft.block.LogBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import rando.beasts.common.utils.BeastsUtil;

public class BlockJellyfisLog extends LogBlock {

	public BlockJellyfisLog() {
		super(MaterialColor.BLUE, Properties.create(Material.ORGANIC));
		this.setDefaultState(this.getDefaultState().with(BlockStateProperties.AXIS, Direction.Axis.Y));
		BeastsUtil.addToRegistry(this, "jellywood", true, BlockItem::new);
	}
}
