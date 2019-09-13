package rando.beasts.common.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import rando.beasts.common.item.crafting.RecipeCoconutJuice;

public class BeastsRecipes {
	public static final List<IRecipeSerializer<?>> LIST = new ArrayList<>();

	public static final IRecipeSerializer<RecipeCoconutJuice> COCONUT_JUICE = new SpecialRecipeSerializer<RecipeCoconutJuice>(
			RecipeCoconutJuice::new);
}
