package net.kokochi.kkotycoon.entity.product;

import net.kokochi.kkotycoon.entity.item.KkoTycoonItems;

import java.util.Arrays;
import java.util.List;

public class KkoShopProductDataManager {
    public static final List<KkoShopProduct> PRODUCT_LIST =
            Arrays.asList(
                    new KkoShopProduct(0L, KkoTycoonItems.ITEM_EFFECT_SCROLL, 50000L),
                    new KkoShopProduct(1L, KkoTycoonItems.RANDOM_ENCHANT_TICKET, 10000L),
                    new KkoShopProduct(2L, KkoTycoonItems.ITEM_TOOL_BOX, 5000L),
                    new KkoShopProduct(3L, KkoTycoonItems.HOME_SCROLL, 100L)
            );
}
