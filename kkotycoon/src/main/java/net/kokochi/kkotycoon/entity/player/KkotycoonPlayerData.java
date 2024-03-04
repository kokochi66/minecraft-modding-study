package net.kokochi.kkotycoon.entity.player;


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

    public KkotycoonPlayerData() {
        this.codexArray = new byte[350];
        this.kkoCoin = 0L;
        this.lastReceivedCodexRewardDate = LocalDateTime.now();
        this.codexLevelUpStack = new ArrayList<>();
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
}
