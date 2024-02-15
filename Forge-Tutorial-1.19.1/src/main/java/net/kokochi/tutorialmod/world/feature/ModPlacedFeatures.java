package net.kokochi.tutorialmod.world.feature;

import com.google.common.base.Suppliers;
import net.kokochi.tutorialmod.TutorialMod;
import net.kokochi.tutorialmod.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

// ModPlacedFeatures 클래스는 게임 세계에 광물을 배치하는 방법을 정의합니다.
public class ModPlacedFeatures {
    // PLACED_FEATURES는 PlacedFeature 객체를 안전하게 등록하기 위한 DeferredRegister입니다.
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
            DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, TutorialMod.MOD_ID);

    // ZIRCON_ORE_PLACED는 지르콘 광물의 배치 설정을 등록합니다.
    // 이 설정은 광물이 오버월드에서 어디에, 얼마나 자주 생성될지를 결정합니다.
    public static final RegistryObject<PlacedFeature> ZIRCON_ORE_PLACED = PLACED_FEATURES.register("zircon_ore_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.ZIRCON_ORE.getHolder().get(),
                    commonOrePlacement(7, // VeinsPerChunk
                            HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))));

    // END_ZIRCON_ORE_PLACED는 엔드 차원에서 지르콘 광물의 배치 설정을 등록합니다.
    // uniform() 메소드는 광물이 일정한 높이 범위에 균일하게 분포되도록 합니다.
    public static final RegistryObject<PlacedFeature> END_ZIRCON_ORE_PLACED = PLACED_FEATURES.register("end_zircon_ore_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.END_ZIRCON_ORE.getHolder().get(), commonOrePlacement(7, // VeinsPerChunk
                    HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))));

    // NETHER_ZIRCON_ORE_PLACED는 네더 차원에서 지르콘 광물의 배치 설정을 등록합니다.
    // 이 설정은 광물이 네더 차원에서 어디에, 얼마나 자주 생성될지를 결정합니다.
    public static final RegistryObject<PlacedFeature> NETHER_ZIRCON_ORE_PLACED = PLACED_FEATURES.register("nether_zircon_ore_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.NETHER_ZIRCON_ORE.getHolder().get(), commonOrePlacement(7, // VeinsPerChunk
                    HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))));

    // orePlacement 메소드는 광물이 생성될 위치의 기본 조건을 정의합니다.
    // 이 메소드는 광물의 배치를 더 상세하게 커스터마이징할 수 있도록 합니다.
    public static List<PlacementModifier> orePlacement(PlacementModifier count, PlacementModifier heightRange) {
        return List.of(count, InSquarePlacement.spread(), heightRange, BiomeFilter.biome());
    }

    // commonOrePlacement 메소드는 일반 광물에 대한 표준 배치 조건을 제공합니다.
    // 이는 광물이 생성될 빈도(veins per chunk)와 높이 범위를 설정합니다.
    public static List<PlacementModifier> commonOrePlacement(int veinsPerChunk, PlacementModifier heightRange) {
        return orePlacement(CountPlacement.of(veinsPerChunk), heightRange);
    }

    // rareOrePlacement 메소드는 드문 광물에 대한 배치 조건을 제공합니다.
    // 이는 광물이 매우 드물게 생성되도록 설정합니다(예: 평균적으로 매 50 청크당 한 번).
    public static List<PlacementModifier> rareOrePlacement(int rarity, PlacementModifier heightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(rarity), heightRange);
    }

    // register 메소드는 모드의 초기화 동안 이 클래스에서 생성된 모든 배치 특성을 게임 레지스트리에 등록합니다.
    public static void register(IEventBus eventBus) {
        PLACED_FEATURES.register(eventBus);
    }
}
