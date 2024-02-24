package net.kokochi.kkotycoon.client.data;

import net.minecraft.nbt.NbtCompound;

import java.util.Arrays;
import java.util.Map;

public class PlayerCodex {

    private Map<CodexSet, Boolean> codexMap;


    public void initCodexMap() {
        Arrays.stream(CodexSet.values()).forEach(codexSet -> {
            codexMap.put(codexSet, false);
        });
    }
    public boolean isCompleteCodex(CodexSet codexSet) {
        return codexMap.get(codexSet);
    }

    public void completeCodex(NbtCompound nbt, CodexSet codexSet) {
        nbt.putBoolean("player_codex_" + codexSet.name(), true);
    }
}
