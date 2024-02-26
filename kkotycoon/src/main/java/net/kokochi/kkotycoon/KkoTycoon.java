package net.kokochi.kkotycoon;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kokochi.kkotycoon.client.data.CodexSet;
import net.kokochi.kkotycoon.client.packet.CodexS2CGetPacket;
import net.kokochi.kkotycoon.client.packet.CodexC2SPostPacket;
import net.kokochi.kkotycoon.entity.KkotycoonPlayer;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class KkoTycoon implements ModInitializer {
	public static final String MOD_ID = "kkotycoon";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// 게임 실행 될 때의 서버측 로직
		ServerPlayNetworking.registerGlobalReceiver(
				new Identifier(MOD_ID, CodexS2CGetPacket.CODEX_GET_PACKET_REQUEST_ID), (server, player, handler, buf, responseSender) ->
				{
					// 사용자의 최초 도감 정보 조회
					LOGGER.info(player.getUuid() + " codex get");

					// nbt에서 사용자에게 저장되어있는 도감 정보를 조회합니다.
					NbtCompound nbtCompound = new NbtCompound();
					player.writeNbt(nbtCompound);
					byte[] nbtArray = nbtCompound.getByteArray(CodexS2CGetPacket.CODEX_LIST_NBT_KEY);
					if (nbtArray.length == 0) {
						nbtArray = new byte[CodexSet.values().length];
					}
					PacketByteBuf responseBuf = new PacketByteBuf(Unpooled.buffer());

					// 도감 정보를 가져온 정보를 buf화하여 클라이언트로 보내줍니다.
					CodexS2CGetPacket.encode(new CodexS2CGetPacket(nbtArray), responseBuf);
					Identifier responsePacketId = new Identifier(MOD_ID, CodexS2CGetPacket.CODEX_GET_PACKET_RESPONSE_ID);
					ServerPlayNetworking.send(player, responsePacketId, responseBuf);
				});

		// 도감에 아이템 등록
		ServerPlayNetworking.registerGlobalReceiver(
				new Identifier(MOD_ID, CodexC2SPostPacket.CODEX_POST_PACKET_REQUEST_ID), (server, player, handler, buf, responseSender) ->
				{
					LOGGER.info(player.getUuid() + " codex post");
					// 패킷을 가져와서 플레이어의 코덱 정보에 저장
					int codexIndex = CodexC2SPostPacket.decode(buf);

					player.readCustomDataFromNbt();

					// 해당 유저가 도감에 해당하는 아이템을 보유하고 있는지를 체크합니다.
					Item item = CodexSet.values()[codexIndex].getItem();
					int itemCount = player.getInventory().count(item);
					if (itemCount < 10) {
						// 아이템을 개수만큼 보유하고 있지 않다면 도감 등록 처리를 하지 않습니다.
						player.sendMessage(Text.of("아이템 개수가 부족합니다."));
						return;
					} else {
						// 아이템이 충분하다면 아이템을 소모시킵니다.
						player.getInventory().remove(itemStack -> Item.getRawId(itemStack.getItem()) == Item.getRawId(item), 10, player.getInventory());
					}

					NbtCompound nbtCompound = new NbtCompound();
					player.writeNbt(nbtCompound);
					byte[] nbtArray = nbtCompound.getByteArray(CodexS2CGetPacket.CODEX_LIST_NBT_KEY);
					if (nbtArray.length == 0) {
						// 저장된 데이터가 없다면 새로 생성해줍니다.
						CodexSet[] codexSetArray = CodexSet.values();
						nbtArray = new byte[codexSetArray.length];
						nbtArray[codexIndex] = 1;
					} else if (nbtArray[codexIndex] == 1) {
						// 이미 도감에 등록되었다면 등록 처리를 하지 않습니다.
						player.sendMessage(Text.of("이미 도감에 등록된 아이템 입니다."));
						return;
					} else {
						// 이미 있다면 도감에 데이터를 채워줍니다.
						nbtArray[codexIndex] = 1;
					}
					nbtCompound.putByteArray(CodexS2CGetPacket.CODEX_LIST_NBT_KEY, nbtArray);
					player.readNbt(nbtCompound);
					player.sendMessage(Text.of("\"" + item.getName().getString()+ "\" 아이템이 도감에 등록되었습니다!"));
				});
	}
}