package net.kokochi.kkotycoon.packet;


import net.kokochi.kkotycoon.KkoTycoon;
import net.kokochi.kkotycoon.entity.player.KkotycoonPlayerData;
import net.kokochi.kkotycoon.entity.player.ServerPlayerDataManager;
import net.minecraft.network.PacketByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class KkotycoonMainDataS2CGetPacket {
    public static final Logger LOGGER = LoggerFactory.getLogger(KkoTycoon.MOD_ID);
    public KkotycoonPlayerData playerData;
    public List<Integer> codexList;

    public static final String CODEX_GET_PACKET_REQUEST_ID = "kkotycoon_data_get_request";
    public static final String CODEX_GET_PACKET_RESPONSE_ID = "kkotycoon_data_get_response";
    public KkotycoonMainDataS2CGetPacket(KkotycoonPlayerData playerData) {
        this.playerData = playerData;
        this.codexList = ServerPlayerDataManager.codexItemIdList;
    }

    public static void encode(KkotycoonMainDataS2CGetPacket packet, PacketByteBuf buf) {
        buf.writeByteArray(packet.playerData.getCodexArray());
        buf.writeLong(packet.playerData.getKkoCoin());
        buf.writeLong(packet.playerData.getLastReceivedCodexRewardDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        List<LocalDateTime> codexLevelUpStack = packet.playerData.getCodexLevelUpStack();
        buf.writeInt(codexLevelUpStack.size());
        for (LocalDateTime localDateTime : codexLevelUpStack) {
            buf.writeLong(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }

        int[] codexArray = new int[packet.codexList.size()];
        for (int i=0;i<packet.codexList.size();i++) {
            codexArray[i] = packet.codexList.get(i);
        }
        buf.writeIntArray(codexArray);
    }

    public static KkotycoonPlayerData decode(PacketByteBuf buf) {
        KkotycoonPlayerData data = new KkotycoonPlayerData();
        data.setCodexArray(buf.readByteArray());
        data.setKkoCoin(buf.readLong());
        data.setLastReceivedCodexRewardDate(Instant.ofEpochMilli(buf.readLong()).atZone(ZoneId.systemDefault()).toLocalDateTime());

        int stackSize = buf.readInt();
        List<LocalDateTime> codexLevelUpStack = data.getCodexLevelUpStack();
        for (int i = 0; i < stackSize; i++) {
            codexLevelUpStack.add(Instant.ofEpochMilli(buf.readLong()).atZone(ZoneId.systemDefault()).toLocalDateTime());
        }
        return data;
    }

    public static List<Integer> decodeCodexArray(PacketByteBuf buf) {
        int[] ints = buf.readIntArray();
        List<Integer> list = new ArrayList<>();
        for (int i=0;i<ints.length;i++) {
            list.add(ints[i]);
        }
        return list;
    }
}
