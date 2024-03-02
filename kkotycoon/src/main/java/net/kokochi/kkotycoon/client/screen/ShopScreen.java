package net.kokochi.kkotycoon.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ShopScreen extends Screen {
    protected ShopScreen(Text title) {
        super(title);
    }


    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }


    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
