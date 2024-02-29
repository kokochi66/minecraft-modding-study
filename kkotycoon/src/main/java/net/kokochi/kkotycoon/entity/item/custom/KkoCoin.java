package net.kokochi.kkotycoon.entity.item.custom;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kokochi.kkotycoon.KkoTycoon;
import net.kokochi.kkotycoon.entity.player.KkotycoonPlayerData;
import net.kokochi.kkotycoon.entity.player.ServerPlayerDataManager;
import net.kokochi.kkotycoon.packet.KkotycoonMainDataS2CGetPacket;
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

public class KkoCoin extends Item {
    public KkoCoin(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if (!world.isClient && hand == Hand.MAIN_HAND) {
            // 서버에서만 처리, 메인 손에 들려있는 경우에만 사용 처리
            ItemStack stackInHand = user.getStackInHand(hand);
            Text name = stackInHand.getName();
            // 아이템 이름으로부터 가격 정보만 가져옵니다.
            long coinAmount = Long.parseLong(name.getString().replaceAll("§6", "").replaceAll("꼬코인", "").replaceAll(",", ""));

            // 아이템을 사용하면 코인을 지급합니다.
            KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(user);
            user.getInventory().removeOne(stackInHand);
            playerData.setKkoCoin(playerData.getKkoCoin() + coinAmount);
            user.sendMessage(Text.of(NumberFormat.getInstance().format(coinAmount) + " 꼬코인을 획득하였습니다."));

            // 클라이언트로 돈 정보를 보내줍니다.
            PacketByteBuf responseBuf = new PacketByteBuf(Unpooled.buffer());
            KkotycoonMainDataS2CGetPacket.encode(new KkotycoonMainDataS2CGetPacket(playerData), responseBuf);
            Identifier responsePacketId = new Identifier(KkoTycoon.MOD_ID, KkotycoonMainDataS2CGetPacket.CODEX_GET_PACKET_RESPONSE_ID);
            ServerPlayNetworking.send((ServerPlayerEntity) user, responsePacketId, responseBuf);
        }

        return super.use(world, user, hand);
    }
}
