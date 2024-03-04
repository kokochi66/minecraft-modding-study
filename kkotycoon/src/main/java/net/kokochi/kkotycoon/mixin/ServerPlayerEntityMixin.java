package net.kokochi.kkotycoon.mixin;


import net.kokochi.kkotycoon.entity.player.KkotycoonPlayerData;
import net.kokochi.kkotycoon.entity.player.ServerPlayerDataManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @Inject(method = "onDisconnect", at = @At("TAIL"), cancellable = true)
    private void injectOnDisconnect(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (!player.getWorld().isClient) {
            KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(player);
            long between = ChronoUnit.SECONDS.between(playerData.getLoginDate() == null ? LocalDateTime.now() : playerData.getLoginDate(), LocalDateTime.now());
            playerData.setAccumulatedPlayTime(playerData.getAccumulatedPlayTime() + between);
        }
    }

    @Inject(method = "onSpawn", at = @At("TAIL"), cancellable = true)
    private void injectOnSpawn(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (!player.getWorld().isClient) {
            KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(player);
            playerData.setLoginDate(LocalDateTime.now());
        }
    }

}
