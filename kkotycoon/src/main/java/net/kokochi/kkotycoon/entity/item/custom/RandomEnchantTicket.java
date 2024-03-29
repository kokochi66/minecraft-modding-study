package net.kokochi.kkotycoon.entity.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static net.minecraft.enchantment.Enchantments.*;

// 랜덤 인챈트북 뽑기권
public class RandomEnchantTicket extends Item {
    private final Random random = new Random();
    public RandomEnchantTicket(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(Text.of("§9확률은 비밀입니다. 꼬우면 아시죠?"));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            ItemStack ticketStack = user.getStackInHand(hand);
            if (user.getInventory().getEmptySlot() == -1) {
                user.sendMessage(Text.of("인벤토리 창이 꽉 차있습니다."));
                return TypedActionResult.fail(ticketStack);
            }
            ItemStack offHandStack = user.getOffHandStack();

            // 인챈트북을 생성하고 랜덤 인챈트를 적용한다.
            Enchantment enchantment = pickRandomEnchantment();
            int level = enchantment.getMaxLevel();

            Map<Enchantment, Integer> enchantmentIntegerMap = new HashMap<>();
            enchantmentIntegerMap.put(enchantment, level);

            ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantmentHelper.set(enchantmentIntegerMap, book);
//            book.addEnchantment(enchantment, level);

            String tier = "";
            if (SS_TIER.stream().anyMatch(e -> e == enchantment)) {
                tier = "§cSS";

                for (ServerWorld serverWorld : world.getServer().getWorlds()) {
                    for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                        player.sendMessage(Text.of("§6" + user.getName().getString() + "§f 님께서 "  + tier + "§f 티어의 \"§9" + enchantment.getName(level).getString() + "§f\" 를 획득하셨습니다!"));
                        player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER, 1.0F, 1.0F);
                    }
                }
            } else {
                if (S_TIER.stream().anyMatch(e -> e == enchantment)) {
                    tier = "§4S";
                } else if (A_TIER.stream().anyMatch(e -> e == enchantment)) {
                    tier = "§5A";
                } else if (B_TIER.stream().anyMatch(e -> e == enchantment)) {
                    tier = "§eB";
                } else if (C_TIER.stream().anyMatch(e -> e == enchantment)) {
                    tier = "§dC";
                }
                user.sendMessage(Text.of(tier + "§f 티어의 \"§9" + enchantment.getName(level).getString() + "§f\" 를 획득하셨습니다!"));
            }

            // 인챈트 북 획득 시 사용자에게 어떤 인챈트북을 획득했는지 메세지를 보낸다.
            user.getInventory().insertStack(book);
            // 아이템 사용 시 아이템을 소모한다.
            ticketStack.decrement(1);
            return TypedActionResult.success(ticketStack, world.isClient());
        } else {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }
    }

    private Enchantment pickRandomEnchantment() {
        double chance = random.nextDouble() * 100;
        if (chance < 0.293) {
            return pickEnchantmentFromTier(SS_TIER); // SS티어
        } else if (chance < 6.516) {
            return pickEnchantmentFromTier(S_TIER); // S티어
        } else if (chance < 25.85) {
            return pickEnchantmentFromTier(A_TIER); // A티어
        } else if (chance < 71.609) {
            return pickEnchantmentFromTier(B_TIER); // B티어
        } else {
            return pickEnchantmentFromTier(C_TIER); // C티어
        }
    }

    private Enchantment pickEnchantmentFromTier(List<Enchantment> tier) {
        return tier.get(random.nextInt(tier.size()));
    }

    private static final List<Enchantment> SS_TIER = List.of(
            Enchantments.MENDING // 수선
    );

    private static final List<Enchantment> S_TIER = List.of(
            Enchantments.LOOTING, // 약탈
            Enchantments.EFFICIENCY, // 효율
            Enchantments.FORTUNE, // 행운
            Enchantments.UNBREAKING, // 내구성
            Enchantments.LOYALTY, // 충성
            Enchantments.RIPTIDE, // 급류
            Enchantments.CHANNELING // 집전
    );

    private static final List<Enchantment> A_TIER = List.of(
            Enchantments.FEATHER_FALLING, // 가벼운 착지
            Enchantments.PROTECTION, // 보호
            Enchantments.SHARPNESS, // 날카로움
            Enchantments.SMITE, // 강타
            Enchantments.SILK_TOUCH, // 섬세한 손길
            Enchantments.INFINITY, // 무한
            Enchantments.LUCK_OF_THE_SEA, // 바다의 행운
            Enchantments.LURE // 미끼
    );

    private static final List<Enchantment> B_TIER = List.of(
            Enchantments.FIRE_PROTECTION, // 화염으로부터 보호
            Enchantments.BLAST_PROTECTION, // 폭발로부터 보호
            Enchantments.PROJECTILE_PROTECTION, // 발사체로부터 보호
            Enchantments.RESPIRATION, // 친수성
            Enchantments.AQUA_AFFINITY, // 물갈퀴
            Enchantments.DEPTH_STRIDER, // 신속한 잠행
            Enchantments.BANE_OF_ARTHROPODS, // 살충
            Enchantments.FLAME, // 발화
            Enchantments.POWER, // 힘
            Enchantments.FIRE_ASPECT, // 화염
            Enchantments.IMPALING, // 찌르기
            Enchantments.MULTISHOT, // 다중발사
            Enchantments.QUICK_CHARGE // 빠른장전
    );

    private static final List<Enchantment> C_TIER = List.of(
            Enchantments.THORNS, // 가시
            Enchantments.FROST_WALKER, // 차가운 걸음
            Enchantments.SOUL_SPEED, // 영혼가속
            Enchantments.KNOCKBACK, // 밀치기
            Enchantments.SWEEPING // 휩쓸기
    );
}
