package net.kokochi.kkotycoon.entity.player;

import net.kokochi.kkotycoon.KkoTycoon;
import net.kokochi.kkotycoon.packet.KkotycoonMainDataS2CGetPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

public class ServerPlayerDataManager extends PersistentState {
    public static final HashMap<UUID, KkotycoonPlayerData> playerDataMap = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound playersNbt = new NbtCompound();
        playerDataMap.forEach((uuid, playerData) -> {
            NbtCompound playerNbt = new NbtCompound();
            playerNbt.putByteArray(KkotycoonMainDataS2CGetPacket.CODEX_LIST_NBT_KEY, playerData.getCodexArray());
            playersNbt.put(uuid.toString(), playerNbt);
        });
        nbt.put("players", playersNbt);
        return nbt;
    }


    public static ServerPlayerDataManager createFromNbt(NbtCompound tag) {
        ServerPlayerDataManager state = new ServerPlayerDataManager();

        NbtCompound playersNbt = tag.getCompound("players");
        playersNbt.getKeys().forEach(key -> {
            KkotycoonPlayerData playerData = new KkotycoonPlayerData();

            byte[] byteArray = playersNbt.getCompound(key).getByteArray(KkotycoonMainDataS2CGetPacket.CODEX_LIST_NBT_KEY);
            playerData.setCodexArray(byteArray);

            UUID uuid = UUID.fromString(key);
            state.playerDataMap.put(uuid, playerData);
        });

        return state;
    }


    public static ServerPlayerDataManager getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        ServerPlayerDataManager state = persistentStateManager.getOrCreate(ServerPlayerDataManager::createFromNbt, ServerPlayerDataManager::new, KkoTycoon.MOD_ID);
        state.markDirty();
        return state;
    }

    public static KkotycoonPlayerData getPlayerData(LivingEntity player) {
        ServerPlayerDataManager serverState = getServerState(player.getWorld().getServer());
        return serverState.playerDataMap.computeIfAbsent(player.getUuid(), uuid -> new KkotycoonPlayerData());
    }

    public static KkotycoonPlayerData resetPlayerData(LivingEntity player) {
        ServerPlayerDataManager serverState = getServerState(player.getWorld().getServer());
        // 데이터를 기본으로 초기화
        KkotycoonPlayerData kkotycoonPlayerData = new KkotycoonPlayerData();
        serverState.playerDataMap.put(player.getUuid(), kkotycoonPlayerData);
        return kkotycoonPlayerData;
    }
}
