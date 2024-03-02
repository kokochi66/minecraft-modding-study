package net.kokochi.kkotycoon.entity.product;

import net.minecraft.item.Item;

public class KkoShopProduct {
    // 상품 품목 데이터
    private long productId;
    private Item item;
    private long price;

    public KkoShopProduct(long productId, Item item, long price) {
        this.productId = productId;
        this.item = item;
        this.price = price;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
