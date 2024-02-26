package net.kokochi.kkotycoon.entity;

import com.mojang.authlib.GameProfile;
import net.kokochi.kkotycoon.client.packet.CodexS2CGetPacket;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class KkotycoonPlayer extends ServerPlayerEntity {

    private byte[] codexArray;
    public KkotycoonPlayer(MinecraftServer server, ServerWorld world, GameProfile profile) {
        super(server, world, profile);
    }


    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.codexArray = nbt.getByteArray(CodexS2CGetPacket.CODEX_LIST_NBT_KEY);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound nbtCompound = super.writeNbt(nbt);
        nbtCompound.putByteArray(CodexS2CGetPacket.CODEX_LIST_NBT_KEY, this.codexArray);
        return nbtCompound;
    }
}
