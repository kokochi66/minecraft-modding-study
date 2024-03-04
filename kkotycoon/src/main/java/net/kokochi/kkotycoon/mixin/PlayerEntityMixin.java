package net.kokochi.kkotycoon.mixin;


import com.google.common.collect.Multimap;
import net.kokochi.kkotycoon.entity.player.KkotycoonPlayerData;
import net.kokochi.kkotycoon.entity.player.ServerPlayerDataManager;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

import static net.kokochi.kkotycoon.server.handler.PlayerActionEventHandler.*;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "attack", at = @At("TAIL"), cancellable = true)
    private void injectAttack(Entity target,
                              CallbackInfo ci) {
        if (target instanceof LivingEntity) {
            // 여기에 사용자 정의 추가 데미지 로직을 구현합니다.
            // 예: 플레이어의 메인 핸드 아이템이 특정 조건을 만족하는 경우 추가 데미지 적용
            LivingEntity livingEntity = (LivingEntity) target;
            PlayerEntity player = (PlayerEntity)(Object)this;

            if (!player.getWorld().isClient()) {
                ItemStack mainHandItem = player.getMainHandStack();

                if (mainHandItem.hasNbt()) {
                    NbtCompound nbt = mainHandItem.getNbt();
                    if (nbt.contains(SWORD_EFF_LEVEL_KEY)) {
                        int level = nbt.getInt(SWORD_EFF_LEVEL_KEY);
                        double chance = nbt.getDouble(SWORD_EFF_WEIGHT_KEY); // 퍼센트로 변환

                        // 메인 핸드 아이템의 공격 데미지 속성을 가져옵니다.
                        Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers = mainHandItem.getAttributeModifiers(EquipmentSlot.MAINHAND);
                        Collection<EntityAttributeModifier> attackDamageModifiers = attributeModifiers.get(EntityAttributes.GENERIC_ATTACK_DAMAGE);

                        // 공격 데미지 속성 수정자의 합계를 계산합니다.
                        double attackDamage = 0.0;
                        for (EntityAttributeModifier modifier : attackDamageModifiers) {
                            if (modifier.getOperation() == EntityAttributeModifier.Operation.ADDITION) {
                                attackDamage += modifier.getValue();
                            }
                        }

                        if (Math.random() < chance) {
                            player.sendMessage(Text.of("추가 데미지 적용"));
                            float additionalDamage = (float) (attackDamage * (0.01d * (double)level));
                            DamageSource damageSource = player.getDamageSources().playerAttack(player);
                            livingEntity.damage(damageSource, additionalDamage);
                        }
                    }
                }


            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"), cancellable = true)
    private void injectTick(CallbackInfo ci) {
        PlayerEntity playerEntity = (PlayerEntity) (Object) this;
        // 사용자의 이동거리 저장
        if (!playerEntity.getWorld().isClient) {
            KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(playerEntity);
            if (playerData.getPreviousPosition() == null) {
                playerData.setPreviousPosition(playerEntity.getPos());
            } else {
                Vec3d currentPosition = playerEntity.getPos();
                double distanceMoved = playerData.getPreviousPosition().distanceTo(currentPosition);
                if (distanceMoved > 0) {

                    playerData.setAccumulatedDistance(playerData.getAccumulatedDistance() + distanceMoved);
                }
                playerData.setPreviousPosition(currentPosition);
            }
        }
    }
}
