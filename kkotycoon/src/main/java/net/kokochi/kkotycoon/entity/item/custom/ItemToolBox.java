package net.kokochi.kkotycoon.entity.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

// 아이템 수리 키트
public class ItemToolBox extends Item {
    public ItemToolBox(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.of("§9무조건 모든 아이템의 내구도를 100%로 수리해줍니다."));
        tooltip.add(Text.of("§c사용방법 (필독)"));
        tooltip.add(Text.of("§6오른손 §f: 아이템 수리 키트를 듭니다."));
        tooltip.add(Text.of("§6왼손 §f: 수리하고자 하는 장비를 듭니다."));
        tooltip.add(Text.of("§f마우스 우클릭을 누릅니다."));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            // 오른손에 아이템을 들고 있다면
            if (hand == Hand.MAIN_HAND) {
                ItemStack itemInOffhand = user.getOffHandStack(); // 왼손에 들고 있는 아이템
                if (!itemInOffhand.isEmpty() && itemInOffhand.isDamageable()) {
                    if (itemInOffhand.getDamage() > 0) { // 내구도가 닳았다면
                        itemInOffhand.setDamage(0); // 내구도를 완전히 복구
                        user.sendMessage(Text.literal("아이템이 수리되었습니다."), true);
                        user.getStackInHand(hand).decrement(1); // 수리 키트를 하나 사용
                        return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
                    } else {
                        user.sendMessage(Text.literal("이 아이템은 수리가 필요하지 않습니다."), true);
                        return TypedActionResult.fail(user.getStackInHand(hand));
                    }
                }
            }
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
