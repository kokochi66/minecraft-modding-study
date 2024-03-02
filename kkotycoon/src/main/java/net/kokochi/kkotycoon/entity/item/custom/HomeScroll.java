package net.kokochi.kkotycoon.entity.item.custom;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kokochi.kkotycoon.KkoTycoon;
import net.kokochi.kkotycoon.entity.player.KkotycoonPlayerData;
import net.kokochi.kkotycoon.entity.player.ServerPlayerDataManager;
import net.kokochi.kkotycoon.packet.KkotycoonMainDataS2CGetPacket;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.text.NumberFormat;
import java.util.Optional;

// 마을 귀환서
public class HomeScroll extends Item {
    public HomeScroll(Settings settings) {
        super(settings);
    }


    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        // 툴팁 추가
        // 우클릭 시 스폰 지역으로 이동됩니다.
        return super.getTooltipData(stack);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // 스폰 지역으로 유저를 이동시키도록 설정
        return super.use(world, user, hand);
    }
}
