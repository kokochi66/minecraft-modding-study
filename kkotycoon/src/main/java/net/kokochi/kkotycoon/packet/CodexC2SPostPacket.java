package net.kokochi.kkotycoon.packet;


import net.kokochi.kkotycoon.KkoTycoon;
import net.minecraft.network.PacketByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodexC2SPostPacket {
    public static final Logger LOGGER = LoggerFactory.getLogger(KkoTycoon.MOD_ID);
    private final int itemId;
    public static final String CODEX_POST_PACKET_REQUEST_ID = "codex_post_request";
    public static final String CODEX_POST_REWARD_REQUEST_ID = "codex_post_reward_request";
    public CodexC2SPostPacket(int itemId) {
        this.itemId = itemId;
    }

    public static void encode(CodexC2SPostPacket packet, PacketByteBuf buf) {
        buf.writeInt(packet.itemId);
    }

    public static int decode(PacketByteBuf buf) {
        return buf.readInt();
    }
}
