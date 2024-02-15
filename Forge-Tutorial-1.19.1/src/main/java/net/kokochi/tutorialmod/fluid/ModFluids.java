package net.kokochi.tutorialmod.fluid;

import net.kokochi.tutorialmod.TutorialMod;
import net.kokochi.tutorialmod.block.ModBlocks;
import net.kokochi.tutorialmod.item.ModItems;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


// ModFluids 클래스는 실제 액체 인스턴스와 그들의 속성을 정의하고 등록하는 역할을 합니다.
public class ModFluids {
    // FLUIDS는 모든 액체 인스턴스를 등록하기 위한 DeferredRegister 객체입니다.
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, TutorialMod.MOD_ID);

    // SOURCE_SOAP_WATER는 커스텀 액체의 '소스' 블록을 정의합니다.
    public static final RegistryObject<FlowingFluid> SOURCE_SOAP_WATER = FLUIDS.register("soap_water_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.SOAP_WATER_FLUID_PROPERTIES));

    // FLOWING_SOAP_WATER는 커스텀 액체가 흐를 때 사용되는 '흐르는' 액체 블록을 정의합니다.
    public static final RegistryObject<FlowingFluid> FLOWING_SOAP_WATER = FLUIDS.register("flowing_soap_water",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.SOAP_WATER_FLUID_PROPERTIES));

    // SOAP_WATER_FLUID_PROPERTIES는 커스텀 액체의 물리적 및 렌더링 속성을 설정합니다.
    // 여기서는 액체가 얼마나 빠르게 퍼지는지, 높이 흐르는지, 해당 액체 블록과 버킷 아이템 등을 지정합니다.
    public static final ForgeFlowingFluid.Properties SOAP_WATER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            ModFluidTypes.SOAP_WATER_FLUID_TYPE, SOURCE_SOAP_WATER, FLOWING_SOAP_WATER)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(ModBlocks.SOAP_WATER_BLOCK).bucket(ModItems.SOAP_WATER_BUCKET);

    // 모드의 이벤트 버스에 액체 인스턴스를 등록하는 메소드입니다.
    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}