package net.kokochi.kkotycoon.entity.codex;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

// 도감 순서
public enum CodexSet {
    STONE(Items.STONE, 10),     // 돌
    GRANITE(Items.GRANITE,10),  // 화강암
    DIORITE(Items.DIORITE,10),  // 섬록암
    ANDESITE(Items.ANDESITE,10),    // 안산암
    DEEPSLATE(Items.DEEPSLATE,10),  // 심층암

    CALCITE(Items.CALCITE,10),  // 방해석
    TUFF(Items.TUFF,10),    // 응회암
    DRIPSTONE_BLOCK(Items.DRIPSTONE_BLOCK,10),  // 점적석 블록
    MOSSY_COBBLESTONE(Items.MOSSY_COBBLESTONE,10),  // 이끼 낀 조약돌
    GRASS_BLOCK(Items.GRASS_BLOCK,10),      // 잔디 블록

    COARSE_DIRT(Items.COARSE_DIRT,10),      // 거친 흙
    PODZOL(Items.PODZOL,10),                // 회백토
    MUD(Items.MUD,10),                      // 진흙
    WARPED_NYLIUM(Items.WARPED_NYLIUM,10),      // 뒤틀린 네사체
    OAK_SAPLING(Items.OAK_SAPLING,10),      // 참나무 묘목

    SPRUCE_SAPLING(Items.SPRUCE_SAPLING,10),           // 가문비 나무 묘목
    BIRCH_SAPLING(Items.BIRCH_SAPLING,10),             // 자작 나무 묘목
    JUNGLE_SAPLING(Items.JUNGLE_SAPLING,10),           // 정글 나무 묘목
    ACACIA_SAPLING(Items.ACACIA_SAPLING,10),           // 아카시아 나무 묘목
    CHERRY_SAPLING(Items.CHERRY_SAPLING,10),           // 벚나무 묘목

    DARK_OAK_SAPLING(Items.DARK_OAK_SAPLING,10),       // 짙은 참나무 묘목
    MANGROVE_PROPAGULE(Items.MANGROVE_PROPAGULE,10),   // 맹그로브 나무 주아
    SAND(Items.SAND,10),       // 모래
    RED_SAND(Items.RED_SAND,10),       // 붉은 모래
    GRAVEL(Items.GRAVEL,10),           // 자갈

    COAL_ORE(Items.COAL_ORE,10),                   // 석탄 광석
    IRON_ORE(Items.IRON_ORE,10),                   // 철 광석
    COPPER_ORE(Items.COPPER_ORE,10),               // 구리 광석
    GOLD_ORE(Items.GOLD_ORE,10),                   // 금 광석
    EMERALD_ORE(Items.EMERALD_ORE,5),             // 에메랄드 광석

    DEEPSLATE_REDSTONE_ORE(Items.DEEPSLATE_REDSTONE_ORE,10),       // 심층암 레드스톤 광석
    DEEPSLATE_LAPIS_ORE(Items.DEEPSLATE_LAPIS_ORE,10),             // 심층암 청금석 광석
    DEEPSLATE_DIAMOND_ORE(Items.DEEPSLATE_DIAMOND_ORE,10),         // 심층암 다이아몬드 광석
    NETHER_GOLD_ORE(Items.NETHER_GOLD_ORE,10),     // 네더 금 광석
    NETHER_QUARTZ_ORE(Items.NETHER_QUARTZ_ORE,10),     // 네더 석영 광석

    ANCIENT_DEBRIS(Items.ANCIENT_DEBRIS,10),       // 고대 잔해
    AMETHYST_BLOCK(Items.AMETHYST_BLOCK,10),        //  자수정 블록
    OXIDIZED_COPPER(Items.OXIDIZED_COPPER,10),     // 산화된 구리
    IRON_BLOCK(Items.IRON_BLOCK,10),    // 철 블록
    GOLD_BLOCK(Items.GOLD_BLOCK,10),    // 금 블록

    COAL_BLOCK(Items.COAL_BLOCK,10),        // 석탄 블록
    LAPIS_BLOCK(Items.LAPIS_BLOCK,10),      // 청금석 블록
    REDSTONE_BLOCK(Items.REDSTONE_BLOCK,10),        // 레드스톤 블록
    DIAMOND_BLOCK(Items.DIAMOND_BLOCK,10),  // 다이아몬드 블록
    EMERALD_BLOCK(Items.EMERALD_BLOCK,10),      // 에메랄드 블록

    WHITE_CONCRETE(Items.WHITE_CONCRETE,10),            // 콘크리트
    OAK_LOG(Items.OAK_LOG,10),      // 참나무 원목
    SPRUCE_LOG(Items.SPRUCE_LOG,10),        // 가문비나무 원목
    BIRCH_LOG(Items.BIRCH_LOG,10),      // 자작나무 원목
    JUNGLE_LOG(Items.JUNGLE_LOG,10),        // 정글나무 원목

    ACACIA_LOG(Items.ACACIA_LOG,10),        // 아카시아나무 원목
    CHERRY_LOG(Items.CHERRY_LOG,10),    // 벚나무 원목
    DARK_OAK_LOG(Items.DARK_OAK_LOG,10),    // 짙은 참나무 원목
    MANGROVE_LOG(Items.MANGROVE_LOG,10),            // 맹그로브 나무 원목
    MANGROVE_ROOTS(Items.MANGROVE_ROOTS,10),            // 맹그로브 나무 뿌리

    CRIMSON_STEM(Items.CRIMSON_STEM,10),           // 진홍빛 자루
    WARPED_STEM(Items.WARPED_STEM,10),             // 뒤틀린 자루
    BAMBOO_BLOCK(Items.BAMBOO_BLOCK,10),           // 대나무 블록
    FLOWERING_AZALEA_LEAVES(Items.FLOWERING_AZALEA_LEAVES,10),     // 꽃 핀 진달래 잎
    SPONGE(Items.SPONGE,5),            // 스펀지

    GLASS(Items.GLASS,10),                 // 유리
    COBWEB(Items.COBWEB,10),            // 거미줄
    GRASS(Items.GRASS,10),          // 잔디
    FERN(Items.FERN,10),             // 고사리
    DEAD_BUSH(Items.DEAD_BUSH,10),      // 마른 덤불

    SEAGRASS(Items.SEAGRASS,10),            // 해초
    SEA_PICKLE(Items.SEA_PICKLE,10),            // 불우렁쉥이
    WHITE_WOOL(Items.WHITE_WOOL,10),            // 하얀색 양털
    DANDELION(Items.DANDELION,5),      // 민들레
    POPPY(Items.POPPY,5),      // 양귀비

    BLUE_ORCHID(Items.BLUE_ORCHID,5),      // 파란색 난초
    ALLIUM(Items.ALLIUM,5),        // 알리움
    AZURE_BLUET(Items.AZURE_BLUET,5),      // 선애기별꽃
    RED_TULIP(Items.RED_TULIP,5),      // 빨간색 튤립
    ORANGE_TULIP(Items.ORANGE_TULIP,5),            // 주황색 튤립

    WHITE_TULIP(Items.WHITE_TULIP,5),              // 하얀색 튤립
    PINK_TULIP(Items.PINK_TULIP,5),         // 분홍색 튤립
    OXEYE_DAISY(Items.OXEYE_DAISY,5),         // 데이지
    CORNFLOWER(Items.CORNFLOWER,5),           // 수레국화
    LILY_OF_THE_VALLEY(Items.LILY_OF_THE_VALLEY,5),       // 은방울꽃

    WITHER_ROSE(Items.WITHER_ROSE,5), // 위더 장미
    PITCHER_PLANT(Items.PITCHER_PLANT,5),       // 벌레잡이 풀
    SPORE_BLOSSOM(Items.SPORE_BLOSSOM,5),               // 포자꽃
    BROWN_MUSHROOM(Items.BROWN_MUSHROOM, 10),       // 갈색 버섯
    RED_MUSHROOM(Items.RED_MUSHROOM, 10),           // 빨간색 버섯

    CRIMSON_FUNGUS(Items.CRIMSON_FUNGUS, 10),   // 진홍빛 균
    WARPED_FUNGUS(Items.WARPED_FUNGUS, 10),     // 뒤틀린 균
    CRIMSON_ROOTS(Items.CRIMSON_ROOTS, 10),     // 진홍빛 뿌리
    WARPED_ROOTS(Items.WARPED_ROOTS, 10),   // 뒤틀린 뿌리
    NETHER_SPROUTS(Items.NETHER_SPROUTS, 10),       // 네더 싹

    SUGAR_CANE(Items.SUGAR_CANE, 10),       // 사탕 수수
    KELP(Items.KELP, 10),               //  켈프
    HANGING_ROOTS(Items.HANGING_ROOTS, 10),     // 매달린 뿌리
    BRICKS(Items.BRICKS, 10),       // 벽돌
    BOOKSHELF(Items.BOOKSHELF, 10),     // 책장

    OBSIDIAN(Items.OBSIDIAN, 10),           // 흑요석
    END_ROD(Items.END_ROD, 10),         // 엔드 막대기
    CHORUS_FLOWER(Items.CHORUS_FLOWER, 10),     // 후렴화
    ICE(Items.ICE, 10),     // 얼음
    SNOW_BLOCK(Items.SNOW_BLOCK, 10),       // 눈 블록

    PACKED_ICE(Items.PACKED_ICE, 10),           // 꽁꽁 언 얼음
    CACTUS(Items.CACTUS, 10),       // 선인장
    CLAY(Items.CLAY, 10),           // 점토
    JUKEBOX(Items.JUKEBOX, 10),     // 주크박스
    JACK_O_LANTERN(Items.JACK_O_LANTERN, 10),       // 잭 오 랜턴

    DAMAGED_ANVIL(Items.DAMAGED_ANVIL, 10),         // 손상된 모루
    TERRACOTTA(Items.TERRACOTTA, 10),           // 테라코타
    HAY_BLOCK(Items.HAY_BLOCK, 10),         // 건초더미
    SUNFLOWER(Items.SUNFLOWER, 5),     // 해바라기
    LILAC(Items.LILAC, 5),              // 라일락

    ROSE_BUSH(Items.ROSE_BUSH, 5),      // 장미 덤불
    PEONY(Items.PEONY, 5),              // 모란
    PRISMARINE(Items.PRISMARINE, 10),       // 프리즈마린
    SEA_LANTERN(Items.SEA_LANTERN, 10),         // 바다 랜턴
    MAGMA_BLOCK(Items.MAGMA_BLOCK, 10),         // 마그마 블록

    DEAD_TUBE_CORAL_BLOCK(Items.DEAD_TUBE_CORAL_BLOCK, 10),     // 죽은 관 산호 블록
    TUBE_CORAL_BLOCK(Items.TUBE_CORAL_BLOCK, 10),               // 관 산호 블록
    TUBE_CORAL(Items.TUBE_CORAL, 10),                       // 관 산호
    BRAIN_CORAL(Items.BRAIN_CORAL, 10),                     // 뇌 산호
    BUBBLE_CORAL(Items.BUBBLE_CORAL, 10),                   // 거품 산호

    FIRE_CORAL(Items.FIRE_CORAL, 10),                   // 불 산호
    HORN_CORAL(Items.HORN_CORAL, 10),                   // 사방 산호
    SCAFFOLDING(Items.SCAFFOLDING, 10),                 // 비계
    REPEATER(Items.REPEATER, 10),                       // 레드스톤 중계기
    COMPARATOR(Items.COMPARATOR, 10),                   // 레드스톤 비교기

    PISTON(Items.PISTON, 10),                   // 피스톤
    STICKY_PISTON(Items.STICKY_PISTON, 10),     // 끈끈이 피스톤
    SLIME_BLOCK(Items.SLIME_BLOCK, 10),         // 슬라임 블록
    HONEY_BLOCK(Items.HONEY_BLOCK, 10),         // 꿀 블록
    OBSERVER(Items.OBSERVER, 10),               // 관측기

    HOPPER(Items.HOPPER, 10),           // 호퍼
    DISPENSER(Items.DISPENSER, 10),     // 발사기
    DROPPER(Items.DROPPER, 10),     // 공급기
    LECTERN(Items.LECTERN, 10),     // 독서대
    TARGET(Items.TARGET, 10),           // 과녁

    LIGHTNING_ROD(Items.LIGHTNING_ROD, 10),             // 피뢰침
    DAYLIGHT_DETECTOR(Items.DAYLIGHT_DETECTOR, 10),     // 햇빛 감지기
    SCULK_SENSOR(Items.SCULK_SENSOR, 10),           // 스컬크 감지체
    SCULK_CATALYST(Items.SCULK_CATALYST, 10),           // 스컬크 촉매
    SCULK_SHRIEKER(Items.SCULK_SHRIEKER, 10),           // 스컬크 비명체

    TRIPWIRE_HOOK(Items.TRIPWIRE_HOOK, 10),         // 철사 덫 갈고리
    TNT(Items.TNT, 10),                     // TNT
    REDSTONE_LAMP(Items.REDSTONE_LAMP, 10),     // 레드스톤 조명
    NOTE_BLOCK(Items.NOTE_BLOCK, 10),       // 소리 블록
    CARROT_ON_A_STICK(Items.CARROT_ON_A_STICK, 10),     // 당근 낚시대

    WARPED_FUNGUS_ON_A_STICK(Items.WARPED_FUNGUS_ON_A_STICK, 10),           // 뒤틀린 균 낚시대
    ELYTRA(Items.ELYTRA, 1),               // 겉날개
    MUSHROOM_STEW(Items.MUSHROOM_STEW, 10),     // 버섯 스튜
    STRING(Items.STRING, 10),           // 실
    FEATHER(Items.FEATHER, 10),         // 깃털

    LEATHER(Items.LEATHER, 10),         // 가죽
    GUNPOWDER(Items.GUNPOWDER, 10),     // 화약
    PORKCHOP(Items.PORKCHOP, 10),   // 익히지 않은 돼지고기
    PAINTING(Items.PAINTING, 10),       // 그림
    APPLE(Items.APPLE, 10),         // 사과

    GOLDEN_APPLE(Items.GOLDEN_APPLE, 10),       // 황금 사과
    ENCHANTED_GOLDEN_APPLE(Items.ENCHANTED_GOLDEN_APPLE, 3),       // 마법 부여된 황금 사과
    PUFFERFISH(Items.PUFFERFISH, 10),           // 복어
    PUFFERFISH_BUCKET(Items.PUFFERFISH_BUCKET, 10),         // 복어가 담긴 양동이
    SALMON_BUCKET(Items.SALMON_BUCKET, 10),             // 연어가 담긴 양동이

    TROPICAL_FISH_BUCKET(Items.TROPICAL_FISH_BUCKET, 10),       // 열대어가 담긴 양동이
    POWDER_SNOW_BUCKET(Items.POWDER_SNOW_BUCKET, 10),       // 가루눈이 담긴 양동이
    TADPOLE_BUCKET(Items.TADPOLE_BUCKET, 10),       // 올챙이가 담긴 양동이
    AXOLOTL_BUCKET(Items.AXOLOTL_BUCKET, 10),       // 아홀로틀이 담긴 양동이
    EGG(Items.EGG, 10),     // 계란

    COMPASS(Items.COMPASS, 10),         // 나침반
    INK_SAC(Items.INK_SAC, 10),         // 먹물 주머니
    GLOW_INK_SAC(Items.GLOW_INK_SAC, 10),       // 발광 먹물 주머니
    COCOA_BEANS(Items.COCOA_BEANS, 10),         // 코코아 콩
    BONE(Items.BONE, 10),           //  뼈다귀

    WHITE_BED(Items.WHITE_BED, 10),         // 침대
    COOKIE(Items.COOKIE, 10),           // 쿠키
    FILLED_MAP(Items.FILLED_MAP, 5),           // 지도
    SHEARS(Items.SHEARS, 10),       // 가위
    BEEF(Items.BEEF, 10),       // 익히지 않은 소고기

    CHICKEN(Items.CHICKEN, 10),     // 익히지 않은 닭고기
    ROTTEN_FLESH(Items.ROTTEN_FLESH, 10),       // 썩은 살점
    ENDER_PEARL(Items.ENDER_PEARL, 10),     // 엔더 진주
    BLAZE_ROD(Items.BLAZE_ROD, 10),     // 블레이즈 막대
    GHAST_TEAR(Items.GHAST_TEAR, 10),       // 가스트 눈물

    SPIDER_EYE(Items.SPIDER_EYE, 10),           // 거미 눈
    BLAZE_POWDER(Items.BLAZE_POWDER, 10),           // 블레이즈 가루
    MAGMA_CREAM(Items.MAGMA_CREAM, 10),         // 마그마 크림
    BREWING_STAND(Items.BREWING_STAND, 10),         // 양조기
    CAULDRON(Items.CAULDRON, 10),           // 가마솥

    ENDER_EYE(Items.ENDER_EYE, 10),         // 엔더의 눈
    GLISTERING_MELON_SLICE(Items.GLISTERING_MELON_SLICE, 10),       // 반짝이는 수박 조각
    EXPERIENCE_BOTTLE(Items.EXPERIENCE_BOTTLE, 3),      // 경험치 병
    FIRE_CHARGE(Items.FIRE_CHARGE, 10),             // 화염구
    ITEM_FRAME(Items.ITEM_FRAME, 10),           // 아이템 액자

    FLOWER_POT(Items.FLOWER_POT, 10),           // 화분
    CARROT(Items.CARROT, 10),       // 당근
    POTATO(Items.POTATO, 10),       // 감자
    POISONOUS_POTATO(Items.POISONOUS_POTATO, 10),       // 독이 든 감자
    GOLDEN_CARROT(Items.GOLDEN_CARROT, 10),     // 황금당근

    NETHER_STAR(Items.NETHER_STAR, 1),          // 네더의 별
    PUMPKIN_PIE(Items.PUMPKIN_PIE, 10),         // 호박 파이
    FIREWORK_ROCKET(Items.FIREWORK_ROCKET, 10),         // 폭죽 로켓
    ENCHANTED_BOOK(Items.ENCHANTED_BOOK, 10),       // 마법이 부여된 책
    ARMOR_STAND(Items.ARMOR_STAND, 10),     // 갑옷 거치대

    IRON_HORSE_ARMOR(Items.IRON_HORSE_ARMOR, 1),        // 철 말 갑옷
    GOLDEN_HORSE_ARMOR(Items.GOLDEN_HORSE_ARMOR, 1),        // 금 말 갑옷
    DIAMOND_HORSE_ARMOR(Items.DIAMOND_HORSE_ARMOR, 1),      // 다이아 말 갑옷
    LEAD(Items.LEAD, 5),            // 끈
    NAME_TAG(Items.NAME_TAG, 1),        // 이름표

    MUTTON(Items.MUTTON, 10),       // 익히지 않은 양고기
    WHITE_BANNER(Items.WHITE_BANNER, 10),       // 하얀 현수막
    BEETROOT(Items.BEETROOT, 10),       // 비트
    SPECTRAL_ARROW(Items.SPECTRAL_ARROW, 10),       // 분광화살
    TOTEM_OF_UNDYING(Items.TOTEM_OF_UNDYING, 1),    // 불사의 토템
    SHULKER_SHELL(Items.SHULKER_SHELL, 10),     // 셜커 껍데기
    MUSIC_DISC_13(Items.MUSIC_DISC_13, 1),
    MUSIC_DISC_CAT(Items.MUSIC_DISC_CAT, 1),
    MUSIC_DISC_BLOCKS(Items.MUSIC_DISC_BLOCKS, 1),
    MUSIC_DISC_CHIRP(Items.MUSIC_DISC_CHIRP, 1),
    MUSIC_DISC_FAR(Items.MUSIC_DISC_FAR, 1),
    MUSIC_DISC_MALL(Items.MUSIC_DISC_MALL, 1),
    MUSIC_DISC_MELLOHI(Items.MUSIC_DISC_MELLOHI, 1),
    MUSIC_DISC_STAL(Items.MUSIC_DISC_STAL, 1),
    MUSIC_DISC_STRAD(Items.MUSIC_DISC_STRAD, 1),
    MUSIC_DISC_WARD(Items.MUSIC_DISC_WARD, 1),
    MUSIC_DISC_11(Items.MUSIC_DISC_11, 1),
    MUSIC_DISC_WAIT(Items.MUSIC_DISC_WAIT, 1),
    MUSIC_DISC_OTHERSIDE(Items.MUSIC_DISC_OTHERSIDE, 1),
    MUSIC_DISC_RELIC(Items.MUSIC_DISC_RELIC, 1),
    MUSIC_DISC_5(Items.MUSIC_DISC_5, 1),
    MUSIC_DISC_PIGSTEP(Items.MUSIC_DISC_PIGSTEP, 1),
    TRIDENT(Items.TRIDENT, 1),      // 삼지창
    PHANTOM_MEMBRANE(Items.PHANTOM_MEMBRANE, 10),       // 팬텀 막
    HEART_OF_THE_SEA(Items.HEART_OF_THE_SEA, 1),       // 바다의 심장
    CROSSBOW(Items.CROSSBOW, 1),        // 쇠뇌
    SUSPICIOUS_STEW(Items.SUSPICIOUS_STEW, 10),     // 수상한 스튜
    LOOM(Items.LOOM, 10),       // 베틀
    GOAT_HORN(Items.GOAT_HORN, 1),     // 염소 뿔
    COMPOSTER(Items.COMPOSTER, 10),    // 퇴비통
    BARREL(Items.BARREL, 10),       // 통
    SMOKER(Items.SMOKER, 10),       // 훈연기
    BLAST_FURNACE(Items.BLAST_FURNACE, 10),     // 용광로
    CARTOGRAPHY_TABLE(Items.CARTOGRAPHY_TABLE, 10),     // 지도 제작대
    FLETCHING_TABLE(Items.FLETCHING_TABLE, 10),     // 화살 작업대
    GRINDSTONE(Items.GRINDSTONE, 10),       // 숫돌
    SMITHING_TABLE(Items.SMITHING_TABLE, 10),       // 대장장이 작업대
    STONECUTTER(Items.STONECUTTER, 10),     // 석재 절단기
    BELL(Items.BELL, 1),       // 종
    LANTERN(Items.LANTERN, 10),     // 랜턴
    SOUL_LANTERN(Items.SOUL_LANTERN, 10),       // 영혼 랜턴
    SWEET_BERRIES(Items.SWEET_BERRIES, 10),         // 달콤한 열매
    GLOW_BERRIES(Items.GLOW_BERRIES, 10),       // 발광 열매
    CAMPFIRE(Items.CAMPFIRE, 10),       // 모닥불
    SOUL_CAMPFIRE(Items.SOUL_CAMPFIRE, 10),     // 영혼 모닥불
    SHROOMLIGHT(Items.SHROOMLIGHT, 10),     // 버섯불
    BEE_NEST(Items.BEE_NEST, 10),           // 벌집
    HONEY_BOTTLE(Items.HONEY_BOTTLE, 10),       // 꿀이 든 병
    LODESTONE(Items.LODESTONE, 10),         // 자석석
    CRYING_OBSIDIAN(Items.CRYING_OBSIDIAN, 5),     // 우는 흑요석
    BLACKSTONE(Items.BLACKSTONE, 5),       // 흑암
    CANDLE(Items.CANDLE, 10),           //  초
    POINTED_DRIPSTONE(Items.POINTED_DRIPSTONE, 10),     // 뾰족한 점적석
    RED_MUSHROOM_BLOCK(Items.RED_MUSHROOM_BLOCK, 10),       // 빨간색 버섯 블록
    BROWN_MUSHROOM_BLOCK(Items.BROWN_MUSHROOM_BLOCK, 10),       // 갈색 버섯 블록
    CAKE(Items.CAKE, 10),               // 케이크
    FLINT(Items.FLINT, 10),             // 부싯돌
    BRUSH(Items.BRUSH, 10),         /// 솔
    NETHERITE_UPGRADE_SMITHING_TEMPLATE(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1),     // 대장장이 형판
    SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, 1),
    COAST_ARMOR_TRIM_SMITHING_TEMPLATE(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, 1),
    WARD_ARMOR_TRIM_SMITHING_TEMPLATE(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, 1),
    VEX_ARMOR_TRIM_SMITHING_TEMPLATE(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, 1),
    SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, 1),
    RIB_ARMOR_TRIM_SMITHING_TEMPLATE(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, 1),
    SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, 1),
    WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, 1),
    SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, 1),
    ANGLER_POTTERY_SHERD(Items.ANGLER_POTTERY_SHERD, 1),
    ARCHER_POTTERY_SHERD(Items.ARCHER_POTTERY_SHERD, 1),
    ARMS_UP_POTTERY_SHERD(Items.ARMS_UP_POTTERY_SHERD, 1),
    BLADE_POTTERY_SHERD(Items.BLADE_POTTERY_SHERD, 1),
    BREWER_POTTERY_SHERD(Items.BREWER_POTTERY_SHERD, 1),
    BURN_POTTERY_SHERD(Items.BURN_POTTERY_SHERD, 1),
    DANGER_POTTERY_SHERD(Items.DANGER_POTTERY_SHERD, 1),
    EXPLORER_POTTERY_SHERD(Items.EXPLORER_POTTERY_SHERD, 1),
    FRIEND_POTTERY_SHERD(Items.FRIEND_POTTERY_SHERD, 1),
    HEART_POTTERY_SHERD(Items.HEART_POTTERY_SHERD, 1),
    HEARTBREAK_POTTERY_SHERD(Items.HEARTBREAK_POTTERY_SHERD, 1),
    HOWL_POTTERY_SHERD(Items.HOWL_POTTERY_SHERD, 1),
    MINER_POTTERY_SHERD(Items.MINER_POTTERY_SHERD, 1),
    MOURNER_POTTERY_SHERD(Items.MOURNER_POTTERY_SHERD, 1),
    PLENTY_POTTERY_SHERD(Items.PLENTY_POTTERY_SHERD, 1),
    PRIZE_POTTERY_SHERD(Items.PRIZE_POTTERY_SHERD, 1),
    SHEAF_POTTERY_SHERD(Items.SHEAF_POTTERY_SHERD, 1),
    SHELTER_POTTERY_SHERD(Items.SHELTER_POTTERY_SHERD, 1),
    SKULL_POTTERY_SHERD(Items.SKULL_POTTERY_SHERD, 1),
    SNORT_POTTERY_SHERD(Items.SNORT_POTTERY_SHERD, 1);
    ;


    CodexSet(Item item) {
        this.item = item;
        this.codexCount = 10;
    }

    CodexSet(Item item, int codexCount) {
        this.item = item;
        this.codexCount = codexCount;
    }

    private final Item item;
    private final int codexCount;

    public Item getItem() {
        return this.item;
    }

    public int getCount() {
        return this.codexCount;
    }
}
