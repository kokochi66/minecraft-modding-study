package net.kokochi.kkotycoon.mixin;


import net.kokochi.kkotycoon.entity.item.KkoTycoonItems;
import net.kokochi.kkotycoon.entity.player.KkotycoonPlayerData;
import net.kokochi.kkotycoon.entity.player.ServerPlayerDataManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @Shadow public abstract void playerTick();

    @Shadow public abstract void increaseStat(Stat<?> stat, int amount);

    @Inject(method = "onDisconnect", at = @At("TAIL"), cancellable = true)
    private void injectOnDisconnect(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (!player.getWorld().isClient) {
            KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(player);
            long between = ChronoUnit.SECONDS.between(
                    playerData.getLoginDate() == null ? LocalDateTime.now() : playerData.getLoginDate(), LocalDateTime.now());
            playerData.setAccumulatedPlayTime(playerData.getAccumulatedPlayTime() + between);
        }
    }

    @Inject(method = "onSpawn", at = @At("HEAD"), cancellable = true)
    private void injectOnSpawn(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (!player.getWorld().isClient) {

            KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(player);

            // 아이템 스택 복원
            List<ItemStack> itemStacks = playerData.getItemStacks();
            if (itemStacks != null && !itemStacks.isEmpty()) {
                PlayerInventory inventory = player.getInventory();
                inventory.clear(); // 기존 인벤토리를 클리어
                for (int i = 0; i < itemStacks.size(); i++) {
                    ItemStack stack = itemStacks.get(i);
                    if (i >= 0 && i <= 3) { // Armor slots are usually 0-3, from feet to head
                        inventory.armor.set(i, stack); // Reverse armor order to match inventory order
                    } else if (i == 4) { // Off-hand slot
                        inventory.offHand.set(0, stack); // Off-hand slot is a single slot, usually at index 0
                    } else {
                        // Main inventory slots start after armor and off-hand, so adjust the index accordingly
                        int mainInventoryIndex = i - inventory.armor.size() - inventory.offHand.size();
                        if (mainInventoryIndex < inventory.main.size()) {
                            inventory.main.set(mainInventoryIndex, stack); // Set stack in main inventory
                        }
                    }
                }
                playerData.setItemStacks(null); // 복원 후 데이터 삭제
            }

            // 레벨과 경험치 복원
            Pair<Integer, Float> levelInfo = playerData.getLevelInfo();
            if (levelInfo != null) {
                player.experienceLevel = levelInfo.getLeft(); // 플레이어 레벨 설정
                player.experienceProgress = levelInfo.getRight(); // 경험치 설정
                playerData.setLevelInfo(null); // 복원 후 데이터 삭제
                player.sendMessage(Text.of("인벤토리 세이브권을 사용하여 데이터가 복원되었습니다."));
            }

            playerData.setLoginDate(LocalDateTime.now());
        }
    }

    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    private void injectOnDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (!player.getWorld().isClient) {
            KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(player);
            PlayerInventory inventory = player.getInventory();

            // 플레이어 인벤토리 안에 인벤토리 세이브권이 있는지 확인합니다.
            boolean contains = inventory.contains(new ItemStack(KkoTycoonItems.INVENTORY_SAVE_TICKET));
            if (contains) {
                // 인벤토리 세이브권이 있다면 한 장 소모합니다.
                boolean inventorySaveTicketMinus = false;
                List<ItemStack> items = new ArrayList<>();
                for (ItemStack stack : inventory.armor) {
                    items.add(stack.copy());
                }

                for (ItemStack stack : inventory.offHand) {
                    if (Item.getRawId(stack.getItem()) == Item.getRawId(KkoTycoonItems.INVENTORY_SAVE_TICKET)
                            && !inventorySaveTicketMinus) {
                        stack.setCount(stack.getCount() - 1);
                        if (stack.getCount() == 0) {
                            stack = new ItemStack(Items.AIR);
                        }
                    }
                    items.add(stack.copy());
                }

                for (int i = 0; i < inventory.size(); ++i) {
                    ItemStack stack = inventory.getStack(i);
                    if (Item.getRawId(stack.getItem()) == Item.getRawId(KkoTycoonItems.INVENTORY_SAVE_TICKET)
                            && !inventorySaveTicketMinus) {
                        stack.setCount(stack.getCount() - 1);
                        if (stack.getCount() == 0) {
                            stack = new ItemStack(Items.AIR);
                        }
                    }
                    items.add(stack.copy()); // 아이템 복사를 통해 원본 데이터를 보존합니다.
                }

                // 플레이어의 현재 레벨과 경험치를 가져옵니다.
                int playerLevel = player.experienceLevel;
                float playerExperience = player.experienceProgress;

                playerData.setItemStacks(items);
                playerData.setLevelInfo(new Pair<>(playerLevel, playerExperience));
            }


            playerData.setAccumulatedDeathCount(playerData.getAccumulatedDeathCount() + 1);
        }
    }
}
