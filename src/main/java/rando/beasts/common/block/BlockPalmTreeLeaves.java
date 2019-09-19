package rando.beasts.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import rando.beasts.common.entity.item.EntityFallingCoconut;
import rando.beasts.common.init.BeastsEntities;
import rando.beasts.common.utils.BeastsUtil;

public class BlockPalmTreeLeaves extends LeavesBlock {

    public BlockPalmTreeLeaves() {
    	super(Properties.create(Material.LEAVES).hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT));
        BeastsUtil.addToRegistry(this, "palm_leaves", false, null);
    }

    @Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		super.tick(state, worldIn, pos, random);
		if(random.nextInt(200) == 0) worldIn.addEntity(new EntityFallingCoconut(BeastsEntities.FALLING_COCONUT,worldIn, pos));
	}

    @Override
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack stack) {
    	if (!worldIn.isRemote && stack.getItem() == Items.SHEARS) player.addStat(Stats.BLOCK_MINED.get(this));
    	else super.harvestBlock(worldIn, player, pos, state, te, stack);
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IWorld world, BlockPos pos, int fortune) {
		return NonNullList.withSize(1, new ItemStack(this, 1));
	}
}
