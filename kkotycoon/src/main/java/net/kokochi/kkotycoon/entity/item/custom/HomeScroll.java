package net.kokochi.kkotycoon.entity.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// 마을 귀환서
public class HomeScroll extends Item {
    public HomeScroll(Settings settings) {
        super(settings);
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.of("§9스폰 지역으로 즉시 이동합니다."));
        tooltip.add(Text.of("§c사용방법"));
        tooltip.add(Text.of("§f마우스 우클릭을 누릅니다."));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient && user instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) user;

            // 오버월드로 이동하도록 설정
            ServerWorld overworld = serverPlayer.getServer().getWorld(World.OVERWORLD);
            BlockPos spawnPoint = serverPlayer.getSpawnPointPosition();

            // 플레이어를 오버월드의 스폰 지점으로 순간 이동시킵니다.
            serverPlayer.teleport(
                    overworld,
                    spawnPoint.getX() + 0.5, // 중앙으로 이동하기 위해 0.5를 더함
                    spawnPoint.getY(),
                    spawnPoint.getZ() + 0.5, // 중앙으로 이동하기 위해 0.5를 더함
                    serverPlayer.getYaw(),
                    serverPlayer.getPitch()
            );

            // 소리 효과를 재생합니다.
            overworld.playSound(null,
                    serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(),
                    SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS,
                    1.0f, 1.0f);

            // 아이템을 사용하고 나면, 아이템을 하나 소모합니다.
            if (!serverPlayer.getAbilities().creativeMode) {
                user.getStackInHand(hand).decrement(1);
            }

            return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
        }

        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
