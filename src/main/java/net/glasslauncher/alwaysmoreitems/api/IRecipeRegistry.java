package net.glasslauncher.alwaysmoreitems.api;

import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeCategory;
import net.glasslauncher.alwaysmoreitems.api.recipe.IRecipeHandler;
import net.minecraft.item.ItemStack;

import javax.annotation.*;
import java.util.*;

/**
 * The IRecipeManager offers several functions for retrieving and handling recipes.
 * The IRecipeManager instance is provided in AMIManager.
 * Available to IModPlugins
 */
public interface IRecipeRegistry {

	/** Returns the IRecipeHandler associated with the recipeClass or null if there is none */
	@Nullable
    IRecipeHandler getRecipeHandler(@Nonnull Class recipeClass);

	/** Returns an unmodifiable list of all Recipe Categories */
	@Nonnull
	List<IRecipeCategory> getRecipeCategories();

	/** Returns an unmodifiable list of Recipe Categories */
	@Nonnull
	List<IRecipeCategory> getRecipeCategories(@Nonnull List<String> recipeCategoryUids);

	/** Returns an unmodifiable list of Recipe Categories that have the ItemStack as an input */
	@Nonnull
	List<IRecipeCategory> getRecipeCategoriesWithInput(@Nonnull ItemStack input);

// TODO: Implement once StationAPI gets a fluid API.
//	/** Returns an unmodifiable list of Recipe Categories that have the Fluid as an input */
//	@Nonnull
//	List<IRecipeCategory> getRecipeCategoriesWithInput(@Nonnull Fluid input);

	/** Returns an unmodifiable list of Recipe Categories that have the ItemStack as an output */
	@Nonnull
	List<IRecipeCategory> getRecipeCategoriesWithOutput(@Nonnull ItemStack output);

// TODO: Implement once StationAPI gets a fluid API.
//	/** Returns an unmodifiable list of Recipe Categories that have the Fluid as an output */
//	@Nonnull
//	List<IRecipeCategory> getRecipeCategoriesWithOutput(@Nonnull Fluid output);

	/** Returns an unmodifiable list of Recipes of recipeCategory that have the ItemStack as an input */
	@Nonnull
	List<Object> getRecipesWithInput(@Nonnull IRecipeCategory recipeCategory, @Nonnull ItemStack input);

// TODO: Implement once StationAPI gets a fluid API.
//	/** Returns an unmodifiable list of Recipes of recipeCategory that have the Fluid as an input */
//	@Nonnull
//	List<Object> getRecipesWithInput(@Nonnull IRecipeCategory recipeCategory, @Nonnull Fluid input);

	/** Returns an unmodifiable list of Recipes of recipeCategory that have the ItemStack as an output */
	@Nonnull
	List<Object> getRecipesWithOutput(@Nonnull IRecipeCategory recipeCategory, @Nonnull ItemStack output);

// TODO: Implement once StationAPI gets a fluid API.
//	/** Returns an unmodifiable list of Recipes of recipeCategory that have the Fluid as an output */
//	@Nonnull
//	List<Object> getRecipesWithOutput(@Nonnull IRecipeCategory recipeCategory, @Nonnull Fluid output);

	/** Returns an unmodifiable list of Recipes in recipeCategory */
	@Nonnull
	List<Object> getRecipes(@Nonnull IRecipeCategory recipeCategory);

	/**
	 * Add a new recipe while the game is running.
	 * This is only for things like gated recipes becoming available, like the ones in Thaumcraft.
	 * Use your IRecipeHandler.isValid to determine which recipes are hidden, and when a recipe becomes valid you can add it here.
	 * (note that IRecipeHandler.isValid must be true when the recipe is added here for it to work)
	 */
	void addRecipe(@Nonnull Object recipe);
}
