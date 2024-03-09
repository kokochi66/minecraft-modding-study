package net.kokochi.kkotycoon.entity.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// 창고 정리 도구
public class StorageArrangeTool extends Item {
    public StorageArrangeTool(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

//        tooltip.add(Text.of("§9스폰 지역으로 즉시 이동합니다."));
//        tooltip.add(Text.of("§c사용방법"));
//        tooltip.add(Text.of("§f마우스 우클릭을 누릅니다."));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient && user instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) user;

            return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
        }

        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
