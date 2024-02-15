package net.kokochi.tutorialmod.fluid;

import com.mojang.math.Vector3f;
import net.kokochi.tutorialmod.TutorialMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// ModFluidTypes 클래스는 모든 커스텀 액체 타입을 등록하는 역할을 합니다.
public class ModFluidTypes {
    // 액체의 정지 상태와 흐르는 상태의 리소스 위치를 정의합니다.
    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");

    // 커스텀 액체 오버레이의 리소스 위치를 정의합니다.
    public static final ResourceLocation SOAP_OVERLAY_RL = new ResourceLocation(TutorialMod.MOD_ID, "misc/in_soap_water");

    // 커스텀 액체 타입을 등록하기 위한 DeferredRegister 객체입니다.
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, TutorialMod.MOD_ID);

    // 커스텀 액체 타입을 등록합니다. 여기서 BaseFluidType 클래스의 인스턴스를 생성하고, 액체의 텍스처와 속성을 지정합니다.
    public static final RegistryObject<FluidType> SOAP_WATER_FLUID_TYPE = register("soap_water_fluid",
            FluidType.Properties.create().lightLevel(2).density(15).viscosity(5).sound(SoundAction.get("drink"), SoundEvents.HONEY_DRINK));

    // 커스텀 액체 타입을 등록하는 헬퍼 메소드입니다.
    private static RegistryObject<FluidType> register(String name, FluidType.Properties properties) {
        return FLUID_TYPES.register(name, () -> new BaseFluidType(
                WATER_STILL_RL, WATER_FLOWING_RL, SOAP_OVERLAY_RL,
                0xA1E038D0, // 액체의 색상
                new Vector3f(224f / 255f, 56f / 255f, 208f/255f), // 안개 색상
                properties)
        );
    }

    // 모드의 이벤트 버스에 액체 타입을 등록하는 메소드입니다.
    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
