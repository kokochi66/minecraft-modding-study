package net.kokochi.tutorialmod.event;

import net.kokochi.tutorialmod.TutorialMod;
import net.kokochi.tutorialmod.client.ThirstHudOverlay;
import net.kokochi.tutorialmod.networking.ModMessages;
import net.kokochi.tutorialmod.networking.packet.DrinkWaterC2SPacket;
import net.kokochi.tutorialmod.util.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// ClientEvents 클래스는 클라이언트 측 이벤트를 처리합니다.
public class ClientEvents {

    // ClientForgeEvents는 포지 이벤트 시스템을 통해 게임의 클라이언트 측 이벤트를 처리하는 내부 클래스입니다.
    // @Mod.EventBusSubscriber 어노테이션을 사용하여 이 클래스가 이벤트 구독자임을 나타냅니다.
    // modid는 이 이벤트 리스너가 속한 모드의 ID를 지정하고, value = Dist.CLIENT는 이 코드가 클라이언트 측에서만 실행되어야 함을 나타냅니다.
    @Mod.EventBusSubscriber(modid = TutorialMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        // onKeyRegister 메소드는 키 바인딩을 게임에 등록하는 이벤트 핸들러입니다.
        // RegisterKeyMappingsEvent는 게임 초기화 중에 발생하며, 모든 키 바인딩을 등록할 때 사용됩니다.
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            // KeyBinding.DRINKING_KEY를 게임의 키 바인딩 시스템에 등록합니다.
            event.register(KeyBinding.DRINKING_KEY);
        }

        // onKeyInput 메소드는 키 입력을 처리하는 이벤트 핸들러입니다.
        // InputEvent는 키 입력이나 마우스 클릭과 같은 사용자 입력이 발생할 때마다 발생합니다.
        @SubscribeEvent
        public static void onKeyInput(InputEvent event) {
            // KeyBinding.DRINKING_KEY.consumeClick() 메소드를 호출하여
            // 사용자가 정의한 '물 마시기' 키가 눌렸는지 확인합니다.
            // consumeClick() 메소드는 키 입력을 "소비"하고, 키가 눌렸다면 true를 반환합니다.
            if (KeyBinding.DRINKING_KEY.consumeClick()) {
                // 키가 눌렸을 때 수행할 동작을 여기에 정의합니다.
                // 예제에서는 간단히 플레이어에게 시스템 메시지를 보냅니다.
//                Minecraft.getInstance().player.sendSystemMessage(
//                        Component.literal("Pressed a Drinking Key"));
                ModMessages.sendToServer(new DrinkWaterC2SPacket());
            }
        }
    }

    // ClientModBusEvents는 모드 이벤트 버스에 등록된 클라이언트 측 이벤트를 처리하는 내부 클래스입니다.
    // bus = Mod.EventBusSubscriber.Bus.MOD를 통해 모드 이벤트 버스에 등록됨을 나타냅니다.
    @Mod.EventBusSubscriber(modid = TutorialMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        // onKeyRegister 메소드는 모드 이벤트 버스에서 키 바인딩을 등록하는 데 사용됩니다.
        // 이 메소드는 ClientForgeEvents 내부 클래스의 onKeyRegister 메소드와 유사하게 작동합니다.
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            // 키 바인딩을 등록합니다.
            event.register(KeyBinding.DRINKING_KEY);
        }

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("thirst", ThirstHudOverlay.HUD_THIRST);
        }
    }
}
