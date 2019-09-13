package rando.beasts.common.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import rando.beasts.common.init.BeastsBlocks;
import rando.beasts.common.utils.BeastsUtil;

public class BlockTentacle extends BushBlock {

	public static final IntegerProperty SIZE = IntegerProperty.create("size", 1, 8);
	public static final BooleanProperty FULL = BooleanProperty.create("full");
	private static final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[8];

	public BlockTentacle() {
		super(Properties.create(Material.PLANTS).doesNotBlockMovement());
		this.setDefaultState(this.getDefaultState().with(SIZE, 8).with(FULL, false));
		BeastsUtil.addToRegistry(this, "tentacle", false, null);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		Block block = worldIn.getBlockState(pos.up()).getBlock();
		return block == BeastsBlocks.JELLY_LEAVES || block == this;
	}

	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		super.tick(state, worldIn, pos, random);

	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		int index = state.get(SIZE) - 1;
		if (BOUNDING_BOXES[index] == null)
			BOUNDING_BOXES[index] = new AxisAlignedBB(0.375, 1, 0.375, 0.625, 1 - ((index + 1) * 0.125), 0.625);
		return VoxelShapes.create(BOUNDING_BOXES[index]);
	}

	@Override
	@Nullable
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos,
			ISelectionContext context) {
		return VoxelShapes.empty();
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(SIZE, FULL);
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		super.onEntityCollision(state, worldIn, pos, entityIn);
		if (entityIn instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) entityIn;
			if (!entity.isPotionActive(Effects.POISON)) {
				EffectInstance effect = new EffectInstance(Effects.POISON, 100);
				if (entity.isPotionApplicable(effect))
					entity.addPotionEffect(effect);
			}
		}
	}
}
