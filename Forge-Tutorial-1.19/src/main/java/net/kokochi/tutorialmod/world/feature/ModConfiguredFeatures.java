package net.kokochi.tutorialmod.world.feature;

import com.google.common.base.Suppliers;
import net.kokochi.tutorialmod.TutorialMod;
import net.kokochi.tutorialmod.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public class ModConfiguredFeatures {
    // CONFIGURED_FEATURES를 사용하여 ConfiguredFeature 인스턴스를 등록하기 위한 DeferredRegister 생성
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
            DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, TutorialMod.MOD_ID);

    // 각 환경(오버월드, 엔드, 네더)에 따른 지르콘 광물의 대상 블록 상태를 지정합니다.
    // memoize를 사용하여 이러한 값이 단 한 번만 계산되고 캐시되도록 합니다.
    /* Supplier<List<OreConfiguration.TargetBlockState>> 타입을 사용해 지연 로딩을 구현합니다.
     * Supplier 인터페이스는 Java에서 제공하는 함수형 인터페이스로, 호출 시점에 결과를 제공합니다.
     * 여기서는 Suppliers.memoize를 사용하여 결과를 캐싱하고, 이후 호출에서는 캐싱된 결과를 재사용합니다.
     * 이 방식은 계산 비용이 높거나 결과가 변경되지 않을 때 성능 최적화에 유용합니다. */
    public static final Supplier<List<OreConfiguration.TargetBlockState>> OVER_WORLD_ZIRCON_ORES =
            Suppliers.memoize(() -> List.of(
                    /* OreConfiguration.target을 사용하여 지르콘 광물이 생성될 대상 블록 상태를 정의합니다.
                     * 첫 번째 매개변수는 광물이 생성될 수 있는 블록 유형(예: 돌)을 나타냅니다.
                     * 두 번째 매개변수는 실제로 생성될 광물 블록의 상태를 지정합니다. */
                    OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.ZIRCON_ORE.get().defaultBlockState()),
                    OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_ZIRCON_ORE.get().defaultBlockState())
            ));

    public static final Supplier<List<OreConfiguration.TargetBlockState>> END_ZIRCON_ORES =
            Suppliers.memoize(() -> List.of(
                    /* BlockMatchTest를 사용하여 특정 블록 유형(여기서는 엔드 스톤)에서만 광물이 생성되도록 합니다.
                     * 이는 광물이 특정 조건을 충족하는 위치에만 생성되도록 하는 데 사용됩니다. */
                    OreConfiguration.target(new BlockMatchTest(Blocks.END_STONE), ModBlocks.ENDSTONE_ZIRCON_ORE.get().defaultBlockState())
            ));

    public static final Supplier<List<OreConfiguration.TargetBlockState>> NETHER_ZIRCON_ORES =
            Suppliers.memoize(() -> List.of(
                    /* NETHER_ORE_REPLACEABLES를 사용하여 네더 광물이 생성될 수 있는 대상 블록 유형을 지정합니다.
                     * 네더에서는 주로 네더랙이 이 역할을 합니다. */
                    OreConfiguration.target(OreFeatures.NETHER_ORE_REPLACEABLES, ModBlocks.NETHERRACK_ZIRCON_ORE.get().defaultBlockState())
            ));

    // 각 환경(오버월드, 엔드, 네더)에 대한 지르콘 광물의 구성된 특징을 등록합니다.
    // 이 구성된 특징은 광물의 생성 규칙(위치, 크기 등)을 정의합니다.
    public static final RegistryObject<ConfiguredFeature<?, ? >> ZIRCON_ORE = CONFIGURED_FEATURES.register("zircon_ore",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OVER_WORLD_ZIRCON_ORES.get(), 7)));
    public static final RegistryObject<ConfiguredFeature<?, ? >> END_ZIRCON_ORE = CONFIGURED_FEATURES.register("end_zircon_ore",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(END_ZIRCON_ORES.get(), 9)));
    public static final RegistryObject<ConfiguredFeature<?, ? >> NETHER_ZIRCON_ORE = CONFIGURED_FEATURES.register("nether_zircon_ore",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(NETHER_ZIRCON_ORES.get(), 9)));

    // 이 메소드는 모드의 초기화 동안 이 클래스에서 생성된 모든 구성된 특징을 실제 게임 레지스트리에 등록하는 데 사용됩니다.
    public static void register(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus);
    }
}