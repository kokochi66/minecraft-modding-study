package net.kokochi.tutorialmod.painting;

import net.kokochi.tutorialmod.TutorialMod;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// 커스텀 그림 모드를 만들기
public class ModPaintings {
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS =
            DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, TutorialMod.MOD_ID);

    public static final RegistryObject<PaintingVariant> PLANT =
            PAINTING_VARIANTS.register("plant",
                    () -> new PaintingVariant(16, 16)   // 그림의 사이즈를 정의합니다.
            );
    public static final RegistryObject<PaintingVariant> WANDERER =
            PAINTING_VARIANTS.register("wanderer",
                    () -> new PaintingVariant(16, 32)   // 그림의 사이즈를 정의합니다.
            );
    public static final RegistryObject<PaintingVariant> SUNSET =
            PAINTING_VARIANTS.register("sunset",
                    () -> new PaintingVariant(32, 16)   // 그림의 사이즈를 정의합니다.
            );

    public static void register(IEventBus eventBus) {
        PAINTING_VARIANTS.register(eventBus);
    }
}
