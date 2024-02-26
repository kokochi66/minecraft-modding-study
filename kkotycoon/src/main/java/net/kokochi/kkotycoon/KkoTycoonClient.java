package net.kokochi.kkotycoon;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.kokochi.kkotycoon.client.packet.CodexS2CGetPacket;
import net.kokochi.kkotycoon.client.screen.CodexScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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

        // 클라이언트가 월드에 진입 시의 실행될 로직을 등록
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
            Identifier identifier = new Identifier(KkoTycoon.MOD_ID, CodexS2CGetPacket.CODEX_GET_PACKET_REQUEST_ID);
            ClientPlayNetworking.send(identifier, packetByteBuf);
        });

        ClientPlayNetworking.registerGlobalReceiver(
                new Identifier(KkoTycoon.MOD_ID, CodexS2CGetPacket.CODEX_GET_PACKET_RESPONSE_ID), (client, handler, buf, responseSender) ->
                {
                    // 서버로부터 받은 도감 정보를 디코딩하는 로직
                    byte[] codexArray = CodexS2CGetPacket.decode(buf);
                    client.execute(() -> {
                        // 받은 도감 정보를 클라이언트 내부 저장소에 저장
                        NbtCompound nbtCompound = new NbtCompound();
                        nbtCompound.putByteArray(CodexS2CGetPacket.CODEX_LIST_NBT_KEY, codexArray);
                        if (client.player != null) {
                            LOGGER.info(client.player.getUuidAsString() + " player get packet response");
                            client.player.writeNbt(nbtCompound);
                        }
                    });
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
    }
}
