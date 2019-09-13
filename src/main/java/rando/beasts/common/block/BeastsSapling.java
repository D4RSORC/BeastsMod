package rando.beasts.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.trees.Tree;
import net.minecraft.item.BlockItem;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import rando.beasts.common.utils.BeastsUtil;
import rando.beasts.common.utils.ItemFactory;

@SuppressWarnings("deprecation")
public class BeastsSapling extends BushBlock implements IGrowable {

	public static final IntegerProperty STAGE = BlockStateProperties.STAGE_0_1;
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
	private Tree tree;

	public BeastsSapling(String name, ItemFactory<?> item) {
		super(Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().sound(SoundType.PLANT)
				.hardnessAndResistance(0.6F));
		this.setDefaultState(this.stateContainer.getBaseState().with(STAGE, Integer.valueOf(0)));
		BeastsUtil.addToRegistry(this, name, true, item);
	}

	public BeastsSapling(String name, Tree tree) {
		this(name, BlockItem::new);
		this.tree = tree;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return state.getBlock() == Blocks.SAND;
	}

	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		if (!worldIn.isRemote) {
			super.tick(state, worldIn, pos, random);
			if (!worldIn.isAreaLoaded(pos, 1))
				return;
			if (worldIn.getLight(pos.up()) >= 9 && random.nextInt(7) == 0)
				this.grow(worldIn, random, pos, state);
		}
	}

	@Override
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return worldIn.rand.nextFloat() < 0.45;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, BlockState state) {
		if (state.get(STAGE) == 0)
			worldIn.setBlockState(pos, state.cycle(STAGE), 4);
		generateTree(worldIn, pos, state, rand);
	}

	protected void generateTree(World worldIn, BlockPos pos, BlockState state, Random rand) {
		if (tree != null)
			tree.spawn(worldIn, pos, state, rand);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(STAGE);
	}
}
