package net.kokochi.tutorialmod.thirst;

import net.minecraft.nbt.CompoundTag;

// 사용자의 목마름 상태를 관리할 클래스
public class PlayerThirst {
    private int thirst;
    private final int MIN_THIRST = 0;
    private final int MAX_THIRST = 10;

    public int getThirst() {
        return thirst;
    }

    // 물을 마실 때 목마름 정도를 증가시켜준다.
    public void addThirst(int add) {
        this.thirst = Math.min(thirst + add, MAX_THIRST);
    }

    // 목마름 정도를 내린다.
    public void subThirst(int sub) {
        this.thirst = Math.max(thirst - sub, MIN_THIRST);
    }

    public void copyForm(PlayerThirst source) {
        this.thirst = source.thirst;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("thirst", thirst);
    }

    public void loadNBTData(CompoundTag nbt) {
        this.thirst = nbt.getInt("thirst");
    }
}
