package net.kokochi.tutorialmod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

// 빛나는 지르콘 블록을 만들기.
public class ZirconLampBlock extends Block {
    // 빛나는 속성을 만들기 위해서 Lit 프로퍼티를 만듭니다.
    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    // 프로퍼티는 Integer, 불리언 등등이 존재한다.
    // 이 프로퍼티를 blockstates에서 속성값으로 사용해서, 각각의 상태를 나타내도록 한다.

    public ZirconLampBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state,
                                 Level level,
                                 BlockPos pos,
                                 Player player,
                                 InteractionHand hand,
                                 BlockHitResult res) {
        if (!level.isClientSide()
                && hand == InteractionHand.MAIN_HAND) {
            // BooleanProperty는 true 혹은 false의 값만 가질 수 있다.
            level.setBlock(pos, state.cycle(LIT), 3);
        }
        return super.use(state, level, pos, player, hand, res);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}
