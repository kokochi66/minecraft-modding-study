package net.kokochi.kkotycoon;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.kokochi.kkotycoon.client.screen.CodexScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KkoTycoonClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(KkoTycoon.MOD_ID);

    // 도감이 열렸는지 여부 변수
    private static boolean isCodexOpen = false;

    private static final KeyBinding CODEX_OPEN_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.kkotycoon.o_key", // 키바인딩의 이름에 대한 번역 키
            InputUtil.Type.KEYSYM, // 키바인딩 타입 (키보드)
            GLFW.GLFW_KEY_O, // 'o' 키에 대한 GLFW 키 코드
            "category.kkotycoon.keys" // 키바인딩 카테고리에 대한 번역 키
    ));
    @Override
    public void onInitializeClient() {
        LOGGER.info("Hello Fabric world! client");

        // ctrl + d를 눌렀을 때 도감이 열리는 것을 설정
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client != null && client.player != null) {

                while (CODEX_OPEN_KEY.wasPressed()) {
                    isCodexOpen = !isCodexOpen;
                    MinecraftClient.getInstance().setScreen(new CodexScreen(Text.literal("도감")));
                }
            }
        });
    }
}
