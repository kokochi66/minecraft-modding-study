package net.kokochi.tutorialmod.networking;


import net.kokochi.tutorialmod.TutorialMod;
import net.kokochi.tutorialmod.networking.packet.DrinkWaterC2SPacket;
import net.kokochi.tutorialmod.networking.packet.ExampleC2SPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    // SimpleChannel 인스턴스는 모드의 네트워크 채널을 나타냅니다. 이 채널을 통해 패킷을 송수신합니다.
    private static SimpleChannel INSTANCE;

    // 패킷 ID를 관리하는 변수로, 각 패킷에 고유 ID를 할당하는 데 사용됩니다.
    private static int packetId = 0;
    // 패킷 ID를 증가시키고 반환하는 도우미 메소드입니다.
    private static int id() {
        return packetId++;
    }

    // 네트워크 메시지(패킷)를 등록하는 메소드입니다. 이 메소드는 모드의 초기화 과정에서 호출됩니다.
    public static void register() {
        // 네트워크 채널을 생성하고 구성합니다.
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(TutorialMod.MOD_ID, "messages")) // 채널의 고유 이름을 설정합니다.
                .networkProtocolVersion(() -> "1.0") // 네트워크 프로토콜 버전을 설정합니다.
                .clientAcceptedVersions(s -> true) // 클라이언트 호환성을 확인하는 로직입니다.
                .serverAcceptedVersions(s -> true) // 서버 호환성을 확인하는 로직입니다.
                .simpleChannel(); // SimpleChannel 인스턴스를 생성합니다.

        INSTANCE = net; // 생성된 네트워크 채널 인스턴스를 저장합니다.

        // ExampleC2SPacket을 서버로 보내는 패킷으로 등록합니다.
        net.messageBuilder(ExampleC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ExampleC2SPacket::new) // 패킷 데이터를 해석하는 디코더를 설정합니다.
                .encoder(ExampleC2SPacket::toBytes) // 패킷 데이터를 바이트로 인코딩하는 인코더를 설정합니다.
                .consumerMainThread(ExampleC2SPacket::handle) // 패킷을 처리하는 핸들러 메소드를 설정합니다.
                .add(); // 패킷을 채널에 등록합니다.

        // DrinkWaterC2SPacket을 서버로 보내는 패킷으로 등록합니다.
        net.messageBuilder(DrinkWaterC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(DrinkWaterC2SPacket::new) // 패킷 데이터를 해석하는 디코더를 설정합니다.
                .encoder(DrinkWaterC2SPacket::toBytes) // 패킷 데이터를 바이트로 인코딩하는 인코더를 설정합니다.
                .consumerMainThread(DrinkWaterC2SPacket::handle) // 패킷을 처리하는 핸들러 메소드를 설정합니다.
                .add(); // 패킷을 채널에 등록합니다.
    }

    // 서버로 메시지를 보내는 메소드입니다. 이 메소드는 클라이언트 측에서 호출됩니다.
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    // 특정 플레이어에게 메시지를 보내는 메소드입니다. 이 메소드는 서버 측에서 호출됩니다.
    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}