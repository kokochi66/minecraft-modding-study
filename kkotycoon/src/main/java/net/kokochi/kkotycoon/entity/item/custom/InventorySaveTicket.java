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

// 인벤토리 세이브권
public class InventorySaveTicket extends Item {
    public InventorySaveTicket(Settings settings) {
        super(settings);
    }
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.of("§c사용방법"));
        tooltip.add(Text.of("§f인벤토리에 갖고만 있어도 인벤토리가 보호됩니다."));
        tooltip.add(Text.of("§c주의: 죽고나서 바로 리스폰을 누르지 않고 나갈 경우 인벤토리 보호가 되지 않고 모든 아이템이 사라집니다."));
    }
}
