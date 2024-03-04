package net.kokochi.kkotycoon.mixin;

import com.google.common.collect.Multimap;
import net.kokochi.kkotycoon.server.handler.PlayerActionEventHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

import static net.kokochi.kkotycoon.server.handler.PlayerActionEventHandler.*;

@Mixin(ArrowEntity.class)
public class ArrowEntityMixin {

    @Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
    public void injectOnHit(LivingEntity target, CallbackInfo ci) {
        Entity shooter = ((ArrowEntity) (Object) this).getOwner();

        if (shooter instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) shooter;
            ItemStack mainHandItem = player.getMainHandStack();

            if (mainHandItem.hasNbt()) {
                NbtCompound nbt = mainHandItem.getNbt();
                if (nbt.contains(BOW_EFF_LEVEL_KEY)) {
                    int level = nbt.getInt(BOW_EFF_LEVEL_KEY);
                    double chance = nbt.getDouble(BOW_EFF_WEIGHT_KEY); // 퍼센트로 변환

                    // 메인 핸드 아이템의 공격 데미지 속성을 가져옵니다.
                    Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers = mainHandItem.getAttributeModifiers(EquipmentSlot.MAINHAND);
                    Collection<EntityAttributeModifier> attackDamageModifiers = attributeModifiers.get(EntityAttributes.GENERIC_ATTACK_DAMAGE);

                    // 공격 데미지 속성 수정자의 합계를 계산합니다.
                    int enchantLevel = EnchantmentHelper.getLevel(Enchantments.POWER, mainHandItem);
                    float baseDamage = (float)player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                    float powerDamage = enchantLevel > 0 ? enchantLevel * 0.5F + 0.5F : 0;

                    if (Math.random() < chance) {
                        float additionalDamage = (float) (baseDamage * powerDamage * (0.01d * (double)level));
                        player.sendMessage(Text.of("추가 데미지 적용 = " + additionalDamage));
                        DamageSource damageSource = player.getDamageSources().playerAttack(player);
                        target.damage(damageSource, additionalDamage);
                    }
                }


            }
        }
    }
}
