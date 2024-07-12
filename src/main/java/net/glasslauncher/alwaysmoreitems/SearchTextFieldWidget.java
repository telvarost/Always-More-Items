package net.glasslauncher.alwaysmoreitems;

import net.glasslauncher.mods.gcapi.impl.screen.widget.ExtensibleTextFieldWidget;
import net.minecraft.client.font.TextRenderer;

public class SearchTextFieldWidget extends ExtensibleTextFieldWidget {

    public SearchTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height) {
        super(textRenderer);
        glass_config_api$setXYWH(x, y, width, height);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if(mouseX >= getXYWH()[0] && mouseX < getXYWH()[0] + getXYWH()[2] && mouseY >= getXYWH()[1] && mouseY < getXYWH()[1] + getXYWH()[3] && button == 1){
            this.setText("");
        }

        super.mouseClicked(mouseX, mouseY, button);
    }
}
