package net.kokochi.kkotycoon.entity.player;


import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class PlayerDeathInvenHistory {
    private List<ItemStack> itemStacks;
    private Pair<Integer, Float> levelInfo;
    private LocalDateTime deathDate;

    public PlayerDeathInvenHistory(List<ItemStack> itemStacks, Pair<Integer, Float> levelInfo) {
        this.itemStacks = itemStacks;
        this.levelInfo = levelInfo;
        this.deathDate = LocalDateTime.now();
    }

    public PlayerDeathInvenHistory(List<ItemStack> itemStacks, Pair<Integer, Float> levelInfo, LocalDateTime deathDate) {
        this.itemStacks = itemStacks;
        this.levelInfo = levelInfo;
        this.deathDate = deathDate;
    }

    public List<ItemStack> getItemStacks() {
        return itemStacks;
    }

    public void setItemStacks(List<ItemStack> itemStacks) {
        this.itemStacks = itemStacks;
    }

    public Pair<Integer, Float> getLevelInfo() {
        return levelInfo;
    }

    public void setLevelInfo(Pair<Integer, Float> levelInfo) {
        this.levelInfo = levelInfo;
    }

    public LocalDateTime getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(LocalDateTime deathDate) {
        this.deathDate = deathDate;
    }
}
