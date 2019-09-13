package rando.beasts.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import rando.beasts.common.utils.BeastsUtil;
import rando.beasts.common.utils.ItemFactory;

public class BeastsBlock extends Block {

	// aaaaa too many constructors

	public BeastsBlock(Material materialIn, String name) {
		this(materialIn, name, true);
	}

	public BeastsBlock(Material materialIn, MaterialColor mapColorIn, String name, boolean tab) {
		this(materialIn, mapColorIn, name, tab, BlockItem::new);
	}

	public BeastsBlock(Material materialIn, String name, boolean tab) {
		this(materialIn, name, tab, BlockItem::new);
	}

	public BeastsBlock(Material materialIn, String name, ItemFactory<?> item) {
		this(materialIn, materialIn.getColor(), name, true, item);
	}

	public BeastsBlock(Material materialIn, String name, boolean tab, ItemFactory<?> item) {
		this(materialIn, materialIn.getColor(), name, tab, item);
	}

	public BeastsBlock(Material materialIn, MaterialColor mapColorIn, String name, boolean tab, ItemFactory<?> item) {
		super(Properties.create(materialIn, mapColorIn));
		BeastsUtil.addToRegistry(this, name, tab, item);
	}

	public BeastsBlock(Properties properties, String name, boolean tab, ItemFactory<?> item) {
		super(properties);
		BeastsUtil.addToRegistry(this, name, tab, item);
	}
}
