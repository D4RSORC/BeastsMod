package rando.beasts.common.block;

import net.minecraft.block.DoorBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import rando.beasts.common.item.ItemJellyWoodDoor;
import rando.beasts.common.utils.BeastsUtil;

public class BlockJellyWoodDoor extends DoorBlock {
	public BlockJellyWoodDoor() {
		super(Properties.create(Material.WOOD).hardnessAndResistance(3.0F).sound(SoundType.WOOD));
		BeastsUtil.addToRegistry(this, "jellywood_door", true, ItemJellyWoodDoor::new);
	}
}
