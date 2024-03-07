package net.kokochi.kkotycoon.mixin;


import net.kokochi.kkotycoon.entity.player.KkotycoonPlayerData;
import net.kokochi.kkotycoon.entity.player.ServerPlayerDataManager;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static net.kokochi.kkotycoon.server.handler.PlayerActionEventHandler.PICKAXE_EFF_LEVEL_KEY;
import static net.kokochi.kkotycoon.server.handler.PlayerActionEventHandler.PICKAXE_EFF_WEIGHT_KEY;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "afterBreak", at = @At("TAIL"), cancellable = true)
    private void injectAfterBreak(World world,
                                  PlayerEntity player,
                                  BlockPos pos,
                                  BlockState state,
                                  @Nullable BlockEntity blockEntity,
                                  ItemStack stack,
                                  CallbackInfo ci) {
        if (!world.isClient) {
            KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(player);

            // 광물 블럭 채굴 시
            if (state.getBlock().getLootTableId().getPath().endsWith("_ore")) {
                ItemStack mainHandStack = player.getMainHandStack();
                if (mainHandStack.hasNbt()) {
                    NbtCompound nbt = mainHandStack.getNbt();
                    if (nbt.contains(PICKAXE_EFF_LEVEL_KEY)) {
                        // 인챈트 정보를 가져와서 섬세한 손길이 있는 경우에는 효과가 발동되지 않도록 예외처리
                        Map<Enchantment, Integer> enchantmentIntegerMap = EnchantmentHelper.get(mainHandStack);
                        if (!enchantmentIntegerMap.containsKey(Enchantments.SILK_TOUCH)) {
                            int level = nbt.getInt(PICKAXE_EFF_LEVEL_KEY);
                            double chance = nbt.getDouble(PICKAXE_EFF_WEIGHT_KEY); // 퍼센트로 변환
                            if (Math.random() < chance) {
//                                player.sendMessage(Text.of("아이템 추가 드랍"));
                                ItemStack itemStack = state.getBlock().getDroppedStacks(state, (ServerWorld) world, pos, blockEntity)
                                        .stream().findFirst().orElse(null);
                                if (itemStack != null) {
                                    itemStack.setCount(level);
                                    Block.dropStack(world, pos, itemStack);
                                }
                            }
                        }
                    }
                }


                playerData.setAccumulatedBreakOreBlock(playerData.getAccumulatedBreakOreBlock() + 1);
            } else if (state.getBlock() instanceof CropBlock || state.getBlock() instanceof StemBlock || state.getBlock() instanceof AttachedStemBlock) {
                playerData.setAccumulatedBreakCropBlock(playerData.getAccumulatedBreakCropBlock() + 1);
            }
            playerData.setAccumulatedBlock(playerData.getAccumulatedBlock() + 1);
        }
    }
    @Inject(method = "onPlaced", at = @At("TAIL"), cancellable = true)
    private void injectOnPlaced(World world,
                                BlockPos pos,
                                BlockState state,
                                @Nullable LivingEntity placer,
                                ItemStack itemStack,
                                CallbackInfo ci) {
        if (!world.isClient && placer instanceof ServerPlayerEntity) {
            KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(placer);
            playerData.setAccumulatedOnBlock(playerData.getAccumulatedOnBlock() + 1);
        }
    }

}
