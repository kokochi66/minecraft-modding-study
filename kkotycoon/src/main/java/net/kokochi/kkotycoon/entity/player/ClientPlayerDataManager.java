package net.kokochi.kkotycoon.entity.player;

import net.kokochi.kkotycoon.entity.codex.CodexInfo;

import java.util.ArrayList;
import java.util.List;

public class ClientPlayerDataManager {
    public static KkotycoonPlayerData playerData = new KkotycoonPlayerData();
    public static List<CodexInfo> codexList = new ArrayList<>();
    public static void setPlayerData(KkotycoonPlayerData data) {
        playerData.setCodexLevelUpStack(data.getCodexLevelUpStack());
        playerData.setCodexArray(data.getCodexArray());
        playerData.setKkoCoin(data.getKkoCoin());
        playerData.setLastReceivedCodexRewardDate(data.getLastReceivedCodexRewardDate());
        playerData.setLastPurchaseProductDate(data.getLastPurchaseProductDate());
    }

    public static void setCodexList(List<CodexInfo> newCodexList) {
        codexList = newCodexList;
    }
}
