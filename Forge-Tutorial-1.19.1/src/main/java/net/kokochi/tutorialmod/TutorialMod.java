package net.kokochi.tutorialmod;

import com.mojang.logging.LogUtils;
import net.kokochi.tutorialmod.block.ModBlocks;
import net.kokochi.tutorialmod.fluid.ModFluidTypes;
import net.kokochi.tutorialmod.fluid.ModFluids;
import net.kokochi.tutorialmod.item.ModItems;
import net.kokochi.tutorialmod.networking.ModMessages;
import net.kokochi.tutorialmod.painting.ModPaintings;
import net.kokochi.tutorialmod.villiager.ModVillagers;
import net.kokochi.tutorialmod.world.feature.ModConfiguredFeatures;
import net.kokochi.tutorialmod.world.feature.ModPlacedFeatures;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// META-INF/mods.toml 파일에 있는 항목과 일치해야 합니다.
@Mod(TutorialMod.MOD_ID)
public class TutorialMod
{
    // 모든 것이 참조하는 공통된 곳에 모드 ID를 정의합니다.
    public static final String MOD_ID = "tutorialmod";
    // slf4j 로거를 직접 참조합니다.
    private static final Logger LOGGER = LogUtils.getLogger();

    public TutorialMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 생성한 모드 아이템을 register 한다. (최초 모드가 실행될 때 진행)
        ModItems.register(modEventBus);
        // 생성한 모드 블럭을 register 한다.
        ModBlocks.register(modEventBus);

        ModVillagers.register(modEventBus);
        ModPaintings.register(modEventBus);
        ModConfiguredFeatures.register(modEventBus);
        ModPlacedFeatures.register(modEventBus);

        ModFluids.register(modEventBus);
        ModFluidTypes.register(modEventBus);


        // 모드 로딩을 위한 commonSetup 메서드를 등록합니다.
        modEventBus.addListener(this::commonSetup);

        // 서버 및 기타 게임 이벤트에 관심 있는 이벤트를 자신으로 등록합니다.
        MinecraftForge.EVENT_BUS.register(this);
    }


    /*
    * commonSetup 메소드
    * 마인크래프트 모드가 초기화 될 때 호출 (기본적인 초기화 작업이 실행될 때 호출됨.)
    * enqueueWork 내에서 게임 로직 다중 스레드 환경에서 안정적으로 실행되도록 도움을 줄 수있음.
    * */
    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            ModVillagers.registerPOIs();
        });

        /*
        *
        * */
        ModMessages.register();
    }

    // SubscribeEvent를 사용하여 이벤트 버스에서 호출될 메서드를 발견하도록 할 수 있습니다.
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // 서버가 시작될 때 작업을 수행합니다.
        LOGGER.info("서버 시작에서 안녕하세요.");
    }

    // EventBusSubscriber를 사용하여 @SubscribeEvent로 주석이 달린 클래스의 모든 정적 메서드를 자동으로 등록할 수 있습니다.
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // 클라이언트 설정 코드
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BLUEBERRY_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SOAP_WATER.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SOAP_WATER.get(), RenderType.translucent());
        }
    }
}
