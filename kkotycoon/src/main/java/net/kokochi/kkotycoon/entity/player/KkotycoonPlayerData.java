package net.kokochi.kkotycoon.entity.player;

import net.kokochi.kkotycoon.client.data.CodexSet;

public class KkotycoonPlayerData {
    private byte[] codexArray;

    public byte[] getCodexArray() {
        return codexArray;
    }

    public void setCodexArray(byte[] codexArray) {
        this.codexArray = codexArray;
    }

    public KkotycoonPlayerData() {
        codexArray = new byte[CodexSet.values().length];
    }
}
