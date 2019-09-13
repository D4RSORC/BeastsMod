package rando.beasts.common.item.crafting;

import com.google.common.collect.Lists;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import rando.beasts.common.init.BeastsItems;
import rando.beasts.common.init.BeastsRecipes;

public class RecipeCoconutJuice extends ShapelessRecipe {

	public RecipeCoconutJuice(ResourceLocation res) {
		super(new ResourceLocation("coconut_juice"), "", new ItemStack(BeastsItems.COCONUT_JUICE),
				NonNullList.from(Ingredient.EMPTY,
						Ingredient.fromItems(Lists.newArrayList(Item.BLOCK_TO_ITEM).stream()
								.filter(item -> item instanceof SwordItem).toArray(Item[]::new)),
						Ingredient.fromItems(BeastsItems.COCONUT)));
		BeastsRecipes.LIST.add(new SpecialRecipeSerializer<RecipeCoconutJuice>(RecipeCoconutJuice::new));
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.getItem() instanceof SwordItem)
				stack.setDamage(stack.getDamage() - 1);
		}
		return super.getCraftingResult(inv);
	}
}
