package net.glasslauncher.alwaysmoreitems.gui.screen;

import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.alwaysmoreitems.AlwaysMoreItems;
import net.glasslauncher.alwaysmoreitems.RenderHelper;
import net.glasslauncher.alwaysmoreitems.SearchHelper;
import net.glasslauncher.alwaysmoreitems.action.ActionButtonRegistry;
import net.glasslauncher.alwaysmoreitems.gui.widget.ActionButtonWidget;
import net.glasslauncher.alwaysmoreitems.gui.widget.SearchTextFieldWidget;
import net.glasslauncher.alwaysmoreitems.network.ActionButtonC2SPacket;
import net.minecraft.class_564;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

public class OverlayScreen extends Screen {

    // Parent Screen
    public HandledScreen parent;

    // Search Field
    public SearchTextFieldWidget searchField;
    public static int searchFieldWidth = 180;

    // Trash Button
    public ActionButtonWidget trashButton;

    // Action Buttons
    ArrayList<ActionButtonWidget> actionButtons;
    public static int maxActionButtonPanelWidth = 100;
    public static int actionButtonOffset = 2;

    // Tooltip
    public String currentTooltip;
    int tooltipYOffset = 0;
    int tooltipXOffset = 0;

    // Item Overlay
    public static int maxItemListWidth = 10;
    public static int maxItemListHeight = 10;
    public static int itemSize = 18;
    ArrayList<ItemRenderEntry> renderedItems;
    public ButtonWidget nextButton;
    public ButtonWidget previousButton;
    int currentPage = 7;
    int pageCount = 11;
    boolean rolloverPage = true;
    public boolean flipScrollDirection = false;

    // Screen Rescaling Stuff
    int lastWidth = 0;
    int lastHeight = 0;
    class_564 screenScaler;

    public OverlayScreen(HandledScreen parent) {
        this.parent = parent;
    }

    @Override
    public void init() {
        int id = 100;

        buttons.clear();

        // Tooltip
        currentTooltip = "";

        // Search Field
        searchField = new SearchTextFieldWidget(textRenderer, (width / 2) - (searchFieldWidth / 2), height - 25, searchFieldWidth, 20);
        searchField.setMaxLength(64);
        searchField.setText(SearchHelper.searchTerm);

        // Item Overlay
        previousButton = new ButtonWidget(10, getOverlayStartX(), 0, 20, 20, "<");
        buttons.add(previousButton);
        nextButton = new ButtonWidget(11, width - 20, 0, 20, 20, ">");
        buttons.add(nextButton);

        // Action Buttons
        actionButtons = new ArrayList<>();
        int actionButtonX = 0;
        int actionButtonY = 0;
        int maxHeightForLine = 0;

        for (var action : ActionButtonRegistry.registry.entrySet()) {
            if (action.getValue().dontAddToScreen()) {
                continue;
            }

            if (actionButtonX + action.getValue().getWidth() > maxActionButtonPanelWidth) {
                actionButtonX = 0;
                actionButtonY += maxHeightForLine + actionButtonOffset;
                maxHeightForLine = 0;
            }

            if (action.getValue().getHeight() > maxHeightForLine) {
                maxHeightForLine = action.getValue().getHeight();
            }

            ActionButtonWidget widget = new ActionButtonWidget(id++, actionButtonX, actionButtonY, action.getValue().getWidth(), action.getValue().getHeight(), action.getValue().getTexture());
            actionButtons.add(widget);
            widget.action = action.getValue();
            widget.actionIdentifier = action.getKey();

            actionButtonX += action.getValue().getWidth() + actionButtonOffset;
        }

        // Trash Button
        trashButton = new ActionButtonWidget(id++, 0, height - 20, 90, 20, "button.always_more_items.trash", "button.always_more_items.trash.alt");
        trashButton.actionIdentifier = AlwaysMoreItems.NAMESPACE.id("trash");
        trashButton.action = ActionButtonRegistry.get(trashButton.actionIdentifier);
        actionButtons.add(trashButton);
    }

    int counter = 20;

    @Override
    public void tick() {
        rescale();
        if (counter == 0) {
            rebuildRenderList();
            counter = 50;
        }
        counter--;
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        // Draw Regular Buttons
        super.render(mouseX, mouseY, delta);

        // Reset Tooltip
        currentTooltip = "";

        // Draw Items
        if (renderedItems != null) {
            for (ItemRenderEntry item : renderedItems) {
                RenderHelper.drawItemStack(item.x, item.y, item.item, false);
            }
        }

        // Draw Search Field
        searchField.draw(mouseX, mouseY);

        // Draw CAAALM
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            textRenderer.drawWithShadow("CAALM", 120, 0, 16722100);
        }

        // Draw Page Number
        String pageNumberString = currentPage + "/" + pageCount;
        textRenderer.drawWithShadow(
                pageNumberString,
                (width - ((width - getOverlayStartX()) / 2)) - (textRenderer.getWidth(pageNumberString) / 2),
                6,
                -1
        );

        // Draw Action Buttons
        for (var actionButton : actionButtons) {
            actionButton.render(minecraft, mouseX, mouseY);

            if (!actionButton.action.tooltipEnabled()) {
                continue;
            }

            if (actionButton.isMouseOver(minecraft, mouseX, mouseY)) {
                boolean holdingShift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
                String translationKey = "actionButton." + actionButton.actionIdentifier.namespace + "." + actionButton.actionIdentifier.path;

                if (holdingShift && !TranslationStorage.getInstance().get(translationKey + ".alt").equals(translationKey + ".alt")) {
                    currentTooltip = TranslationStorage.getInstance().get(translationKey + ".alt");
                } else {
                    currentTooltip = TranslationStorage.getInstance().get(translationKey);
                }
            }
        }

        // Tooltip offsets
        tooltipXOffset = 9;
        tooltipYOffset = -15;

        // If the mouse is so close to the top of the screen that the tooltip would get cut off, render it at the height of mouse cursor
        if (mouseY < -tooltipYOffset) {
            tooltipYOffset = 0;
        }

        // Draw Tooltip
        if (!currentTooltip.isEmpty()) {
            int tooltipWidth = textRenderer.getWidth(currentTooltip) + 4;
            fillGradient(mouseX + tooltipXOffset, mouseY + tooltipYOffset, mouseX + tooltipWidth + tooltipXOffset, mouseY + 12 + tooltipYOffset + 2, -1073741824, -1073741824); // -1073741824
            textRenderer.drawWithShadow(currentTooltip, mouseX + tooltipXOffset + 3, mouseY + tooltipYOffset + 3, -1);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        System.out.println("MOUSE X : " + mouseX + " | MOUSE Y : " + mouseY);

        super.mouseClicked(mouseX, mouseY, button);

        // Search Field
        searchField.mouseClicked(mouseX, mouseY, button);
        SearchHelper.searchTerm = searchField.getText();

        for (var actionButton : actionButtons) {
            if (actionButton.isMouseOver(minecraft, mouseX, mouseY)) {
                this.minecraft.soundManager.playSound(actionButton.action.getClickSound(), 1.0F, 1.0F);
                boolean holdingShift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
                if (!minecraft.world.isRemote || actionButton.action.isClientsideOnly()) {
                    actionButton.performAction(minecraft, minecraft.world, minecraft.player, true, button, holdingShift);
                } else {
                    PacketHelper.send(new ActionButtonC2SPacket(actionButton.actionIdentifier, button, holdingShift));
                }
            }
        }
    }

    @Override
    public void onMouseEvent() {
        super.onMouseEvent();

        int mouseX = Mouse.getEventX() * this.width / this.minecraft.displayWidth;
        if (mouseX >= getOverlayStartX()) {
            if (flipScrollDirection) {
                flipPage(Mouse.getEventDWheel());
            } else {
                flipPage(-Mouse.getEventDWheel());
            }
        }
    }

    @Override
    public void keyPressed(char character, int keyCode) {
        super.keyPressed(character, keyCode);

        // Search Field
        if (searchField.isSelected()) {
            searchField.keyPressed(character, keyCode);
            SearchHelper.searchTerm = searchField.getText();
        }
    }

    @Override
    protected void buttonClicked(ButtonWidget button) {
        if (button.id == 10) {
            flipPage(-1);
        }

        if (button.id == 11) {
            flipPage(1);
        }
    }

    /**
     * Checks if the display dimensions have changed since the last time the method was ran and if so,
     * recalculates the scaled width and height
     */
    public void rescale() {
        if (minecraft.displayWidth != lastWidth || minecraft.displayHeight != lastHeight) {
            screenScaler = new class_564(minecraft.options, minecraft.displayWidth, minecraft.displayHeight);
            width = screenScaler.method_1857();
            height = screenScaler.method_1858();
            lastWidth = minecraft.displayWidth;
            lastHeight = minecraft.displayHeight;
            init();
            rebuildRenderList();
        }
    }

    public int getItemListWidth() {
        int possibleOverlayStartX = ((parent.width - parent.backgroundWidth) / 2) + parent.backgroundWidth + 10;
        int itemListWidth = Math.min(((width - possibleOverlayStartX) / itemSize), maxItemListWidth);
        return itemListWidth;
    }

    public int getItemListHeight() {
        int itemListHeight = Math.min(((height - 20) / itemSize), maxItemListHeight);
        return itemListHeight;
    }

    public int getOverlayStartX() {
        int overlayStartX = width - (getItemListWidth() * itemSize);
        return overlayStartX;
    }

    // Rebuild the list of items that are rendered
    public void rebuildRenderList() {
        // Discovered
        ArrayList<ItemStack> discoveredItems = new ArrayList<>();
        for (var item : ItemRegistry.INSTANCE.getIndexedEntries()) {
            discoveredItems.add(new ItemStack(item.value()));
        }

        // Rendered
        renderedItems = new ArrayList<>();

        int itemListWidth = getItemListWidth();
        int itemListHeight = getItemListHeight();
        int overlayStartX = getOverlayStartX();

        int itemX = 0;
        int itemY = 0;

        for (ItemStack item : discoveredItems) {
            if (itemX >= itemListWidth) {
                itemX = 0;
                itemY ++;
            }

            if(itemY >= itemListHeight) {
                continue;
            }

            renderedItems.add(new ItemRenderEntry(overlayStartX + (itemX * itemSize), 21 + (itemY * itemSize), item));

            itemX++;
        }
    }

    public void flipPage(int direction) {
        if (direction > 0) {
            if (currentPage + 1 <= pageCount) {
                currentPage++;
            } else {
                if (rolloverPage) {
                    currentPage = 1;
                }
            }
        } else if (direction < 0) {
            if (currentPage - 1 >= 1) {
                currentPage--;
            } else {
                if (rolloverPage) {
                    currentPage = pageCount;
                }
            }
        }
    }

    static class ItemRenderEntry {
        int x;
        int y;
        ItemStack item;

        public ItemRenderEntry(int x, int y, ItemStack item) {
            this.x = x;
            this.y = y;
            this.item = item;
        }
    }
}
