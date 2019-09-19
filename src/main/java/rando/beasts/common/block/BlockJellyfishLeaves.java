package rando.beasts.common.block;


import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rando.beasts.common.utils.BeastsUtil;

public class BlockJellyfishLeaves extends LeavesBlock {

	public BlockJellyfishLeaves() {
		super(Properties.create(Material.LEAVES).sound(SoundType.SLIME).hardnessAndResistance(0.2F));
		BeastsUtil.addToRegistry(this, "jellyleaves", true, BlockItem::new);
	}

	@Override
	public void randomTick(BlockState state, World worldIn, BlockPos pos, Random random) {
	}
	
	@Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
