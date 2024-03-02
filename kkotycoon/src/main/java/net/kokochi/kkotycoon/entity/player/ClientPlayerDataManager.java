package net.kokochi.kkotycoon.entity.player;

import java.util.ArrayList;
import java.util.List;

public class ClientPlayerDataManager {
    public static KkotycoonPlayerData playerData = new KkotycoonPlayerData();
    public static List<Integer> codexList = new ArrayList<>();
    public static void setPlayerData(KkotycoonPlayerData data) {
        playerData.setCodexLevelUpStack(data.getCodexLevelUpStack());
        playerData.setCodexArray(data.getCodexArray());
        playerData.setKkoCoin(data.getKkoCoin());
        playerData.setLastReceivedCodexRewardDate(data.getLastReceivedCodexRewardDate());
    }

    public static void setCodexList(List<Integer> newCodexList) {
        codexList = newCodexList;
    }
}
