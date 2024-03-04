package net.kokochi.kkotycoon.entity.player;

import net.kokochi.kkotycoon.KkoTycoon;
import net.kokochi.kkotycoon.entity.codex.CodexInfo;
import net.kokochi.kkotycoon.entity.codex.CodexSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
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
            playerNbt.putLong("lastReceivedCodexRewardDate", playerData.getLastReceivedCodexRewardDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

            List<LocalDateTime> codexLevelUpStack = playerData.getCodexLevelUpStack();
            long[] codexLevelUpStacksArray = new long[codexLevelUpStack.size()];
            for (int i=0;i<codexLevelUpStack.size();i++){
                codexLevelUpStacksArray[i] = codexLevelUpStack.get(i).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }
            playerNbt.putLongArray("codexLevelUpStack", codexLevelUpStacksArray);
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
        return serverState.playerDataMap.computeIfAbsent(player.getUuid(), uuid -> new KkotycoonPlayerData());
    }

    public static List<CodexInfo> getCodexList(MinecraftServer server) {
        ServerPlayerDataManager serverState = getServerState(server);
        return serverState.codexInfoList;
    }

    public static KkotycoonPlayerData resetPlayerData(LivingEntity player) {
        ServerPlayerDataManager serverState = getServerState(player.getWorld().getServer());
        // 데이터를 기본으로 초기화
        KkotycoonPlayerData kkotycoonPlayerData = new KkotycoonPlayerData();
        serverState.playerDataMap.put(player.getUuid(), kkotycoonPlayerData);
        return kkotycoonPlayerData;
    }
}
