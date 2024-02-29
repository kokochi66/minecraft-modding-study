package net.kokochi.kkotycoon.entity.player;

public class ClientPlayerDataManager {
    public static KkotycoonPlayerData playerData = new KkotycoonPlayerData();

    public static void setPlayerData(KkotycoonPlayerData data) {
        playerData = data;
    }
}
