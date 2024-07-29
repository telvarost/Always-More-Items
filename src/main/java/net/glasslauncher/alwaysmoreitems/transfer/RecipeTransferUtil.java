package net.glasslauncher.alwaysmoreitems.transfer;

import net.glasslauncher.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.alwaysmoreitems.api.recipe.transfer.IRecipeTransferError;
import net.glasslauncher.alwaysmoreitems.api.recipe.transfer.IRecipeTransferHandler;
import net.glasslauncher.alwaysmoreitems.gui.RecipeLayout;
import net.glasslauncher.alwaysmoreitems.gui.screen.OverlayScreen;
import net.glasslauncher.alwaysmoreitems.gui.screen.RecipesGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;

import javax.annotation.*;

public class RecipeTransferUtil {
	public static IRecipeTransferError getTransferRecipeError(@Nonnull RecipeLayout recipeLayout, @Nonnull PlayerEntity player) {
		return transferRecipe(recipeLayout, player, false, false);
	}

	public static boolean transferRecipe(@Nonnull RecipeLayout recipeLayout, @Nonnull PlayerEntity player, boolean maxTransfer) {
		IRecipeTransferError error = transferRecipe(recipeLayout, player, maxTransfer, true);
		return error == null;
	}

	@Nullable
	private static IRecipeTransferError transferRecipe(@Nonnull RecipeLayout recipeLayout, @Nonnull PlayerEntity player, boolean maxTransfer, boolean doTransfer) {
		Screen parentScreen = RecipesGui.INSTANCE.parent;
		if (parentScreen instanceof HandledScreen handledScreen) {
			ScreenHandler container = handledScreen.container;

			IRecipeTransferHandler transferHandler = AlwaysMoreItems.getRecipeRegistry().getRecipeTransferHandler(container, recipeLayout.getRecipeCategory());
			if (transferHandler == null) {
				if (doTransfer) {
					AlwaysMoreItems.LOGGER.error("No Recipe Transfer handler for container {}", container.getClass());
				}
				AlwaysMoreItems.LOGGER.warn("No Recipe Transfer handler for container {}", container.getClass());
				return RecipeTransferErrorInternal.instance;
			}

			return transferHandler.transferRecipe(container, recipeLayout, player, maxTransfer, doTransfer);
		}
		return RecipeTransferErrorInternal.instance;
	}
}
