package net.kokochi.tutorialmod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.RandomUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// 커스텀 아이템을 위한 별도의 아이템 클래스를 만든다.
public class EightBallItem extends Item {
    public EightBallItem(Properties properties) {
        super(properties);
    }


    // 인벤 창에서 표시되는 설명문을 추가하기 (Shift를 누른채로 보면 툴팁이 나옵니다.)
    @Override
    public void appendHoverText(ItemStack stack,
                                @Nullable Level level,
                                List<Component> components,
                                TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            components.add(Component.literal("Right click to get a random number!")
                    .withStyle(ChatFormatting.GOLD));
        } else {
            components.add(Component.literal("Press SHIFT for more info")
                    // 채팅 스타일을 withStyle로 설정할 수 있다.
                    .withStyle(ChatFormatting.DARK_AQUA));
        }

        super.appendHoverText(stack, level, components, flag);
    }

    // 아이템 사용 메소드
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        if (level.isClientSide()                       // 마크는 동일한 요청에 대한 클라이언트 사이드와 서버 사이드의 요청이 두번 받아진다.
                && hand == InteractionHand.MAIN_HAND    // 메인 손(오른손)에 해당 아이템을 갖고 있을 떄
        ) {
            // 0부터 10까지의 랜덤 숫자까지를 만든다.
            int randomNumber = RandomSource.createNewThreadLocalInstance().nextInt(10);
            player.sendSystemMessage(Component.literal("Your Number is " + randomNumber));

            // 쿨타임을 적용한다. (틱으로 적용됨)
            player.getCooldowns().addCooldown(this, 5);
        }
        return super.use(level, player, hand);
    }
}
