package net.kokochi.kkotycoon.entity.player;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class KkotycoonPlayerData {
    private byte[] codexArray;
    private long kkoCoin;
    private LocalDateTime lastReceivedCodexRewardDate;
    private List<LocalDateTime> codexLevelUpStack;
    private LocalDateTime lastPurchaseProductDate;
    private String playerName;


    // 누적 데이터
    private double accumulatedDistance;     // 누적 이동 거리
    private Vec3d previousPosition;
    private int accumulatedBreakOreBlock;            // 누적 캔 광물 블록
    private int accumulatedBreakCropBlock;          // 누적 캔 농작물 블록
    private int accumulatedBlock;          // 누적 캔블록
    private int accumulatedKilledMonster;              // 누적 몬스터 킬 수
    private int accumulatedKilledAnimal;          // 누적 동물 킬 수
    private double accumulatedDamaged;      // 누적 입은 데미지 량
    private double accumulatedAttack;       // 누적 입힌 피해량
    private long accumulatedPlayTime;       // 누적 플레이 타임
    private LocalDateTime loginDate;        // 접속시간
    private int accumulatedOnBlock;         // 누적 설치 블록 개수
    private int accumulatedDeathCount;      // 사망 회수


    // 사망 시 아이템 복구를 위한 데이터 저장
    private List<ItemStack> itemStacks;
    private Pair<Integer, Float> levelInfo;

    // 복구 실패했을 때를 대비하여 사용자 복구 데이터를 다 저장해줍니다.
    private List<PlayerDeathInvenHistory> deathInvenHistories;


    public KkotycoonPlayerData() {
        this.codexArray = new byte[350];
        this.kkoCoin = 0L;
        this.lastReceivedCodexRewardDate = LocalDateTime.now();
        this.codexLevelUpStack = new ArrayList<>();
        this.accumulatedDistance = 0.0d;
        this.deathInvenHistories = new ArrayList<>();
    }

    public KkotycoonPlayerData(String playerName) {
        this.playerName = playerName;
        this.codexArray = new byte[350];
        this.kkoCoin = 0L;
        this.lastReceivedCodexRewardDate = LocalDateTime.now();
        this.codexLevelUpStack = new ArrayList<>();
        this.accumulatedDistance = 0.0d;
        this.deathInvenHistories = new ArrayList<>();
    }


    private static final long REWARD_AMOUNT = 100L;
    private static final long REWARD_TICK = 5L;
    private static final ChronoUnit REWARD_TIMES = ChronoUnit.MINUTES;
    // 현재 쌓여있는 보상 계산 로직
    public long calculCodexReward() {
        long totalReward = 0;
        int codexCount = countCodex() - codexLevelUpStack.size();
        LocalDateTime processingTime = this.lastReceivedCodexRewardDate;

        long countStackDiff = 0;
        for (LocalDateTime localDateTime : codexLevelUpStack) {
            if (localDateTime.isAfter(processingTime)) {
                long minutesDiff = (between(processingTime, localDateTime)) - countStackDiff;
                // 분이 지난 만큼 보상을 추가합니다.
                totalReward += (REWARD_AMOUNT + (REWARD_TICK * codexCount)) * minutesDiff;
                countStackDiff += minutesDiff;
                codexCount++;
            }
        }

        // 마지막으로 현재시간까지의 추가 보상을 추가합니다.
        long minutesDiff = (between(processingTime, LocalDateTime.now())) - countStackDiff;
        totalReward += (REWARD_AMOUNT + (REWARD_TICK * codexCount)) * minutesDiff;

        return totalReward;
    }

    public static long between(LocalDateTime a, LocalDateTime b) {
        return REWARD_TIMES.between(a, b);
    }

    private int countCodex() {
        int count = 0;
        for (byte codex : codexArray) {
            if (codex == 1) count++;
        }
        return count;
    }

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

    public LocalDateTime getLastReceivedCodexRewardDate() {
        return lastReceivedCodexRewardDate;
    }

    public void setLastReceivedCodexRewardDate(LocalDateTime lastReceivedCodexRewardDate) {
        this.lastReceivedCodexRewardDate = lastReceivedCodexRewardDate;
    }

    public List<LocalDateTime> getCodexLevelUpStack() {
        return codexLevelUpStack;
    }

    public void setCodexLevelUpStack(List<LocalDateTime> codexLevelUpStack) {
        this.codexLevelUpStack = codexLevelUpStack;
    }

    public LocalDateTime getLastPurchaseProductDate() {
        return lastPurchaseProductDate;
    }

    public void setLastPurchaseProductDate(LocalDateTime lastPurchaseProductDate) {
        this.lastPurchaseProductDate = lastPurchaseProductDate;
    }

    public double getAccumulatedDistance() {
        return accumulatedDistance;
    }

    public void setAccumulatedDistance(double accumulatedDistance) {
        this.accumulatedDistance = accumulatedDistance;
    }

    public Vec3d getPreviousPosition() {
        return previousPosition;
    }

    public void setPreviousPosition(Vec3d previousPosition) {
        this.previousPosition = previousPosition;
    }

    public int getAccumulatedBreakOreBlock() {
        return accumulatedBreakOreBlock;
    }

    public void setAccumulatedBreakOreBlock(int accumulatedBreakOreBlock) {
        this.accumulatedBreakOreBlock = accumulatedBreakOreBlock;
    }

    public int getAccumulatedBreakCropBlock() {
        return accumulatedBreakCropBlock;
    }

    public void setAccumulatedBreakCropBlock(int accumulatedBreakCropBlock) {
        this.accumulatedBreakCropBlock = accumulatedBreakCropBlock;
    }

    public int getAccumulatedKilledMonster() {
        return accumulatedKilledMonster;
    }

    public void setAccumulatedKilledMonster(int accumulatedKilledMonster) {
        this.accumulatedKilledMonster = accumulatedKilledMonster;
    }

    public int getAccumulatedKilledAnimal() {
        return accumulatedKilledAnimal;
    }

    public void setAccumulatedKilledAnimal(int accumulatedKilledAnimal) {
        this.accumulatedKilledAnimal = accumulatedKilledAnimal;
    }

    public double getAccumulatedDamaged() {
        return accumulatedDamaged;
    }

    public void setAccumulatedDamaged(double accumulatedDamaged) {
        this.accumulatedDamaged = accumulatedDamaged;
    }

    public double getAccumulatedAttack() {
        return accumulatedAttack;
    }

    public void setAccumulatedAttack(double accumulatedAttack) {
        this.accumulatedAttack = accumulatedAttack;
    }

    public long getAccumulatedPlayTime() {
        return accumulatedPlayTime;
    }

    public void setAccumulatedPlayTime(long accumulatedPlayTime) {
        this.accumulatedPlayTime = accumulatedPlayTime;
    }

    public int getAccumulatedOnBlock() {
        return accumulatedOnBlock;
    }

    public void setAccumulatedOnBlock(int accumulatedOnBlock) {
        this.accumulatedOnBlock = accumulatedOnBlock;
    }

    public int getAccumulatedBlock() {
        return accumulatedBlock;
    }

    public void setAccumulatedBlock(int accumulatedBlock) {
        this.accumulatedBlock = accumulatedBlock;
    }

    public LocalDateTime getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(LocalDateTime loginDate) {
        this.loginDate = loginDate;
    }

    public int getAccumulatedDeathCount() {
        return accumulatedDeathCount;
    }

    public void setAccumulatedDeathCount(int accumulatedDeathCount) {
        this.accumulatedDeathCount = accumulatedDeathCount;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
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

    public List<PlayerDeathInvenHistory> getDeathInvenHistories() {
        return deathInvenHistories;
    }

    public void setDeathInvenHistories(List<PlayerDeathInvenHistory> deathInvenHistories) {
        this.deathInvenHistories = deathInvenHistories;
    }
}
