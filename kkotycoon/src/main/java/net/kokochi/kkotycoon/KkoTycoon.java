package net.kokochi.kkotycoon;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kokochi.kkotycoon.client.data.CodexSet;
import net.kokochi.kkotycoon.packet.KkotycoonMainDataS2CGetPacket;
import net.kokochi.kkotycoon.packet.CodexC2SPostPacket;
import net.kokochi.kkotycoon.entity.player.KkotycoonPlayerData;
import net.kokochi.kkotycoon.entity.player.ServerPlayerDataManager;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KkoTycoon implements ModInitializer {
	public static final String MOD_ID = "kkotycoon";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// 게임 실행 될 때의 서버측 로직
		ServerPlayNetworking.registerGlobalReceiver(
				new Identifier(MOD_ID, KkotycoonMainDataS2CGetPacket.CODEX_GET_PACKET_REQUEST_ID), (server, player, handler, buf, responseSender) ->
				{
					// 사용자의 최초 도감 정보 조회
					LOGGER.info(player.getUuid() + " codex get");

					// 도감 정보를 가져온 정보를 buf화하여 클라이언트로 보내줍니다.
					PacketByteBuf responseBuf = new PacketByteBuf(Unpooled.buffer());
					KkotycoonMainDataS2CGetPacket.encode(new KkotycoonMainDataS2CGetPacket(ServerPlayerDataManager.getPlayerData(player)), responseBuf);
					Identifier responsePacketId = new Identifier(MOD_ID, KkotycoonMainDataS2CGetPacket.CODEX_GET_PACKET_RESPONSE_ID);
					ServerPlayNetworking.send(player, responsePacketId, responseBuf);
				});

		// 도감에 아이템 등록
		ServerPlayNetworking.registerGlobalReceiver(
				new Identifier(MOD_ID, CodexC2SPostPacket.CODEX_POST_PACKET_REQUEST_ID), (server, player, handler, buf, responseSender) ->
				{
					LOGGER.info(player.getUuid() + " codex post");
					// 패킷을 가져와서 플레이어의 코덱 정보에 저장
					int codexIndex = CodexC2SPostPacket.decode(buf);


					// 해당 유저가 도감에 해당하는 아이템을 보유하고 있는지를 체크합니다.
					Item item = CodexSet.values()[codexIndex].getItem();
					int itemCount = player.getInventory().count(item);
					if (itemCount < 10) {
						// 아이템을 개수만큼 보유하고 있지 않다면 도감 등록 처리를 하지 않습니다.
						player.sendMessage(Text.of("아이템 개수가 부족합니다."));
						return;
					}

					KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(player);
					byte[] nbtArray = playerData.getCodexArray();
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
						// 아이템을 소모처리 해줍니다.
						player.getInventory().remove(itemStack -> Item.getRawId(itemStack.getItem()) == Item.getRawId(item), 10, player.getInventory());
					}

					// 클라이언트에 응답 데이터를 내려줍니다.
					PacketByteBuf responseBuf = new PacketByteBuf(Unpooled.buffer());
					KkotycoonMainDataS2CGetPacket.encode(new KkotycoonMainDataS2CGetPacket(playerData), responseBuf);
					Identifier responsePacketId = new Identifier(MOD_ID, KkotycoonMainDataS2CGetPacket.CODEX_GET_PACKET_RESPONSE_ID);
					ServerPlayNetworking.send(player, responsePacketId, responseBuf);
					player.sendMessage(Text.of("\"" + item.getName().getString()+ "\" 아이템이 도감에 등록되었습니다!"));
				});

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

			// 데이터 초기화 명령어
			dispatcher.register(CommandManager.literal("kkc")
					.then(CommandManager.argument("playerId", StringArgumentType.string())
							.then(CommandManager.literal("reset")
									.requires(source -> source.hasPermissionLevel(2)) // OP 권한 요구
									.executes(context -> {
										String playerId = StringArgumentType.getString(context, "playerId");
										ServerPlayerEntity player = context.getSource().getServer().getPlayerManager().getPlayer(playerId);
										KkotycoonPlayerData kkotycoonPlayerData = ServerPlayerDataManager.resetPlayerData(player);
										player.sendMessage(Text.of("꼬타이쿤 게임 데이터가 초기화 되었습니다."));

										// 클라이언트에 응답 데이터를 내려줍니다.
										PacketByteBuf responseBuf = new PacketByteBuf(Unpooled.buffer());
										KkotycoonMainDataS2CGetPacket.encode(new KkotycoonMainDataS2CGetPacket(kkotycoonPlayerData), responseBuf);
										Identifier responsePacketId = new Identifier(MOD_ID, KkotycoonMainDataS2CGetPacket.CODEX_GET_PACKET_RESPONSE_ID);
										ServerPlayNetworking.send(player, responsePacketId, responseBuf);
										return 1;
									}))));

			// 코인 추가 명령어
			dispatcher.register(CommandManager.literal("kkc")
					.then(CommandManager.argument("playerId", StringArgumentType.string())
							.then(CommandManager.literal("addCoin")
									.then(CommandManager.argument("coinAmount", IntegerArgumentType.integer())
											.requires(source -> source.hasPermissionLevel(2)) // OP 권한 요구
											.executes(context -> {
												String playerId = StringArgumentType.getString(context, "playerId");
												int coinAmount = IntegerArgumentType.getInteger(context, "coinAmount");
												ServerPlayerEntity player = context.getSource().getServer().getPlayerManager().getPlayer(playerId);
												KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(player);
												playerData.setKkoCoin(playerData.getKkoCoin() + coinAmount);
												player.sendMessage(Text.of("코인이 지급되었습니다."));

												// 클라이언트에 응답 데이터를 내려줍니다.
												PacketByteBuf responseBuf = new PacketByteBuf(Unpooled.buffer());
												KkotycoonMainDataS2CGetPacket.encode(new KkotycoonMainDataS2CGetPacket(playerData), responseBuf);
												Identifier responsePacketId = new Identifier(MOD_ID, KkotycoonMainDataS2CGetPacket.CODEX_GET_PACKET_RESPONSE_ID);
												ServerPlayNetworking.send(player, responsePacketId, responseBuf);
												return 1;
											})))));
		});
	}
}