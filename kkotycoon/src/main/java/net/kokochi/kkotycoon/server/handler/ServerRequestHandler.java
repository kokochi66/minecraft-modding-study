package net.kokochi.kkotycoon.server.handler;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kokochi.kkotycoon.entity.codex.CodexInfo;
import net.kokochi.kkotycoon.entity.player.KkotycoonPlayerData;
import net.kokochi.kkotycoon.entity.player.ServerPlayerDataManager;
import net.kokochi.kkotycoon.entity.product.KkoShopProduct;
import net.kokochi.kkotycoon.entity.product.KkoShopProductDataManager;
import net.kokochi.kkotycoon.packet.CodexC2SPostPacket;
import net.kokochi.kkotycoon.packet.KkotycoonMainDataS2CGetPacket;
import net.kokochi.kkotycoon.packet.ShopScreenPurchaseC2SPacket;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;

import static net.kokochi.kkotycoon.KkoTycoon.MOD_ID;

public class ServerRequestHandler {

    // 기본 공통 데이터 핸들러
    public static void initDataRequestHandler() {
        // 게임 실행 될 때의 서버측 로직
        ServerPlayNetworking.registerGlobalReceiver(
                new Identifier(MOD_ID, KkotycoonMainDataS2CGetPacket.CODEX_GET_PACKET_REQUEST_ID), (server, player, handler, buf, responseSender) ->
                {
                    // 기본 사용자 데이터를 내려줍니다.
                    PacketByteBuf responseBuf = new PacketByteBuf(Unpooled.buffer());
                    KkotycoonMainDataS2CGetPacket kkotycoonMainDataS2CGetPacket = new KkotycoonMainDataS2CGetPacket(ServerPlayerDataManager.getPlayerData(player));
                    KkotycoonMainDataS2CGetPacket.encode(kkotycoonMainDataS2CGetPacket, responseBuf);
                    Identifier responsePacketId = new Identifier(MOD_ID, KkotycoonMainDataS2CGetPacket.CODEX_GET_PACKET_RESPONSE_ID);
                    ServerPlayNetworking.send(player, responsePacketId, responseBuf);
                });
    }

    // 도감 데이터 핸들러
    public static void initCodexRequestHandler() {
        // 도감에 아이템 등록
        ServerPlayNetworking.registerGlobalReceiver(
                new Identifier(MOD_ID, CodexC2SPostPacket.CODEX_POST_PACKET_REQUEST_ID), (server, player, handler, buf, responseSender) ->
                {
                    // 패킷을 가져와서 플레이어의 코덱 정보에 저장
                    int codexItemId = CodexC2SPostPacket.decode(buf);
                    List<CodexInfo> codexList = ServerPlayerDataManager.getCodexList(server);
                    int codexIndex = -1;
                    for (int i=0;i<codexList.size();i++) {
                        if (codexList.get(i).getItemId() == codexItemId) {
                            codexIndex = i;
                        }
                    }

                    // 해당 유저가 도감에 해당하는 아이템을 보유하고 있는지를 체크합니다.
                    Item item = Item.byRawId(codexItemId);
                    int itemCount = player.getInventory().count(item);
                    int needCount = codexList.get(codexIndex).getCount();
                    if (itemCount < needCount) {
                        // 아이템을 개수만큼 보유하고 있지 않다면 도감 등록 처리를 하지 않습니다.
                        player.sendMessage(Text.of("아이템 개수가 부족합니다."));
                        return;
                    }

                    KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(player);
                    byte[] nbtArray = playerData.getCodexArray();
                    if (nbtArray.length == 0) {
                        // 저장된 데이터가 없다면 새로 생성해줍니다.
                        nbtArray = new byte[codexList.size()];
                        nbtArray[codexIndex] = 1;
                    } else if (nbtArray[codexIndex] == 1) {
                        // 이미 도감에 등록되었다면 등록 처리를 하지 않습니다.
                        player.sendMessage(Text.of("이미 도감에 등록된 아이템 입니다."));
                        return;
                    } else {
                        // 이미 있다면 도감에 데이터를 채워줍니다.
                        nbtArray[codexIndex] = 1;
                        // 도감 보상 스택도 추가해줍니다.
                        playerData.getCodexLevelUpStack().add(LocalDateTime.now());
                        // 아이템을 소모처리 해줍니다.
                        player.getInventory().remove(itemStack -> Item.getRawId(itemStack.getItem()) == Item.getRawId(item), needCount, player.getInventory());
                    }

                    // 클라이언트에 응답 데이터를 내려줍니다.
                    PacketByteBuf responseBuf = new PacketByteBuf(Unpooled.buffer());
                    KkotycoonMainDataS2CGetPacket.encode(new KkotycoonMainDataS2CGetPacket(playerData), responseBuf);
                    Identifier responsePacketId = new Identifier(MOD_ID, KkotycoonMainDataS2CGetPacket.CODEX_GET_PACKET_RESPONSE_ID);
                    ServerPlayNetworking.send(player, responsePacketId, responseBuf);
                    player.sendMessage(Text.of("\"§9" + item.getName().getString()+ "§f\" 아이템이 도감에 등록되었습니다!"));
                });


        // 도감 보상 수령
        ServerPlayNetworking.registerGlobalReceiver(
                new Identifier(MOD_ID, CodexC2SPostPacket.CODEX_POST_REWARD_REQUEST_ID), (server, player, handler, buf, responseSender) ->
                {
                    // 해당 플레이어의 보상 정보를 가져와서 지급합니다.
                    KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(player);
                    // 남은 시간을 계산하여 보상 수령 일자를 초기화해줍니다.
                    LocalDateTime now = LocalDateTime.now();
                    long betweenMinute = KkotycoonPlayerData.between(playerData.getLastReceivedCodexRewardDate(), now);
                    if (betweenMinute <= 0) {
                        player.sendMessage(Text.of("수령할 보상이 없습니다. 잠시만 기다려 주세요."));
                        return;
                    }
                    long codexReward = playerData.calculCodexReward();
                    playerData.setLastReceivedCodexRewardDate(playerData.getLastReceivedCodexRewardDate().plusMinutes(betweenMinute));
                    // codexStack을 초기화합니다.
                    playerData.getCodexLevelUpStack().clear();
                    playerData.setKkoCoin(playerData.getKkoCoin() + codexReward);

                    // 클라이언트에 응답 데이터를 내려줍니다.
                    PacketByteBuf responseBuf = new PacketByteBuf(Unpooled.buffer());
                    KkotycoonMainDataS2CGetPacket.encode(new KkotycoonMainDataS2CGetPacket(playerData), responseBuf);
                    Identifier responsePacketId = new Identifier(MOD_ID, KkotycoonMainDataS2CGetPacket.CODEX_GET_PACKET_RESPONSE_ID);
                    ServerPlayNetworking.send(player, responsePacketId, responseBuf);
                    player.sendMessage(Text.of("\"§6" + NumberFormat.getInstance().format(codexReward)+ "§f\" 만큼의 §a도감 보상§f을 획득하였습니다!"));
                });
    }

    public static void initShopPurchaseRequestHandler() {
        // 상점에서 상품 구매 요청
        ServerPlayNetworking.registerGlobalReceiver(
                new Identifier(MOD_ID, ShopScreenPurchaseC2SPacket.SHOP_PURCHASE_SCREEN_REQUEST_ID), (server, player, handler, buf, responseSender) ->
                {
                    // 구매하고자 한 상품 정보를 가져옵니다.
                    long productId = ShopScreenPurchaseC2SPacket.decode(buf);
                    KkoShopProduct kkoShopProduct = KkoShopProductDataManager.PRODUCT_LIST.stream().filter(p -> p.getProductId() == productId)
                            .findFirst().orElse(null);
                    if (kkoShopProduct == null) {
                        player.sendMessage(Text.of("등록되지 않은 상품입니다. 관리자에게 문의하세요."));
                        return;
                    }

                    KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(player);
                    // 금액보다 보유 코인이 부족한 경우 구매가 불가능합니다.
                    if (kkoShopProduct.getPrice() > playerData.getKkoCoin()) {
                        player.sendMessage(Text.of("꼬코인이 부족합니다."));
                        return;
                    }

                    int emptySlot = player.getInventory().getEmptySlot();
                    if (emptySlot == -1) {
                        player.sendMessage(Text.of("인벤토리 창이 꽉 차있습니다."));
                        return;
                    }

                    playerData.setKkoCoin(playerData.getKkoCoin() - kkoShopProduct.getPrice());
                    playerData.setLastPurchaseProductDate(LocalDateTime.now());
                    ItemStack purchaseItemStack = new ItemStack(kkoShopProduct.getItem());
                    purchaseItemStack.setCount(1);
                    player.getInventory().insertStack(purchaseItemStack);

                    // 클라이언트에 응답 데이터를 내려줍니다.
                    PacketByteBuf responseBuf = new PacketByteBuf(Unpooled.buffer());
                    KkotycoonMainDataS2CGetPacket.encode(new KkotycoonMainDataS2CGetPacket(playerData), responseBuf);
                    Identifier responsePacketId = new Identifier(MOD_ID, KkotycoonMainDataS2CGetPacket.CODEX_GET_PACKET_RESPONSE_ID);
                    ServerPlayNetworking.send(player, responsePacketId, responseBuf);
                    player.sendMessage(Text.of("\"§6" +kkoShopProduct.getItem().getName().getString()+ "§f\" 아이템을 구매하였습니다."));
                });
    }
}
