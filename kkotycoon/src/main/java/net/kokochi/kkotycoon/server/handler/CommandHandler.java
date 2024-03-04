package net.kokochi.kkotycoon.server.handler;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kokochi.kkotycoon.KkoTycoon;
import net.kokochi.kkotycoon.entity.codex.CodexInfo;
import net.kokochi.kkotycoon.entity.codex.CodexSet;
import net.kokochi.kkotycoon.entity.item.KkoTycoonItems;
import net.kokochi.kkotycoon.entity.player.ClientPlayerDataManager;
import net.kokochi.kkotycoon.entity.player.KkotycoonPlayerData;
import net.kokochi.kkotycoon.entity.player.ServerPlayerDataManager;
import net.kokochi.kkotycoon.packet.KkotycoonMainDataS2CGetPacket;
import net.kokochi.kkotycoon.packet.ShopScreenS2CPacket;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CommandHandler {

    public static void initCommand() {
        // 데이터 초기화 명령어
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("kkc")
                    .requires(source -> source.hasPermissionLevel(2)) // OP 권한 요구
                    .then(CommandManager.argument("playerId", StringArgumentType.string())
                                    .suggests(((context, builder) -> {
                                        return CommandSource.suggestMatching(context.getSource().getPlayerNames(), builder);
                                    }))
                            // 데이터 초기화 명령어
                            .then(CommandManager.literal("reset")
                                    .executes(context -> {
                                        String playerId = StringArgumentType.getString(context, "playerId");
                                        ServerPlayerEntity player = context.getSource().getServer().getPlayerManager().getPlayer(playerId);
                                        KkotycoonPlayerData kkotycoonPlayerData = ServerPlayerDataManager.resetPlayerData(player);
                                        player.sendMessage(Text.of("꼬타이쿤 게임 데이터가 초기화 되었습니다."));

                                        // 클라이언트에 응답 데이터를 내려줍니다.
                                        PacketByteBuf responseBuf = new PacketByteBuf(Unpooled.buffer());
                                        KkotycoonMainDataS2CGetPacket.encode(new KkotycoonMainDataS2CGetPacket(kkotycoonPlayerData), responseBuf);
                                        Identifier responsePacketId = new Identifier(KkoTycoon.MOD_ID, KkotycoonMainDataS2CGetPacket.CODEX_GET_PACKET_RESPONSE_ID);
                                        ServerPlayNetworking.send(player, responsePacketId, responseBuf);
                                        return 1;
                                    }))
                            // 코인 추가 명령어
                            .then(CommandManager.literal("addCoin")
                                    .then(CommandManager.argument("coinAmount", IntegerArgumentType.integer())
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
                                                Identifier responsePacketId = new Identifier(KkoTycoon.MOD_ID, KkotycoonMainDataS2CGetPacket.CODEX_GET_PACKET_RESPONSE_ID);
                                                ServerPlayNetworking.send(player, responsePacketId, responseBuf);
                                                return 1;
                                            })))
                            // 상점 스크린을 연다.
                            .then(CommandManager.literal("openShop")
                                    .executes(context -> {
                                        String playerId = StringArgumentType.getString(context, "playerId");
                                        ServerPlayerEntity player = context.getSource().getServer().getPlayerManager().getPlayer(playerId);
                                        // 클라이언트에 응답 데이터를 내려줍니다.
                                        PacketByteBuf responseBuf = new PacketByteBuf(Unpooled.buffer());
                                        Identifier responsePacketId = new Identifier(KkoTycoon.MOD_ID, ShopScreenS2CPacket.SHOP_OPEN_SCREEN_REQUEST_ID);
                                        ServerPlayNetworking.send(player, responsePacketId, responseBuf);
                                        return 1;
                                    }))
                    )
                    // 도감에 커스텀 아이템을 추가합니다.
                    .then(CommandManager.literal("addCodex")
                            .then(CommandManager.argument("count", IntegerArgumentType.integer())
                                    .executes(context -> {
                                        int count = IntegerArgumentType.getInteger(context, "count");
                                        ServerPlayerEntity player = context.getSource().getPlayer();

                                        ItemStack mainHandStack = player.getMainHandStack();
                                        List<CodexInfo> codexInfos = ServerPlayerDataManager.codexInfoList;
                                        int rawId = Item.getRawId(mainHandStack.getItem());
                                        codexInfos.add(new CodexInfo(rawId, count));

                                        // 클라이언트에 응답 데이터를 내려줍니다.
                                        PacketByteBuf responseBuf = new PacketByteBuf(Unpooled.buffer());
                                        KkotycoonMainDataS2CGetPacket.encode(new KkotycoonMainDataS2CGetPacket(ServerPlayerDataManager.getPlayerData(player)), responseBuf);
                                        Identifier responsePacketId = new Identifier(KkoTycoon.MOD_ID, KkotycoonMainDataS2CGetPacket.CODEX_GET_PACKET_RESPONSE_ID);
                                        ServerPlayNetworking.send(player, responsePacketId, responseBuf);
                                        return 1;
                                    })))
                    // 도감 정보를 기본값으로 초기화 합니다.
                    .then(CommandManager.literal("clearCodex")
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();

                                List<CodexInfo> codexInfos = ServerPlayerDataManager.codexInfoList;
                                codexInfos.clear();
                                codexInfos.addAll(Arrays.stream(CodexSet.values())
                                        .map(codexSet -> new CodexInfo(Item.getRawId(codexSet.getItem()), codexSet.getCount()))
                                        .collect(Collectors.toList()));

                                // 클라이언트에 응답 데이터를 내려줍니다.
                                PacketByteBuf responseBuf = new PacketByteBuf(Unpooled.buffer());
                                KkotycoonMainDataS2CGetPacket.encode(new KkotycoonMainDataS2CGetPacket(ServerPlayerDataManager.getPlayerData(player)), responseBuf);
                                Identifier responsePacketId = new Identifier(KkoTycoon.MOD_ID, KkotycoonMainDataS2CGetPacket.CODEX_GET_PACKET_RESPONSE_ID);
                                ServerPlayNetworking.send(player, responsePacketId, responseBuf);
                                return 1;
                            }))
            );

            // 출금 명령어
            dispatcher.register(CommandManager.literal("kcoin")
                    .then(CommandManager.argument("amount", LongArgumentType.longArg())
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(player);
                                long amount = LongArgumentType.getLong(context, "amount");

                                if (amount <= 0) {
                                    player.sendMessage(Text.of("1원 이상의 금액을 출금해주세요."));
                                    return 1;
                                }

                                if (playerData.getKkoCoin() < amount) {
                                    player.sendMessage(Text.of("보유중인 꼬코인이 부족합니다."));
                                    return 1;
                                }
                                PlayerInventory inventory = player.getInventory();
                                int emptySlot = inventory.getEmptySlot();
                                if (emptySlot == -1) {
                                    // 비어있는 슬롯 번호를 가져오는 메소드 이며, -1이면 비어있는 슬롯이 없는 것이다.
                                    player.sendMessage(Text.of("인벤토리가 꽉 차있습니다. 인벤토리를 비워주세요."));
                                    return 1;
                                }

                                playerData.setKkoCoin(playerData.getKkoCoin() - amount);
                                ItemStack itemStack = new ItemStack(KkoTycoonItems.KKO_COIN);
                                itemStack.setCustomName(Text.literal("§6" + NumberFormat.getInstance().format(amount) + "꼬코인"));
                                player.getInventory().insertStack(itemStack);
                                
                                PacketByteBuf responseBuf = new PacketByteBuf(Unpooled.buffer());
                                KkotycoonMainDataS2CGetPacket.encode(new KkotycoonMainDataS2CGetPacket(playerData), responseBuf);
                                Identifier responsePacketId = new Identifier(KkoTycoon.MOD_ID, KkotycoonMainDataS2CGetPacket.CODEX_GET_PACKET_RESPONSE_ID);
                                ServerPlayNetworking.send(player, responsePacketId, responseBuf);
                                return 1;
                            })));
            // 곡괭이 인챈트
            dispatcher.register(CommandManager.literal("kkcset")
                    .requires(source -> source.hasPermissionLevel(2)) // OP 권한 요구
                    .then(CommandManager.literal("pickaxe")
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();

                                ItemStack mainHandStack = player.getMainHandStack();
                                NbtCompound nbt = mainHandStack.getOrCreateNbt();
                                nbt.putInt(PlayerActionEventHandler.PICKAXE_EFF_LEVEL_KEY, 3);
                                nbt.putDouble(PlayerActionEventHandler.PICKAXE_EFF_WEIGHT_KEY, 0.5d);
                                mainHandStack.setNbt(nbt);
                                return 1;
                            }))
                    .then(CommandManager.literal("distance")
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(player);

                                player.sendMessage(Text.of("이동한 거리 : " + playerData.getAccumulatedDistance()));
                                return 1;
                            }))
                    .then(CommandManager.literal("ore")
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(player);

                                player.sendMessage(Text.of("캔 광물 : " + playerData.getAccumulatedBreakOreBlock()));
                                return 1;
                            }))
                    .then(CommandManager.literal("crop")
                            .executes(context -> {
                                ServerPlayerEntity player = context.getSource().getPlayer();
                                KkotycoonPlayerData playerData = ServerPlayerDataManager.getPlayerData(player);

                                player.sendMessage(Text.of("캔 농작물 : " + playerData.getAccumulatedBreakCropBlock()));
                                return 1;
                            }))
            );
        });
    }
}
