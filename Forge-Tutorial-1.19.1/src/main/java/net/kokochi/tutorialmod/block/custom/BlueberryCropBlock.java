package net.kokochi.tutorialmod.block.custom;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.slf4j.Logger;

// 커스텀 작물 만들기.
public class BlueberryCropBlock extends CropBlock {
    private static final Logger LOGGER = LogUtils.getLogger();

    // Crop의 기본 MaxAge는 7로 되어있다.
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 6);

    public BlueberryCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        // 시드 Id를 정의해준다.
        return super.getBaseSeedId();
    }

    @Override
    public IntegerProperty getAgeProperty() {
        // Age 상태값을 여기서 받도록 한다.
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 6;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        // 블럭 상태 정의를 추가해준다.
        builder.add(AGE);
    }

    // 성장 속도 수정하기.
    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource source) {
        super.randomTick(state, serverLevel, pos, source);
        LOGGER.info("randomTick()");
        if (!isMaxAge(state)) {
            if (serverLevel.getRawBrightness(pos.above(), 0) >= 9) {
                LOGGER.info("is Raw BrightNess");
                int chance = 1;

                int check = source.nextInt(chance);
                LOGGER.info("check is = {}", check);
                if (check == 0) {
                    serverLevel.setBlock(pos, state.setValue(AGE, getAge(state) + 1), 2);
                }
            }
        }
    }

    @Override
    public void fallOn(Level p_152426_, BlockState p_152427_, BlockPos p_152428_, Entity p_152429_, float p_152430_) {
        // 밟았을 떄 부숴지지 않도록 아무 동작도 하지 않게하기.
        //        super.fallOn(p_152426_, p_152427_, p_152428_, p_152429_, p_152430_);
    }
}
