package net.kokochi.tutorialmod.villiager;

import com.google.common.collect.ImmutableSet;
import net.kokochi.tutorialmod.TutorialMod;
import net.kokochi.tutorialmod.block.ModBlocks;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.InvocationTargetException;

// 커스텀 마을 주민을 만든다.
public class ModVillagers {
    // POI 타입과 마을 주민 직업을 등록하기 위한 DeferredRegister 객체를 생성합니다.
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, TutorialMod.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, TutorialMod.MOD_ID);

    // 커스텀 POI 타입을 정의합니다. 여기서는 'jumpy_block'이라는 블록을 POI로 사용합니다.
    public static final RegistryObject<PoiType> JUMPY_BLOCK_POI = POI_TYPES.register("jumpy_block_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.JUMPY_BLOCK.get().getStateDefinition().getPossibleStates()),
                    1, 1));

    // 커스텀 주민 직업을 정의합니다. 'jumpy_master'라는 직업을 생성하며, 이 직업은 'jumpy_block_poi'를 작업장으로 사용합니다.
    public static final RegistryObject<VillagerProfession> JUMP_MASTER = VILLAGER_PROFESSIONS.register("jumpy_master",
            () -> new VillagerProfession(
                    "jumpy_master",                      // 주민 직업의 이름입니다. 이 이름은 주민과의 상호작용, UI, 통계 등에서 사용됩니다.
                    x -> x.get() == JUMPY_BLOCK_POI.get(),  // 주민이 작업을 위해 사용하는 POI(관심 지점)를 확인하는 Predicate입니다. 여기서는 주민이 'jumpy_block_poi'를 작업장으로 인식하도록 설정됩니다.
                    x -> x.get() == JUMPY_BLOCK_POI.get(),  // 주민이 모여드는 미팅 포인트를 확인하는 Predicate입니다. 일반적으로 작업장 POI와 동일하게 설정됩니다.
                    ImmutableSet.of(),                      // 주민이 수행할 수 있는 작업 목록입니다. 이 컬렉션은 작업 유형의 집합으로, 주민이 할 수 있는 다양한 작업을 정의합니다.
                    ImmutableSet.of(),                      // 주민이 휴식을 취할 때 사용하는 POI입니다. 예를 들어, 침대와 같은 휴식 관련 블록을 여기에 추가할 수 있습니다.
                    SoundEvents.VILLAGER_WORK_ARMORER       // 주민이 작업을 할 때 재생되는 소리입니다. 여기서는 대장장이 주민의 작업 소리를 사용하고 있습니다.
            )
    );


    // POI의 블록 상태를 등록하는 메서드입니다. 이 코드는 현재 불완전합니다.
    public static void registerPOIs() {
        try {
            ObfuscationReflectionHelper.findMethod(PoiType.class,
                    "registerBlockStates", PoiType.class).invoke(null, JUMPY_BLOCK_POI.get());
        } catch (InvocationTargetException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    // 이벤트 버스에 POI 타입과 마을 주민 직업을 등록합니다. 이를 통해 게임이 로드될 때 해당 요소들이 게임에 추가됩니다.
    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
