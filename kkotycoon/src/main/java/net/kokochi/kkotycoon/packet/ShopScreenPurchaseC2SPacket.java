package net.kokochi.kkotycoon.packet;

import net.minecraft.network.PacketByteBuf;

public class ShopScreenPurchaseC2SPacket {
    public static final String SHOP_PURCHASE_SCREEN_REQUEST_ID = "shop_purchase_screen_request";
    private long productId;

    public ShopScreenPurchaseC2SPacket(long productId) {
        this.productId = productId;
    }



    public static void encode(ShopScreenPurchaseC2SPacket packet, PacketByteBuf buf) {
        buf.writeLong(packet.productId);
    }

    public static long decode(PacketByteBuf buf) {
        return buf.readLong();
    }
}
