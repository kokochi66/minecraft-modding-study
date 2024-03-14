package net.kokochi.kkotycoon.entity.player;

import net.fabricmc.fabric.api.util.NbtType;
import net.kokochi.kkotycoon.KkoTycoon;
import net.kokochi.kkotycoon.entity.codex.CodexInfo;
import net.kokochi.kkotycoon.entity.codex.CodexSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class ServerPlayerDataManager extends PersistentState {
    public static HashMap<UUID, KkotycoonPlayerData> playerDataMap = new HashMap<>();
    public static List<CodexInfo> codexInfoList = new ArrayList<>();

    public ServerPlayerDataManager() {
        playerDataMap = new HashMap<>();
        codexInfoList = Arrays.stream(CodexSet.values())
                .map(codexSet -> new CodexInfo(Item.getRawId(codexSet.getItem()), codexSet.getCount()))
                .collect(Collectors.toList());
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound playersNbt = new NbtCompound();
        playerDataMap.forEach((uuid, playerData) -> {
            NbtCompound playerNbt = new NbtCompound();
            playerNbt.putByteArray("codexArray", playerData.getCodexArray());
            playerNbt.putLong("kkoCoin", playerData.getKkoCoin());
            if (playerData.getLastReceivedCodexRewardDate() != null) {
                playerNbt.putLong("lastReceivedCodexRewardDate", playerData.getLastReceivedCodexRewardDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            }
            playerNbt.putString("playerName", playerData.getPlayerName());

            List<LocalDateTime> codexLevelUpStack = playerData.getCodexLevelUpStack();
            if (codexLevelUpStack != null) {
                long[] codexLevelUpStacksArray = new long[codexLevelUpStack.size()];
                for (int i=0;i<codexLevelUpStack.size();i++){
                    codexLevelUpStacksArray[i] = codexLevelUpStack.get(i).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                }
                playerNbt.putLongArray("codexLevelUpStack", codexLevelUpStacksArray);
            }


            if (playerData.getLastPurchaseProductDate() != null) {
                playerNbt.putLong("lastPurchaseProductDate", playerData.getLastPurchaseProductDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            }

            // 누적 데이터
            playerNbt.putDouble("accumulatedDistance", playerData.getAccumulatedDistance());
            playerNbt.putInt("accumulatedBreakOreBlock", playerData.getAccumulatedBreakOreBlock());
            playerNbt.putInt("accumulatedBreakCropBlock", playerData.getAccumulatedBreakCropBlock());
            playerNbt.putInt("accumulatedBlock", playerData.getAccumulatedBlock());
            playerNbt.putInt("accumulatedKilledMonster", playerData.getAccumulatedKilledMonster());
            playerNbt.putInt("accumulatedKilledAnimal", playerData.getAccumulatedKilledAnimal());
            playerNbt.putDouble("accumulatedDamaged", playerData.getAccumulatedDamaged());
            playerNbt.putDouble("accumulatedAttack", playerData.getAccumulatedAttack());
            playerNbt.putLong("accumulatedPlayTime", playerData.getAccumulatedPlayTime());
            playerNbt.putInt("accumulatedOnBlock", playerData.getAccumulatedOnBlock());
            playerNbt.putInt("accumulatedDeathCount", playerData.getAccumulatedDeathCount());

            // 인벤토리 세이브시 아이템 부활을 위한 데이터 저장
            List<ItemStack> itemStacks = playerData.getItemStacks();
            if (itemStacks != null) {
                NbtList itemStacksNbtList = new NbtList();
                for (ItemStack stack : itemStacks) {
                    NbtCompound itemNbt = new NbtCompound();
                    stack.writeNbt(itemNbt); // ItemStack을 NbtCompound로 변환
                    itemStacksNbtList.add(itemNbt);
                }
                playerNbt.put("itemStacks", itemStacksNbtList); // 변환된 NbtList를 playerNbt에 저장
            }

            // 레벨 정보 저장
            Pair<Integer, Float> levelInfo = playerData.getLevelInfo();
            if (levelInfo != null) {
                int level = levelInfo.getLeft(); // 레벨 정보 가져오기
                float experience = levelInfo.getRight(); // 경험치 정보 가져오기
                playerNbt.putInt("playerLevel", level); // 레벨 정보 저장
                playerNbt.putFloat("playerExperience", experience); // 경험치 정보 저장
            }

            NbtList deathInvenHistoriesNbtList = new NbtList();
            for (PlayerDeathInvenHistory history : playerData.getDeathInvenHistories()) {
                NbtCompound historyNbt = new NbtCompound();

                // 아이템 스택 저장
                NbtList itemStacksNbtList = new NbtList();
                for (ItemStack stack : history.getItemStacks()) {
                    NbtCompound itemNbt = new NbtCompound();
                    stack.writeNbt(itemNbt);
                    itemStacksNbtList.add(itemNbt);
                }
                historyNbt.put("itemStacks", itemStacksNbtList);

                // 레벨 정보 저장
                historyNbt.putInt("level", history.getLevelInfo().getLeft());
                historyNbt.putFloat("experience", history.getLevelInfo().getRight());

                // 사망 날짜 저장
                historyNbt.putLong("deathDate", history.getDeathDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

                deathInvenHistoriesNbtList.add(historyNbt);
            }
            playerNbt.put("deathInvenHistories", deathInvenHistoriesNbtList);

            playersNbt.put(uuid.toString(), playerNbt);
        });
        nbt.put("players", playersNbt);

        nbt.putIntArray("codexItemIdList", codexInfoList.stream()
                .mapToInt(CodexInfo::getItemId).toArray());
        nbt.putIntArray("codexItemCountList", codexInfoList.stream()
                .mapToInt(CodexInfo::getCount).toArray());
        return nbt;
    }


    public static ServerPlayerDataManager createFromNbt(NbtCompound tag) {
        ServerPlayerDataManager state = new ServerPlayerDataManager();

        NbtCompound playersNbt = tag.getCompound("players");
        playersNbt.getKeys().forEach(key -> {
            KkotycoonPlayerData playerData = new KkotycoonPlayerData();

            NbtCompound playerNbt = playersNbt.getCompound(key);
            playerData.setCodexArray(playerNbt.getByteArray("codexArray"));
            playerData.setKkoCoin(playerNbt.getLong("kkoCoin"));
            playerData.setLastReceivedCodexRewardDate(Instant.ofEpochMilli(playerNbt.getLong("lastReceivedCodexRewardDate")).atZone(ZoneId.systemDefault()).toLocalDateTime());
            playerData.setPlayerName(playerNbt.getString("playerName"));

            long[] codexLevelUpStacksArray = playerNbt.getLongArray("codexLevelUpStack");
            List<LocalDateTime> codexLevelUpList = new ArrayList<>();
            for (long l : codexLevelUpStacksArray) {
                codexLevelUpList.add(Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDateTime());
            }
            playerData.setCodexLevelUpStack(codexLevelUpList);

            if (playerNbt.contains("lastPurchaseProductDate")) {
                playerData.setLastPurchaseProductDate(Instant.ofEpochMilli(playerNbt.getLong("lastPurchaseProductDate")).atZone(ZoneId.systemDefault()).toLocalDateTime());
            }

            // 누적 데이터
            playerData.setAccumulatedDistance(playerNbt.getDouble("accumulatedDistance"));
            playerData.setAccumulatedBreakOreBlock(playerNbt.getInt("accumulatedBreakOreBlock"));
            playerData.setAccumulatedBreakCropBlock(playerNbt.getInt("accumulatedBreakCropBlock"));
            playerData.setAccumulatedBlock(playerNbt.getInt("accumulatedBlock"));
            playerData.setAccumulatedKilledMonster(playerNbt.getInt("accumulatedKilledMonster"));
            playerData.setAccumulatedKilledAnimal(playerNbt.getInt("accumulatedKilledAnimal"));
            playerData.setAccumulatedDamaged(playerNbt.getDouble("accumulatedDamaged"));
            playerData.setAccumulatedAttack(playerNbt.getDouble("accumulatedAttack"));
            playerData.setAccumulatedPlayTime(playerNbt.getLong("accumulatedPlayTime"));
            playerData.setAccumulatedOnBlock(playerNbt.getInt("accumulatedOnBlock"));
            playerData.setAccumulatedDeathCount(playerNbt.getInt("accumulatedDeathCount"));

            // 아이템 스택 리스트 복원
            if (playerNbt.contains("itemStacks")) { // "itemStacks" 키가 존재하는지 확인
                NbtList itemStacksNbtList = playerNbt.getList("itemStacks", NbtElement.COMPOUND_TYPE);
                List<ItemStack> itemStacks = new ArrayList<>();
                for (NbtElement itemNbtElement : itemStacksNbtList) {
                    NbtCompound itemNbt = (NbtCompound) itemNbtElement;
                    ItemStack stack = ItemStack.fromNbt(itemNbt); // NbtCompound를 ItemStack으로 변환
                    itemStacks.add(stack);
                }
                playerData.setItemStacks(itemStacks); // 복원된 아이템 스택 리스트를 playerData에 설정
            }

            // 레벨 정보 복원
            if (playerNbt.contains("playerLevel") && playerNbt.contains("playerExperience")) {
                int level = playerNbt.getInt("playerLevel");
                float experience = playerNbt.getFloat("playerExperience");
                playerData.setLevelInfo(new Pair(level, experience)); // 복원된 레벨 정보를 playerData에 설정
            }

            if (playerNbt.contains("deathInvenHistories")) {
                NbtList deathHistoriesNbt = playerNbt.getList("deathInvenHistories", 10); // 10은 NbtCompound 타입을 의미
                List<PlayerDeathInvenHistory> deathHistories = new ArrayList<>();

                for (NbtElement elem : deathHistoriesNbt) {
                    NbtCompound historyNbt = (NbtCompound) elem;

                    // 아이템 스택 복원
                    NbtList itemsNbt = historyNbt.getList("itemStacks", 10);
                    List<ItemStack> items = new ArrayList<>();
                    for (NbtElement itemElem : itemsNbt) {
                        items.add(ItemStack.fromNbt((NbtCompound) itemElem));
                    }

                    // 레벨 정보 복원
                    int level = historyNbt.getInt("level");
                    float experience = historyNbt.getFloat("experience");
                    Pair<Integer, Float> levelInfo = new Pair<>(level, experience);

                    // 사망 날짜 복원
                    LocalDateTime deathDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(historyNbt.getLong("deathDate")), ZoneId.systemDefault());

                    deathHistories.add(new PlayerDeathInvenHistory(items, levelInfo, deathDate));
                }

                playerData.setDeathInvenHistories(deathHistories);
            }


            UUID uuid = UUID.fromString(key);
            state.playerDataMap.put(uuid, playerData);
        });

        int[] codexItemIdList = tag.getIntArray("codexItemIdList");
        int[] codexItemCountList = tag.getIntArray("codexItemCountList");
        List<CodexInfo> serverCodexInfoList = new ArrayList<>();

        for (int i=0;i<codexItemIdList.length;i++) {
            serverCodexInfoList.add(new CodexInfo(codexItemIdList[i], codexItemCountList[i]));
        }

        if (serverCodexInfoList.size() == 0) {
            serverCodexInfoList = Arrays.stream(CodexSet.values())
                    .map(codexSet -> new CodexInfo(Item.getRawId(codexSet.getItem()), codexSet.getCount()))
                    .collect(Collectors.toList());
        }
        state.codexInfoList.clear();
        state.codexInfoList.addAll(serverCodexInfoList);

        return state;
    }


    public static ServerPlayerDataManager getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        ServerPlayerDataManager state = persistentStateManager.getOrCreate(nbt -> {
            // nbt가 있다면 nbt 정보를 불러옵니다.
            return createFromNbt(nbt);
        }, () -> {
            // 서버 정보가 없다면 기본 데이터를 선언합니다.
            return new ServerPlayerDataManager();
        }, KkoTycoon.MOD_ID);
        state.markDirty();
        return state;
    }

    public static KkotycoonPlayerData getPlayerData(LivingEntity player) {
        ServerPlayerDataManager serverState = getServerState(player.getWorld().getServer());
        return serverState.playerDataMap.computeIfAbsent(player.getUuid(), uuid -> new KkotycoonPlayerData(player.getName().getString()));
    }

    public static List<CodexInfo> getCodexList(MinecraftServer server) {
        ServerPlayerDataManager serverState = getServerState(server);
        return serverState.codexInfoList;
    }

    public static KkotycoonPlayerData resetPlayerData(LivingEntity player) {
        ServerPlayerDataManager serverState = getServerState(player.getWorld().getServer());
        // 데이터를 기본으로 초기화
        KkotycoonPlayerData kkotycoonPlayerData = new KkotycoonPlayerData(player.getName().getString());
        serverState.playerDataMap.put(player.getUuid(), kkotycoonPlayerData);
        return kkotycoonPlayerData;
    }
}
