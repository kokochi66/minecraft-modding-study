package net.kokochi.kkotycoon.entity.item.custom;

import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Optional;

// 아이템 강화 주문서
public class ItemEffectScroll extends Item {
    public ItemEffectScroll(Settings settings) {
        super(settings);
    }


    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        // 툴팁 추가
        /*
        *   강화 주문서는 오른손에
            강화하고자 하는 아이템을 왼손에 두고
            우클릭으로 강화할 수 있습니다.
            (강화 가능 아이템 (곡괭이, 검, 괭이, 활)
        * */
        return super.getTooltipData(stack);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // 오른손에 강화주문서를 갖고 있을 때 왼손 아이템을 강화하도록 설정
        return super.use(world, user, hand);
    }
}
