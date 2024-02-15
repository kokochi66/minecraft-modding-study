package net.kokochi.tutorialmod.networking.packet;

import net.kokochi.tutorialmod.client.ClientThirstData;
import net.kokochi.tutorialmod.thirst.PlayerThirstProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ThirstDataSyncC2SPacket {
    private final int thirst;

    public ThirstDataSyncC2SPacket(int thirst) {
        this.thirst = thirst;
    }

    public ThirstDataSyncC2SPacket(FriendlyByteBuf buf) {
        this.thirst = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(thirst);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // 현재 목마름 상태를 Gui에 반영해준다.
            ClientThirstData.set(thirst);
        });
        return true;
    }

}
