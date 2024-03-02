package net.kokochi.kkotycoon.entity.player;

public class ClientPlayerDataManager {
    public static KkotycoonPlayerData playerData = new KkotycoonPlayerData();

    public static void setPlayerData(KkotycoonPlayerData data) {
        playerData.setCodexLevelUpStack(data.getCodexLevelUpStack());
        playerData.setCodexArray(data.getCodexArray());
        playerData.setKkoCoin(data.getKkoCoin());
        playerData.setLastReceivedCodexRewardDate(data.getLastReceivedCodexRewardDate());
    }
}
