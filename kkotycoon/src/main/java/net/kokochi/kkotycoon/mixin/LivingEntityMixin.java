package net.kokochi.kkotycoon.mixin;


import net.kokochi.kkotycoon.entity.player.KkotycoonPlayerData;
import net.kokochi.kkotycoon.entity.player.ServerPlayerDataManager;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.kokochi.kkotycoon.server.handler.PlayerActionEventHandler.PICKAXE_EFF_LEVEL_KEY;
import static net.kokochi.kkotycoon.server.handler.PlayerActionEventHandler.PICKAXE_EFF_WEIGHT_KEY;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "onDeath", at = @At("TAIL"), cancellable = true)
    private void injectOnDeath(DamageSource damageSource,
                                  CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (!livingEntity.getWorld().isClient) {
            Entity attacker = damageSource.getAttacker();
            if (attacker instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) attacker;
                KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(player);
                if (livingEntity instanceof Monster) {
                    playerData.setAccumulatedKilledMonster(playerData.getAccumulatedKilledMonster() + 1);
                } else if (livingEntity instanceof AnimalEntity) {
                    playerData.setAccumulatedKilledAnimal(playerData.getAccumulatedKilledAnimal() + 1);
                }
            }
        }
    }

    @Inject(method = "damage", at = @At("TAIL"), cancellable = true)
    private void injectOnDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (!livingEntity.getWorld().isClient) {
            if ((Object)this instanceof ServerPlayerEntity) {
                PlayerEntity player = (ServerPlayerEntity)(Object)this;
                KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(player);
                playerData.setAccumulatedDamaged(playerData.getAccumulatedDamaged() + amount);
            } else {
                Entity attacker = source.getAttacker();
                if (attacker instanceof ServerPlayerEntity) {
                    ServerPlayerEntity player = (ServerPlayerEntity) attacker;
                    KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(player);
                    playerData.setAccumulatedAttack(playerData.getAccumulatedAttack() + amount);
                }
            }
        }
    }

}
