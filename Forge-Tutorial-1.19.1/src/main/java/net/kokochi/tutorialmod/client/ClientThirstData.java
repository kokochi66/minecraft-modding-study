package net.kokochi.tutorialmod.client;

public class ClientThirstData {
    // 클라이언트 사이드에서만 관리되는 데이터입니다. (이것이 서버 사이드에서는 사용되면 안됨)
    // static 한 변수를 저장해두었다가 사용하는 형태이기 때문에 해당 클라이언트의 고유한 값임.
    private static int playerThirst;

    public static void set(int thirst) {
        ClientThirstData.playerThirst = thirst;
    }

    public static int getPlayerThirst() {
        return playerThirst;
    }
}
