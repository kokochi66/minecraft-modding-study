package net.kokochi.kkotycoon.packet;


import net.minecraft.network.PacketByteBuf;

public class KkoCoinS2CGetPacket {
    private final byte[] codexArray;

    public static final String CODEX_GET_PACKET_REQUEST_ID = "kkotycoon_coin_get_response";
    public static final String CODEX_GET_PACKET_RESPONSE_ID = "codex_get_response";
    public static final String CODEX_LIST_NBT_KEY = "codex_list_nbt_key";
    public KkoCoinS2CGetPacket(byte[] codexArray) {
        this.codexArray = codexArray;
    }

    public static void encode(KkoCoinS2CGetPacket packet, PacketByteBuf buf) {
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
