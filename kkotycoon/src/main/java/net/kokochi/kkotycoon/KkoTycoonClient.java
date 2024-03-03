package net.kokochi.kkotycoon;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.kokochi.kkotycoon.client.screen.ShopScreen;
import net.kokochi.kkotycoon.entity.item.KkoTycoonItems;
import net.kokochi.kkotycoon.entity.player.KkotycoonPlayerData;
import net.kokochi.kkotycoon.packet.KkotycoonMainDataS2CGetPacket;
import net.kokochi.kkotycoon.client.screen.CodexScreen;
import net.kokochi.kkotycoon.entity.player.ClientPlayerDataManager;
import net.kokochi.kkotycoon.packet.ShopScreenS2CPacket;
import net.kokochi.kkotycoon.server.handler.PlayerActionEventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;

public class KkoTycoonClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(KkoTycoon.MOD_ID);

    // 도감이 열렸는지 여부 변수
    private static boolean isCodexOpen = false;

    private static final KeyBinding CODEX_OPEN_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.kkotycoon.o_key", // 키바인딩의 이름에 대한 번역 키
            InputUtil.Type.KEYSYM, // 키바인딩 타입 (키보드)
            GLFW.GLFW_KEY_V, // 'v' 키에 대한 GLFW 키 코드
            "category.kkotycoon.keys" // 키바인딩 카테고리에 대한 번역 키
    ));

    @Override
    public void onInitializeClient() {
        LOGGER.info("Hello Fabric world! client");
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
            Identifier identifier = new Identifier(KkoTycoon.MOD_ID, KkotycoonMainDataS2CGetPacket.CODEX_GET_PACKET_REQUEST_ID);
            ClientPlayNetworking.send(identifier, packetByteBuf);
        });

        ClientPlayNetworking.registerGlobalReceiver(
                new Identifier(KkoTycoon.MOD_ID, KkotycoonMainDataS2CGetPacket.CODEX_GET_PACKET_RESPONSE_ID), (client, handler, buf, responseSender) ->
                {
                    // 서버로부터 받은 도감 정보를 디코딩하는 로직
                    KkotycoonPlayerData playerData = KkotycoonMainDataS2CGetPacket.decode(buf);
                    ClientPlayerDataManager.setPlayerData(playerData);
                    ClientPlayerDataManager.setCodexList(KkotycoonMainDataS2CGetPacket.decodeCodexArray(buf));
                });

        ClientPlayNetworking.registerGlobalReceiver(
                new Identifier(KkoTycoon.MOD_ID, ShopScreenS2CPacket.SHOP_OPEN_SCREEN_REQUEST_ID), (client, handler, buf, responseSender) ->
                {
                    if (client != null && client.player != null) {
                        client.execute(() -> {  // 메인 렌더링 스레드에서 실행되도록 예약
                            MinecraftClient.getInstance().setScreen(new ShopScreen(Text.of("상점")));
                        });

                    }
                });

        // ctrl + d를 눌렀을 때 도감이 열리는 것을 설정
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client != null && client.player != null) {

                while (CODEX_OPEN_KEY.wasPressed()) {
                    isCodexOpen = !isCodexOpen;
                    MinecraftClient.getInstance().setScreen(new CodexScreen(Text.literal("도감")));
                }
            }
        });

        // 툴팁 관련 수정내용
        PlayerActionEventHandler.initPlayerActionClientEvent();


        // HUD에 콘텐츠 그리기 이벤트 등록
        HudRenderCallback.EVENT.register((matrixStack, delta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            TextRenderer textRenderer = client.textRenderer;

            KkotycoonPlayerData playerData = ClientPlayerDataManager.playerData;

            // 코인 보유량을 표시하기위한 박스 HUD
            String message = NumberFormat.getInstance().format(playerData.getKkoCoin());
            int messageWidth = textRenderer.getWidth(message);
            int iconWidth = 15;

            int paddingX = 7;
            int paddingY = 5;
            int backgroundX = 10;
            int backgroundY = 6;
            int backgroundWidth = messageWidth + (paddingX * 2) + iconWidth;
            int backgroundHeight = textRenderer.fontHeight + (paddingY * 2);
            int backgroundColor = 0x6A000000; // 반투명 검정색
            matrixStack.fill(backgroundX, backgroundY, backgroundX + backgroundWidth, backgroundY + backgroundHeight, backgroundColor);

            // 메시지 텍스트 그리기
            matrixStack.drawText(textRenderer, Text.of(message), backgroundX + paddingX, backgroundY + paddingY, 0xFFFFFF, true);
            matrixStack.drawItem(new ItemStack(KkoTycoonItems.KKO_COIN), backgroundX + paddingX + messageWidth + 2, backgroundY + paddingY - 4);
        });

        // 모드 아이템 생성
        KkoTycoonItems.initModItems();
    }
}
