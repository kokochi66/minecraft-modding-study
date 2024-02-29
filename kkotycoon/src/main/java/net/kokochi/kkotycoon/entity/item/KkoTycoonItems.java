package net.kokochi.kkotycoon.entity.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.kokochi.kkotycoon.KkoTycoon;
import net.kokochi.kkotycoon.entity.item.custom.KkoCoin;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class KkoTycoonItems {

    public static final Item KkoCoin = registerItem("kko_coin",
            new KkoCoin(new FabricItemSettings()
                    .maxCount(1)
            ));

    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(KkoTycoon.MOD_ID, name), item);
    }

    public static void initModItems() {
        KkoTycoon.LOGGER.info("Registering Item");
    }
}
