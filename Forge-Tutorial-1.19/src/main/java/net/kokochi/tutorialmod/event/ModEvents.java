package net.kokochi.tutorialmod.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.kokochi.tutorialmod.TutorialMod;
import net.kokochi.tutorialmod.item.ModItems;
import net.kokochi.tutorialmod.villiager.ModVillagers;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
}
