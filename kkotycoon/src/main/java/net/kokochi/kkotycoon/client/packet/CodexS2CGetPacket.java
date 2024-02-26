package net.kokochi.kkotycoon.client.packet;


import net.kokochi.kkotycoon.KkoTycoon;
import net.kokochi.kkotycoon.client.data.CodexSet;
import net.minecraft.network.PacketByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CodexS2CGetPacket {
    public static final Logger LOGGER = LoggerFactory.getLogger(KkoTycoon.MOD_ID);
    private final byte[] codexArray;

    public static final String CODEX_GET_PACKET_REQUEST_ID = "codex_get_request";
    public static final String CODEX_GET_PACKET_RESPONSE_ID = "codex_get_response";
    public static final String CODEX_LIST_NBT_KEY = "codex_list_nbt_key";
    public CodexS2CGetPacket(byte[] codexArray) {
        this.codexArray = codexArray;
    }

    public static void encode(CodexS2CGetPacket packet, PacketByteBuf buf) {
        byte[] packetArray = packet.getCodexArray();
        buf.writeByteArray(packetArray);
    }

    public static byte[] decode(PacketByteBuf buf) {
        return buf.readByteArray();
    }

    public byte[] getCodexArray() {
        return codexArray;
    }
}
