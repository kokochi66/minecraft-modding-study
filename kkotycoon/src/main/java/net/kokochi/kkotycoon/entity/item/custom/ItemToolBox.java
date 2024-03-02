package net.kokochi.kkotycoon.entity.item.custom;

import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Optional;

// 아이템 수리 키트
public class ItemToolBox extends Item {
    public ItemToolBox(Settings settings) {
        super(settings);
    }


    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        // 툴팁 추가
        /*
        *
            무조건 모든 아이템의 내구도를 100%로 수리해줍니다.
            오른손에 수리 하고자 하는 아이템을 왼손에 두고
            우클릭으로 수리 할 수 있습니다.
        * */
        return super.getTooltipData(stack);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // 모루로 사용하는건 좀 힘드니까 이것도 오른손에 들고 왼손에
        return super.use(world, user, hand);
    }
}
