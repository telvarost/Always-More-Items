package net.glasslauncher.alwaysmoreitems.plugins.vanilla.crafting;

import net.glasslauncher.alwaysmoreitems.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.glasslauncher.alwaysmoreitems.plugins.vanilla.VanillaRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.impl.recipe.StationShapelessRecipe;

import javax.annotation.*;
import java.util.*;

public class ShapelessOreRecipeWrapper extends VanillaRecipeWrapper implements ICraftingRecipeWrapper {

	@Nonnull
	private final StationShapelessRecipe recipe;

	public ShapelessOreRecipeWrapper(@Nonnull StationShapelessRecipe recipe) {
		this.recipe = recipe;
		for (Object input : this.recipe.getIngredients()) {
			if (input instanceof ItemStack) {
				ItemStack itemStack = (ItemStack) input;
				if (itemStack.count != 1) {
					itemStack.count = 1;
				}
			}
		}
	}

	@Nonnull
	@Override
	public List getInputs() {
		return Arrays.asList(recipe.getIngredients());
	}

	@Nonnull
	@Override
	public List<ItemStack> getOutputs() {
		return List.of(recipe.getOutputs());
	}
}
