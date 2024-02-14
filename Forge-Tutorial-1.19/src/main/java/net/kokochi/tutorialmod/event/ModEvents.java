package net.kokochi.tutorialmod.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.kokochi.tutorialmod.TutorialMod;
import net.kokochi.tutorialmod.item.ModItems;
import net.kokochi.tutorialmod.networking.ModMessages;
import net.kokochi.tutorialmod.networking.packet.ThirstDataSyncC2SPacket;
import net.kokochi.tutorialmod.thirst.PlayerThirst;
import net.kokochi.tutorialmod.thirst.PlayerThirstProvider;
import net.kokochi.tutorialmod.villiager.ModVillagers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = TutorialMod.MOD_ID)
public class ModEvents {
    // 마을 주민 거래 이벤트에 대한 구독 메서드
    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        // 도구 제작자에 대한 커스텀 거래 추가
        if(event.getType() == VillagerProfession.TOOLSMITH) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades(); // 현재 거래 목록을 가져옵니다.
            ItemStack stack = new ItemStack(ModItems.EIGHT_BALL.get(), 1); // 거래에 사용할 아이템 스택을 생성합니다.
            int villagerLevel = 1; // 거래를 추가할 주민 레벨을 지정합니다.

            // 거래 목록에 새 거래를 추가합니다. 여기서는 2개의 에메랄드로 1개의 Eight Ball을 구매할 수 있는 거래입니다.
            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2), // 비용
                    stack, // 결과 아이템
                    10, // 최대 사용 횟수
                    8, // 경험치
                    0.02F)); // 가격 증가 비율
        }

        // 커스텀 직업 'JUMP_MASTER'에 대한 커스텀 거래 추가
        if(event.getType() == ModVillagers.JUMP_MASTER.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades(); // 현재 거래 목록을 가져옵니다.
            ItemStack stack = new ItemStack(ModItems.BLUEBERRY.get(), 15); // 거래에 사용할 아이템 스택을 생성합니다.
            int villagerLevel = 1; // 거래를 추가할 주민 레벨을 지정합니다.

            // 거래 목록에 새 거래를 추가합니다. 여기서는 5개의 에메랄드로 15개의 Blueberry를 구매할 수 있는 거래입니다.
            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 5), // 비용
                    stack, // 결과 아이템
                    10, // 최대 사용 횟수
                    8, // 경험치
                    0.02F)); // 가격 증가 비율
        }
    }

    // 플레이어 엔티티에 Capability를 부착하는 이벤트 핸들러입니다.
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        // 이벤트가 플레이어 엔티티에 대한 것인지 확인합니다.
        if (event.getObject() instanceof Player) {
            // 해당 플레이어가 이미 PlayerThirst Capability를 가지고 있지 않다면,
            if (!event.getObject().getCapability(PlayerThirstProvider.PLAYER_THIRST).isPresent()) {
                // PlayerThirstProvider 인스턴스를 새로 생성하고, 이를 플레이어에 부착합니다.
                event.addCapability(new ResourceLocation(TutorialMod.MOD_ID, "properties"), new PlayerThirstProvider());
            }
        }
    }

    // 플레이어가 죽었다가 부활할 때, 이전 플레이어의 목마름 상태를 새 플레이어에게 복사하는 이벤트 핸들러입니다.
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        // 플레이어가 죽었다가 부활한 경우에만 로직을 실행합니다.
        if (event.isWasDeath()) {
            // 이전 플레이어의 목마름 상태를 가져옵니다.
            event.getOriginal().getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(oldStore -> {
                // 새 플레이어에게 이전 플레이어의 목마름 상태를 복사합니다.
                event.getOriginal().getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(newStore -> {
                    newStore.copyForm(oldStore);
                });
            });
        }
    }

    // PlayerThirst Capability를 시스템에 등록하는 이벤트 핸들러입니다.
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        // PlayerThirst 클래스의 인스턴스를 Capability 시스템에 등록합니다.
        event.register(PlayerThirst.class);
    }

    // 플레이어의 틱 이벤트를 처리하는 이벤트 핸들러입니다. 이 로직은 서버 측에서만 실행됩니다.
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // 이벤트가 서버 측에서 발생했는지 확인합니다.
        if (event.side == LogicalSide.SERVER) {
            // 플레이어의 목마름 상태를 가져옵니다.
            event.player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {
                // 플레이어의 목마름이 0보다 크고, 랜덤한 확률(0.005, 즉 0.5%)로 목마름을 1 감소시킵니다.
                // Minecraft는 기본적으로 초당 20틱(tick)으로 동작하므로, 0.5%의 확률은 평균적으로 20틱 * 100 (0.005의 역수) = 2000틱, 즉 100초마다 한 번씩 발생합니다.
                // 따라서 이 로직은 평균적으로 100초마다 목마름을 1 감소시키는 것으로 계산될 수 있습니다.
                if (thirst.getThirst() > 0 && event.player.getRandom().nextFloat() < 0.005f) {
                    thirst.subThirst(1);
                    // 플레이어에게 목마름이 감소했다는 메시지를 보냅니다.
                    ModMessages.sendToPlayer(new ThirstDataSyncC2SPacket(thirst.getThirst()), (ServerPlayer) event.player);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide()) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {
                    // 플레이어가 월드에 join 했을 때에도 목마름 패킷을 보내도록 한다.
                    ModMessages.sendToPlayer(new ThirstDataSyncC2SPacket(thirst.getThirst()), player);
                });
            }
        }

    }

}
