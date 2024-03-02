package net.kokochi.kkotycoon.entity.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


import java.util.List;

import static net.kokochi.kkotycoon.server.handler.PlayerActionEventHandler.*;

// 아이템 강화 주문서
public class ItemEffectScroll extends Item {
    private final Random random = Random.create();
    public ItemEffectScroll(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.of("§9도구에 특별한 효과를 부여합니다."));
        tooltip.add(Text.of("§c사용방법 (필독)"));
        tooltip.add(Text.of("§6오른손 §f: 아이템 강화 주문서를 듭니다."));
        tooltip.add(Text.of("§6왼손 §f: 강화하고자 하는 장비를 듭니다."));
        tooltip.add(Text.of("§f마우스 우클릭을 누릅니다."));
        tooltip.add(Text.of("§e곡괭이, 괭이, 검, 활 에만 사용이 가능합니다."));
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            ItemStack scrollStack = user.getStackInHand(hand);
            ItemStack toolStack = user.getOffHandStack();
            Item toolItem = toolStack.getItem();

            if (toolItem instanceof PickaxeItem || toolItem instanceof HoeItem ||
                    toolItem instanceof SwordItem || toolItem instanceof BowItem) {

                int level = getEnhancementLevel(toolItem); // 강화 레벨
                double weight = Math.round(getEnhancementWeight() * 100.0) / 100.0; // 강화 확률

                // NBT 데이터에 강화 정보 저장
                NbtCompound tag = toolStack.getOrCreateNbt();
                String levelKey = getLevelKey(toolItem);
                String weightKey = getWeightKey(toolItem);

                tag.putInt(levelKey, level);
                tag.putDouble(weightKey, weight);
                // 아이템 사용 후 소모
                scrollStack.decrement(1);

                // 최고 수준 등급 아이템을 획득했으면 전 서버에 확성기
                if (getLevelValue(toolItem, 3) <= level && weight >= 0.4d){
                    for (ServerPlayerEntity player : ((ServerWorld) world).getPlayers()) {
                        player.sendMessage(Text.literal("§6" + user.getName().getString() + "§f 님께서 '§a" +
                                toolItem.getName().getString() + "§f:" + getMessage(toolItem, level, (int) (weight * 100)) + "' §f를 획득하셨습니다!"));
                        player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER, 1.0F, 1.0F);
                    }
                } else {
                    // 아니면 해당 유저에게만 표기
                    user.sendMessage(Text.literal("§a" +
                            toolItem.getName().getString() + "§f:" + getMessage(toolItem, level, (int) (weight * 100))));
                    user.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.MASTER, 1.0F, 1.0F);

                }
                return TypedActionResult.success(scrollStack, world.isClient());
            } else {
                user.sendMessage(Text.literal("이 도구는 강화할 수 없습니다."), true);
                return TypedActionResult.fail(scrollStack);
            }
        }

        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    private int getEnhancementLevel(Item item) {
        int chance = random.nextInt(100);
        if (chance < 45) return getLevelValue(item, 1); // 60% 확률로 1레벨
        else if (chance < 80) return getLevelValue(item, 2); // 추가 35% 확률로 2레벨
        else return getLevelValue(item, 3); // 나머지 5% 확률로 3레벨
    }

    private double getEnhancementWeight() {
        int chance = random.nextInt(100);
        if (chance < 30) return (random.nextInt(9) + 1d) * 0.01d;
        else if (chance < 55) return (random.nextInt(10) + 11d) * 0.01d;
        else if (chance < 75) return (random.nextInt(10) + 21d) * 0.01d;
        else if (chance < 90) return (random.nextInt(10) + 31d) * 0.01d;
        else return (random.nextInt(10) + 41d) * 0.01d;
    }

    private String getLevelKey(Item item) {
        if (item instanceof PickaxeItem) return PICKAXE_EFF_LEVEL_KEY;
        else if (item instanceof HoeItem) return HOE_EFF_LEVEL_KEY;
        else if (item instanceof SwordItem) return SWORD_EFF_LEVEL_KEY;
        else if (item instanceof BowItem) return BOW_EFF_LEVEL_KEY;
        return "";
    }

    private int getLevelValue(Item item, int level) {
        if (item instanceof PickaxeItem) return level;
        else if (item instanceof HoeItem) return level * 2;
        else if (item instanceof SwordItem) return level == 1 ? 15 : level == 2 ? 25 : 45;
        else if (item instanceof BowItem) return level == 1 ? 15 : level == 2 ? 25 : 45;
        return 0;
    }

    private String getWeightKey(Item item) {
        if (item instanceof PickaxeItem) return PICKAXE_EFF_WEIGHT_KEY;
        else if (item instanceof HoeItem) return HOE_EFF_WEIGHT_KEY;
        else if (item instanceof SwordItem) return SWORD_EFF_WEIGHT_KEY;
        else if (item instanceof BowItem) return BOW_EFF_WEIGHT_KEY;
        return "";
    }

    private String getMessage(Item item, int level, int chance) {
        if (item instanceof PickaxeItem) return "광물 블럭 채굴 시 §4" + chance + "%§f 확률로 주괴 아이템 §2" + level + "개 §f추가 드랍";
        else if (item instanceof HoeItem) return "농작물 수확 시 §4" + chance + "%§f 확률로 농작물 아이템 §2" + level + "개 §f추가 드랍";
        else if (item instanceof SwordItem) return "검으로 공격 시 §4" + chance + "%§f 확률로 치명타 데미지 §2" + level + "%§f 증가";
        else if (item instanceof BowItem) return "활로 공격 시 §4" + chance + "%§f 확률로 데미지 §2" + level + "%§f 증가";
        return "";
    }
}
