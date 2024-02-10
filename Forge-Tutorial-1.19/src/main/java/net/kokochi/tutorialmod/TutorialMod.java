package net.kokochi.tutorialmod;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
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

        // 모드 로딩을 위한 commonSetup 메서드를 등록합니다.
        modEventBus.addListener(this::commonSetup);

        // 서버 및 기타 게임 이벤트에 관심 있는 이벤트를 자신으로 등록합니다.
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // 일부 공통 설정 코드
        LOGGER.info("공통 설정에서 안녕하세요.");
        LOGGER.info("흙 블록 >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
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
        }
    }
}
