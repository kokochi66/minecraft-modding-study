package net.kokochi.kkotycoon.entity.product;

import net.kokochi.kkotycoon.entity.item.KkoTycoonItems;

import java.util.Arrays;
import java.util.List;

public class KkoShopProductDataManager {
    public static final List<KkoShopProduct> PRODUCT_LIST =
            Arrays.asList(
                    // 1페이지
                    new KkoShopProduct(0L, KkoTycoonItems.ITEM_EFFECT_SCROLL, 10000L),
                    new KkoShopProduct(1L, KkoTycoonItems.RANDOM_ENCHANT_TICKET, 10000L),
                    new KkoShopProduct(2L, KkoTycoonItems.ITEM_TOOL_BOX, 5000L),
                    new KkoShopProduct(3L, KkoTycoonItems.HOME_SCROLL, 100L),

                    // 2페이지
                    new KkoShopProduct(4L, KkoTycoonItems.INVENTORY_SAVE_TICKET, 50000L),
                    new KkoShopProduct(5L, KkoTycoonItems.GUARD_TICKET, 150000L)
            );
}
