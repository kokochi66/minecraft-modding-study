package net.kokochi.kkotycoon.packet;


import net.kokochi.kkotycoon.KkoTycoon;
import net.kokochi.kkotycoon.client.data.CodexSet;
import net.minecraft.network.PacketByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodexC2SPostPacket {
    public static final Logger LOGGER = LoggerFactory.getLogger(KkoTycoon.MOD_ID);
    private final CodexSet codexSet;
    public static final String CODEX_POST_PACKET_REQUEST_ID = "codex_post_request";
    public static final String CODEX_POST_REWARD_REQUEST_ID = "codex_post_reward_request";
    public CodexC2SPostPacket(CodexSet codexSet) {
        this.codexSet = codexSet;
    }

    public static void encode(CodexC2SPostPacket packet, PacketByteBuf buf) {
        CodexSet[] codexSetArray = CodexSet.values();
        for (int i = 0; i < codexSetArray.length; i++) {
            if (codexSetArray[i] == packet.codexSet) {
                buf.writeInt(i);
            }
        }
    }

    public static int decode(PacketByteBuf buf) {
        return buf.readInt();
    }

    public CodexSet getCodexSet() {
        return codexSet;
    }
}
