package net.kokochi.kkotycoon.client.handler;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindingHandler {
    public static final KeyBinding CATALOG_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.kkotycoon.catalog", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_O, // The keycode of the key
            "category.kkotycoon.keys" // The translation key of the keybinding's category.
    ));
}
