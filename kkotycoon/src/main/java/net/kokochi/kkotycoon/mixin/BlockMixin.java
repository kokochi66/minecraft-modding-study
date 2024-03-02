package net.kokochi.kkotycoon.mixin;


import net.kokochi.kkotycoon.server.handler.PlayerActionEventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
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

import java.awt.image.PixelGrabber;
import java.util.List;

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

            // 광물 블럭 채굴 시
            if (state.isIn(TagKey.of(Registries.BLOCK.getKey(), new Identifier("minecraft", "mineable/pickaxe")))) {
                ItemStack mainHandStack = player.getMainHandStack();
                if (mainHandStack.hasNbt()) {
                    NbtCompound nbt = mainHandStack.getNbt();
                    if (nbt.contains(PICKAXE_EFF_LEVEL_KEY)) {
                        int level = nbt.getInt(PICKAXE_EFF_LEVEL_KEY);
                        double chance = nbt.getDouble(PICKAXE_EFF_WEIGHT_KEY); // 퍼센트로 변환
                        if (Math.random() < chance) {
                            player.sendMessage(Text.of("아이템 추가 드랍"));
                            ItemStack itemStack = state.getBlock().getDroppedStacks(state, (ServerWorld) world, pos, blockEntity)
                                    .stream().findFirst().orElse(null);
                            if (itemStack != null) {
                                itemStack.setCount(level);
                                Block.dropStack(world, pos, itemStack);
                            }
                        }
                    }
                }
            } else if (state.isIn(TagKey.of(Registries.BLOCK.getKey(), new Identifier("minecraft", "crops")))) {
                ItemStack mainHandStack = player.getMainHandStack();
                if (mainHandStack.hasNbt()) {
                    NbtCompound nbt = mainHandStack.getNbt();
                    if (nbt.contains(PICKAXE_EFF_LEVEL_KEY)) {
                        int level = nbt.getInt(PICKAXE_EFF_LEVEL_KEY);
                        double chance = nbt.getDouble(PICKAXE_EFF_WEIGHT_KEY); // 퍼센트로 변환
                        if (Math.random() < chance) {
                            player.sendMessage(Text.of("아이템 추가 드랍"));
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

        }
    }
}
