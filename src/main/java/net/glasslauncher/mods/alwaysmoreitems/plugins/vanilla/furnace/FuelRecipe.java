package net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.furnace;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.alwaysmoreitems.AMITextRenderer;
import net.glasslauncher.mods.alwaysmoreitems.DrawableHelper;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.IDrawableAnimated;
import net.glasslauncher.mods.alwaysmoreitems.api.gui.IDrawableStatic;
import net.glasslauncher.mods.alwaysmoreitems.plugins.vanilla.VanillaRecipeWrapper;
import net.glasslauncher.mods.alwaysmoreitems.util.HoverChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.*;
import java.awt.*;
import java.text.*;
import java.util.List;
import java.util.*;

public class FuelRecipe extends VanillaRecipeWrapper {
	public static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("0.##");
	@Nonnull
	private final List<List<ItemStack>> inputs;
	@Nonnull
	private final String burnTimeStringTicks;
	@Nonnull
	private final String burnTimeStringItems;
	@Nonnull
	private final String burnTimeStringSeconds;
	private final String burnTimeStringItemsFull;
	private final String burnTimeStringSecondsFull;
	private final int burnTimeStringItemsWidth;
	private final HoverChecker burnTimeStringItemsTooltipChecker;
	private final HoverChecker burnTimeStringSecondsTooltipChecker;
	@Nonnull
	private final IDrawableAnimated flame;

	public FuelRecipe(@Nonnull Collection<ItemStack> input, int burnTime) {
		List<ItemStack> inputList = new ArrayList<>(input);
		inputs = Collections.singletonList(inputList);
		if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT)) {
			burnTimeStringTicks = TranslationStorage.getInstance().get("gui.alwaysmoreitems.category.fuel.burnTime", burnTime);
			burnTimeStringItems = TranslationStorage.getInstance().get("gui.alwaysmoreitems.category.fuel.burnTime.items", NUMBER_FORMAT.format(burnTime / 200f));
			burnTimeStringSeconds = TranslationStorage.getInstance().get("gui.alwaysmoreitems.category.fuel.burnTime.seconds", NUMBER_FORMAT.format(burnTime / 20f));
			burnTimeStringItemsFull = TranslationStorage.getInstance().get("gui.alwaysmoreitems.category.fuel.burnTime.items.full");
			burnTimeStringSecondsFull = TranslationStorage.getInstance().get("gui.alwaysmoreitems.category.fuel.burnTime.seconds.full");
			burnTimeStringItemsWidth = AMITextRenderer.INSTANCE.getWidth(burnTimeStringItems);
			burnTimeStringItemsTooltipChecker = new HoverChecker(26, 35, 24, 24 + burnTimeStringItemsWidth);
			burnTimeStringSecondsTooltipChecker = new HoverChecker(26, 35, 24 + burnTimeStringItemsWidth + 8, 24 + burnTimeStringItemsWidth + AMITextRenderer.INSTANCE.getWidth(burnTimeStringSeconds) + 8);

			IDrawableStatic flameDrawable = DrawableHelper.createDrawable("/gui/furnace.png", 176, 0, 14, 14);
			flame = DrawableHelper.createAnimatedDrawable(flameDrawable, burnTime, IDrawableAnimated.StartDirection.TOP, true);
		}
		else {
			burnTimeStringTicks = null;
			burnTimeStringItems = null;
			burnTimeStringSeconds = null;
			burnTimeStringItemsFull = null;
			burnTimeStringSecondsFull = null;
			burnTimeStringItemsWidth = 0;
			burnTimeStringItemsTooltipChecker = null;
			burnTimeStringSecondsTooltipChecker = null;
			flame = null;
		}
	}

	@Nonnull
	@Override
	public List<List<ItemStack>> getInputs() {
		return inputs;
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		AMITextRenderer.INSTANCE.draw(burnTimeStringTicks, 24, 12, Color.DARK_GRAY.getRGB());
		AMITextRenderer.INSTANCE.draw(burnTimeStringItems, 24, 26, Color.GRAY.getRGB());
		AMITextRenderer.INSTANCE.draw(burnTimeStringSeconds, 24 + burnTimeStringItemsWidth + 8, 26, Color.GRAY.getRGB());
	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {
		flame.draw(minecraft, 2, 0);
	}

	@Nullable
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		if(burnTimeStringItemsTooltipChecker.isOver(mouseX, mouseY)) {
			return Collections.singletonList(burnTimeStringItemsFull);
		}

		if (burnTimeStringSecondsTooltipChecker.isOver(mouseX, mouseY)) {
			return Collections.singletonList(burnTimeStringSecondsFull);
		}

		return null;
	}
}
