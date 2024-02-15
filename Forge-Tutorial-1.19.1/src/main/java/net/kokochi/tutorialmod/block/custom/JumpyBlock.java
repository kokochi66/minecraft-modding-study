package net.kokochi.tutorialmod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class JumpyBlock extends Block {
    public JumpyBlock(Properties properties) {
        super(properties);
    }


    @Override
    public InteractionResult use(BlockState state,
                                 Level level,
                                 BlockPos blockPos,
                                 Player player,
                                 InteractionHand hand,
                                 BlockHitResult result)
    {
        // 클라이언트 사이드인지를 체크하고, Main Hand인지 Off Hand인지 검증해야한다.
        // 이 검증을 거치지 않으면 이 메소드는 4번 돌기 때문에 4번 실행된다.
        if (level.isClientSide()
                && hand == InteractionHand.MAIN_HAND) {
            player.sendSystemMessage(Component.literal("Right Clicked this!"));
        }

        if (!level.isClientSide()
                && hand == InteractionHand.MAIN_HAND) {
            player.sendSystemMessage(Component.literal("Server :: Right Clicked this!"));
        }
        return super.use(state, level, blockPos, player, hand, result);
    }

    // 이것을 밟으면 점프한다는 효과를 넣기 위함
    @Override
    public void stepOn(Level level,
                       BlockPos pos,
                       BlockState state,
                       Entity entity)
    {
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.JUMP, 200));
        }

        super.stepOn(level, pos, state, entity);
    }
}
