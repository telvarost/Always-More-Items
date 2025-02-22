package net.glasslauncher.mods.alwaysmoreitems.api.gui;

import javax.annotation.Nonnull;

public interface RecipeLayout {
    /**
     * Contains all the itemStacks displayed on this recipe layout.
     * Init and set them in your recipe category.
     */
    @Nonnull
    GuiItemStackGroup getItemStacks();

// TODO: Implement once StationAPI gets a fluid API.
//    /**
//     * Contains all the fluidStacks displayed on this recipe layout.
//     * Init and set them in your recipe category.
//     */
//    @Nonnull
//    IGuiFluidStackGroup getFluidStacks();

    /**
     * Moves the recipe transfer button's position relative to the recipe layout.
     * By default the recipe transfer button is at the bottom, to the right of the recipe.
     * If it doesn't fit there, you can use this to move it when you init the recipe layout.
     */
    void setRecipeTransferButton(int posX, int posY);
}
