package net.kokochi.kkotycoon.entity.player;

import net.kokochi.kkotycoon.client.data.CodexSet;

public class KkotycoonPlayerData {
    private byte[] codexArray;
    private long kkoCoin;

    public byte[] getCodexArray() {
        return codexArray;
    }

    public void setCodexArray(byte[] codexArray) {
        this.codexArray = codexArray;
    }

    public long getKkoCoin() {
        return kkoCoin;
    }

    public void setKkoCoin(long kkoCoin) {
        this.kkoCoin = kkoCoin;
    }

    public KkotycoonPlayerData() {
        this.codexArray = new byte[CodexSet.values().length];
        this.kkoCoin = 0L;
    }
}
