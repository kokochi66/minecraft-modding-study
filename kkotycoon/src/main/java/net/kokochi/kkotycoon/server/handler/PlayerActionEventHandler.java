package net.kokochi.kkotycoon.server.handler;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class PlayerActionEventHandler {
    public static final String PICKAXE_EFF_LEVEL_KEY = "kkotycoon_pickaxe_eff_level";
    public static final String PICKAXE_EFF_WEIGHT_KEY = "kkotycoon_pickaxe_eff_weight";
    public static final String HOE_EFF_LEVEL_KEY = "kkotycoon_hoe_eff_level";
    public static final String HOE_EFF_WEIGHT_KEY = "kkotycoon_hoe_eff_weight";
    public static final String SWORD_EFF_LEVEL_KEY = "kkotycoon_sword_eff_level";
    public static final String SWORD_EFF_WEIGHT_KEY = "kkotycoon_sword_eff_weight";
    public static final String BOW_EFF_LEVEL_KEY = "kkotycoon_bow_eff_level";
    public static final String BOW_EFF_WEIGHT_KEY = "kkotycoon_bow_eff_weight";

    public static void initPlayerActionClientEvent() {

        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            if (stack.hasNbt()) {
                NbtCompound tag = stack.getNbt();
                if (tag.contains(PICKAXE_EFF_LEVEL_KEY)) {
                    int level = tag.getInt(PICKAXE_EFF_LEVEL_KEY);
                    double chance = tag.getDouble(PICKAXE_EFF_WEIGHT_KEY) * 100;
                    lines.add(Text.literal("광물 블럭 채굴 시 §4" + chance + "§f 확률로 주괴 아이템 §2" + level + "개 §f추가 드랍"));
                } else if (tag.contains(HOE_EFF_LEVEL_KEY)) {
                    int level = tag.getInt(HOE_EFF_LEVEL_KEY);
                    double chance = tag.getDouble(HOE_EFF_WEIGHT_KEY) * 100;
                    lines.add(Text.literal("농작물 수확 시 §4" + chance + "§f 확률로 농작물 아이템 §2" + level + "개 §f추가 드랍"));
                } else if (tag.contains(SWORD_EFF_LEVEL_KEY)) {
                    int level = tag.getInt(SWORD_EFF_LEVEL_KEY);
                    double chance = tag.getDouble(SWORD_EFF_WEIGHT_KEY) * 100;
                    lines.add(Text.literal("검으로 공격 시 §4" + chance + "§f 확률로 치명타 데미지 §2" + level + "% §f 증가"));
                } else if (tag.contains(BOW_EFF_LEVEL_KEY)) {
                    int level = tag.getInt(BOW_EFF_LEVEL_KEY);
                    double chance = tag.getDouble(BOW_EFF_WEIGHT_KEY) * 100;
                    lines.add(Text.literal("활로 공격 시 §4" + chance + "§f 확률로 데미지 §2" + level + "% §f 증가"));
                }
            }
        });


    }
}
