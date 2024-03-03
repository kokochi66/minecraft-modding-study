package net.kokochi.kkotycoon.entity.player;

import net.kokochi.kkotycoon.KkoTycoon;
import net.kokochi.kkotycoon.entity.codex.CodexSet;
import net.kokochi.kkotycoon.packet.KkotycoonMainDataS2CGetPacket;
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

import static java.util.stream.Collectors.*;

public class ServerPlayerDataManager extends PersistentState {
    public static HashMap<UUID, KkotycoonPlayerData> playerDataMap;
    public static List<Integer> codexItemIdList;

    public ServerPlayerDataManager() {
        playerDataMap = new HashMap<>();
        codexItemIdList = Arrays.stream(CodexSet.values())
                .map(codexSet -> Item.getRawId(codexSet.getItem()))
                .collect(Collectors.toList());
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound playersNbt = new NbtCompound();
        playerDataMap.forEach((uuid, playerData) -> {
            NbtCompound playerNbt = new NbtCompound();
            playerNbt.putByteArray("codexArray", playerData.getCodexArray());
            playerNbt.putLong("kkoCoin", playerData.getKkoCoin());
            nbt.putLong("lastReceivedCodexRewardDate", playerData.getLastReceivedCodexRewardDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

            List<LocalDateTime> codexLevelUpStack = playerData.getCodexLevelUpStack();
            long[] codexLevelUpStacksArray = new long[codexLevelUpStack.size()];
            for (int i=0;i<codexLevelUpStack.size();i++){
                codexLevelUpStacksArray[i] = codexLevelUpStack.get(i).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            }
            nbt.putLongArray("codexLevelUpStack", codexLevelUpStacksArray);

            playersNbt.put(uuid.toString(), playerNbt);
        });
        nbt.put("players", playersNbt);

        int[] codexArray = codexItemIdList.stream() // Stream<Integer> 생성
                .mapToInt(Integer::intValue) // IntStream 생성
                .toArray(); // int[]로 변환
        nbt.putIntArray("codexItemIdList", codexArray);
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

            UUID uuid = UUID.fromString(key);
            state.playerDataMap.put(uuid, playerData);
        });

        int[] codexIemIdArray = tag.getIntArray("codexItemIdList");
        if (ArrayUtils.isEmpty(codexIemIdArray)) {
            codexIemIdArray = Arrays.stream(CodexSet.values())
                    .mapToInt(codexSet -> Item.getRawId(codexSet.getItem()))
                    .toArray();
        }
        state.codexItemIdList.clear();
        state.codexItemIdList.addAll(Arrays.stream(codexIemIdArray).boxed().collect(Collectors.toList()));

        return state;
    }


    public static ServerPlayerDataManager getServerState(MinecraftServer server) {
        ServerWorld world = server.getWorld(World.OVERWORLD);
        String worldKey = world.getRegistryKey().getValue().toString();
        PersistentStateManager persistentStateManager = world.getPersistentStateManager();
        ServerPlayerDataManager state = persistentStateManager.getOrCreate(nbt -> {
            // nbt가 있다면 nbt 정보를 불러옵니다.
            return createFromNbt(nbt);
        }, () -> {
            // 서버 정보가 없다면 기본 데이터를 선언합니다.
            return new ServerPlayerDataManager();
        }, KkoTycoon.MOD_ID);
        HashMap<UUID, KkotycoonPlayerData> playerDataMap1 = state.playerDataMap;
        List<Integer> codexItemIdList1 = state.codexItemIdList;
        state.markDirty();
        return state;
    }

    public static KkotycoonPlayerData getPlayerData(LivingEntity player) {
        ServerPlayerDataManager serverState = getServerState(player.getWorld().getServer());
        return serverState.playerDataMap.computeIfAbsent(player.getUuid(), uuid -> new KkotycoonPlayerData());
    }

    public static List<Integer> getCodexList(MinecraftServer server) {
        ServerPlayerDataManager serverState = getServerState(server);
        return serverState.codexItemIdList;
    }

    public static KkotycoonPlayerData resetPlayerData(LivingEntity player) {
        ServerPlayerDataManager serverState = getServerState(player.getWorld().getServer());
        // 데이터를 기본으로 초기화
        KkotycoonPlayerData kkotycoonPlayerData = new KkotycoonPlayerData();
        serverState.playerDataMap.put(player.getUuid(), kkotycoonPlayerData);
        return kkotycoonPlayerData;
    }
}
