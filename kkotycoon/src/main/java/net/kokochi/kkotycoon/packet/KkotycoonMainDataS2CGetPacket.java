package net.kokochi.kkotycoon.packet;


import net.kokochi.kkotycoon.KkoTycoon;
import net.kokochi.kkotycoon.entity.player.KkotycoonPlayerData;
import net.minecraft.network.PacketByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KkotycoonMainDataS2CGetPacket {
    public static final Logger LOGGER = LoggerFactory.getLogger(KkoTycoon.MOD_ID);
    public KkotycoonPlayerData playerData;

    public static final String CODEX_GET_PACKET_REQUEST_ID = "kkotycoon_data_get_request";
    public static final String CODEX_GET_PACKET_RESPONSE_ID = "kkotycoon_data_get_response";
    public static final String CODEX_LIST_NBT_KEY = "codex_list_nbt_key";
    public KkotycoonMainDataS2CGetPacket(KkotycoonPlayerData playerData) {
        this.playerData = playerData;
    }

    public static void encode(KkotycoonMainDataS2CGetPacket packet, PacketByteBuf buf) {
        buf.writeByteArray(packet.playerData.getCodexArray());
        buf.writeLong(packet.playerData.getKkoCoin());
    }

    public static KkotycoonPlayerData decode(PacketByteBuf buf) {
        KkotycoonPlayerData data = new KkotycoonPlayerData();
        data.setCodexArray(buf.readByteArray());
        data.setKkoCoin(buf.readLong());
        return data;
    }
}
