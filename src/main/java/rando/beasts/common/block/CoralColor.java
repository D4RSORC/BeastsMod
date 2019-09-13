package rando.beasts.common.block;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.IStringSerializable;

public enum CoralColor implements IStringSerializable {
	BLUE(MaterialColor.BLUE), PINK(MaterialColor.PINK), PURPLE(MaterialColor.PURPLE), RED(MaterialColor.RED),
	YELLOW(MaterialColor.YELLOW);

	public MaterialColor mapColor;

	CoralColor(MaterialColor color) {
		this.mapColor = color;
	}

	@Nonnull
	@Override
	public String getName() {
		return name().toLowerCase();
	}

	public static CoralColor getRandom(Random rand) {
		return values()[rand.nextInt(values().length)];
	}
}
