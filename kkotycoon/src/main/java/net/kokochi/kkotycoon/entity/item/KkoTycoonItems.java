package net.kokochi.kkotycoon.entity.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.kokochi.kkotycoon.KkoTycoon;
import net.kokochi.kkotycoon.entity.item.custom.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class KkoTycoonItems {

    public static final Item KKO_COIN = registerItem("kko_coin",
            new KkoCoin(new FabricItemSettings()
                    .maxCount(1)
            ));
    public static final Item RANDOM_ENCHANT_TICKET = registerItem("random_enchant_ticket",
            new RandomEnchantTicket(new FabricItemSettings()
                    .maxCount(64)
            ));
    public static final Item ITEM_TOOL_BOX = registerItem("item_tool_box",
            new ItemToolBox(new FabricItemSettings()
                    .maxCount(64)
            ));
    public static final Item HOME_SCROLL = registerItem("home_scroll",
            new HomeScroll(new FabricItemSettings()
                    .maxCount(64)
            ));
    public static final Item ITEM_EFFECT_SCROLL = registerItem("item_eff_scroll",
            new ItemEffectScroll(new FabricItemSettings()
                    .maxCount(64)
            ));

    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(KkoTycoon.MOD_ID, name), item);
    }

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
        entries.add(RANDOM_ENCHANT_TICKET);
        entries.add(ITEM_TOOL_BOX);
        entries.add(HOME_SCROLL);
        entries.add(ITEM_EFFECT_SCROLL);
    }

    public static void initModItems() {
        KkoTycoon.LOGGER.info("Registering Item");
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(KkoTycoonItems::addItemsToIngredientItemGroup);
    }
}
