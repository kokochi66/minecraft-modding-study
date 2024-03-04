package net.kokochi.kkotycoon.packet;


import net.kokochi.kkotycoon.KkoTycoon;
import net.kokochi.kkotycoon.entity.codex.CodexInfo;
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
    public List<CodexInfo> codexList;

    public static final String CODEX_GET_PACKET_REQUEST_ID = "kkotycoon_data_get_request";
    public static final String CODEX_GET_PACKET_RESPONSE_ID = "kkotycoon_data_get_response";
    // 서버 용도로만 사용되는 constructor 입니다. (서버 데이터 매니저를 사용하기 때문에)
    public KkotycoonMainDataS2CGetPacket(KkotycoonPlayerData playerData) {
        this.playerData = playerData;
        this.codexList = ServerPlayerDataManager.codexInfoList;
    }

    public KkotycoonMainDataS2CGetPacket(KkotycoonPlayerData playerData, List<CodexInfo> codexList) {
        this.playerData = playerData;
        this.codexList = codexList;
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

        buf.writeBoolean(packet.playerData.getLastPurchaseProductDate() != null);
        if (packet.playerData.getLastPurchaseProductDate() != null) {
            buf.writeLong(packet.playerData.getLastPurchaseProductDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }

        buf.writeInt(packet.codexList.size());
        for (int i=0;i<packet.codexList.size();i++) {
            CodexInfo codexInfo = packet.codexList.get(i);
            buf.writeInt(codexInfo.getItemId());
            buf.writeInt(codexInfo.getCount());
        }
    }

    public static KkotycoonMainDataS2CGetPacket decode(PacketByteBuf buf) {
        KkotycoonPlayerData data = new KkotycoonPlayerData();
        data.setCodexArray(buf.readByteArray());
        data.setKkoCoin(buf.readLong());
        data.setLastReceivedCodexRewardDate(Instant.ofEpochMilli(buf.readLong()).atZone(ZoneId.systemDefault()).toLocalDateTime());

        int stackSize = buf.readInt();
        List<LocalDateTime> codexLevelUpStack = data.getCodexLevelUpStack();
        for (int i = 0; i < stackSize; i++) {
            codexLevelUpStack.add(Instant.ofEpochMilli(buf.readLong()).atZone(ZoneId.systemDefault()).toLocalDateTime());
        }

        boolean lastPurchaseProductDateIsExists = buf.readBoolean();
        if (lastPurchaseProductDateIsExists) {
            data.setLastPurchaseProductDate(Instant.ofEpochMilli(buf.readLong()).atZone(ZoneId.systemDefault()).toLocalDateTime());
        }

        int codexSize = buf.readInt();
        List<CodexInfo> bufCodexInfos = new ArrayList<>();
        for (int i=0;i<codexSize;i++) {
            int itemId = buf.readInt();
            int count = buf.readInt();
            bufCodexInfos.add(new CodexInfo(itemId, count));
        }

        return new KkotycoonMainDataS2CGetPacket(data, bufCodexInfos);
    }

}
