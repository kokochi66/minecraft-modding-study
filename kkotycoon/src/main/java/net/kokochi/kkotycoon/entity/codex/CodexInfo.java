package net.kokochi.kkotycoon.entity.codex;

public class CodexInfo {
    private int itemId;
    private int count;

    public CodexInfo(int itemId, int count) {
        this.itemId = itemId;
        this.count = count;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
