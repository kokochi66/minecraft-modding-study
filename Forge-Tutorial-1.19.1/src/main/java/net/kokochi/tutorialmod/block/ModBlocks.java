package net.kokochi.tutorialmod.block;

import net.kokochi.tutorialmod.TutorialMod;
import net.kokochi.tutorialmod.block.custom.BlueberryCropBlock;
import net.kokochi.tutorialmod.block.custom.GemInfusingStationBlock;
import net.kokochi.tutorialmod.block.custom.JumpyBlock;
import net.kokochi.tutorialmod.block.custom.ZirconLampBlock;
import net.kokochi.tutorialmod.fluid.ModFluids;
import net.kokochi.tutorialmod.item.ModCreativeModeTab;
import net.kokochi.tutorialmod.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.checkerframework.checker.units.qual.C;

import java.util.function.Supplier;

// 새로운 모드 블럭들을 생성하기 위한 클래스
public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, TutorialMod.MOD_ID);
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }


    /*
    * 블럭을 정의할 때에는 블럭에 대한 자체 정의와, 블럭을 캤을 때 나오는 아이템에 대한 정의를 기본적으로 같이 해주어야 한다.
    *
    * 1.
    * 블럭 아이템에 대한 정의를 한다.
    * 여기서 편의성을 위한 registerBlock 메소드를 구현한다 (해당 블럭에 대한 구현과 아이템에 대한 register를 연결해주는 메소드)
    *
    * 2. 에셋을 생성한다.
    *
    *   /resources/assets/{modid}/blockstates/{blockName}.json
    *       해당 폴더에 기본적인 블럭에 대한 명세를 추가한다 (모델에 대한 위치를 지정해준다.)
    *       variants키를 사용해서 블록에 대한 에셋 형태를 지정해줄 수 있다.
    *           예를들어 ""가 키로 사용될 때 이것은 방향성 없는 블록이라는 의미이며, 모든 면이 동일한 에셋을 사용함을 의미한다.
    *
    * 3. 레시피 생성
    *   /resources/data/{modid}/recipes/{blockName}.json
    *   로 레시피를 생성할 수 있다.
    *   레시피 생성은 https://misode.github.io/loot-table/ 이런데서 만들 수도 있다.
    * */

    // 블럭 자체에 대한 아이템 정의
    public static final RegistryObject<Block> ZIRCON_BLOCK = registerBlock("zircon_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)        // 어떤 아이템에 유사한 기능을 하는지에 대한 정의를 해준다. (기본값을 쉽게 정의하기 위함)
                    .strength(0.6f, 6.0f)                       // 블록의 경도와 저항력을 설정합니다. 첫 번째 값은 경도로, 블록을 깨는 데 필요한 시간에 영향을 미칩니다. 두 번째 값은 폭발 저항력으로, 블록이 폭발에 얼마나 잘 견디는지를 결정합니다.
                    .requiresCorrectToolForDrops()                              // 올바른 도구를 사용해야만 아이템이 드롭되도록 설정합니다. 예를 들어, 돌은 곡괭이로만 깨뜨릴 수 있습니다.
                    .sound(SoundType.STONE)                                     // 블록이 깨지거나, 놓이거나, 밟혔을 때의 소리를 설정합니다.
                    .lightLevel((state) -> 0)                                   // 블록이 방출하는 빛의 양을 설정합니다. 0은 빛을 방출하지 않음을 의미합니다.
                    // .friction(0.6f)                                          // 블록의 마찰 계수를 설정합니다. 이는 엔티티가 블록 위를 이동할 때의 속도에 영향을 미칩니다.
                    // .speedFactor(1.0f)                                       // 엔티티가 블록 위를 이동할 때의 속도를 조정합니다. 기본값은 1.0입니다.
                    // .jumpFactor(1.0f)                                        // 엔티티가 블록 위에서 점프할 때의 높이를 조정합니다. 기본값은 1.0입니다.
                    .noOcclusion()                                              // 블록이 다른 블록을 가리지 않도록 설정합니다. 이는 일부 반투명 블록에 유용할 수 있습니다.
                    // .isRedstoneConductor((state, world, pos) -> true)        // 블록이 레드스톤 신호를 전도할 수 있는지 여부를 결정합니다.
                    // .isSuffocating((state, world, pos) -> true)              // 블록이 엔티티의 공기를 차단하는지 여부를 결정합니다. 예를 들어, 대부분의 고체 블록은 숨을 막습니다.
                    // .isViewBlocking((state, world, pos) -> true)             // 블록이 엔티티의 시야를 차단하는지 여부를 결정합니다.
                    // .hasPostProcess((state, world, pos) -> false)            // 블록이 특정 시각적 효과(예: 끝 포탈의 시각적 효과)를 가지는지 여부를 결정합니다.
                    // .emissiveRendering((state, world, pos) -> false)         // 블록이 자체 발광하는지 여부를 결정합니다. 이는 일부 블록에 시각적 효과를 추가할 때 유용할 수 있습니다.
            ), ModCreativeModeTab.TUTORIAL_TAB);

    public static final RegistryObject<Block> ZIRCON_ORE = registerBlock("zircon_ore",
            // DropExperienceBlock은 채굴 시 경험치를 지급하는 블록이다.
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(0.1f)
                    .requiresCorrectToolForDrops(),
                    UniformInt.of(3, 7)     // 채굴되는 경험치가 3에서 7까지 무작위로 설정된다.
            ), ModCreativeModeTab.TUTORIAL_TAB);
    public static final RegistryObject<Block> DEEPSLATE_ZIRCON_ORE = registerBlock("deepslate_zircon_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(0.8f).requiresCorrectToolForDrops(),
                    UniformInt.of(3, 7)
            ), ModCreativeModeTab.TUTORIAL_TAB);
    public static final RegistryObject<Block> ENDSTONE_ZIRCON_ORE = registerBlock("endstone_zircon_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(0.5f).requiresCorrectToolForDrops(),
                    UniformInt.of(3, 7)
            ), ModCreativeModeTab.TUTORIAL_TAB);
    public static final RegistryObject<Block> NETHERRACK_ZIRCON_ORE = registerBlock("netherrack_zircon_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(0.3f).requiresCorrectToolForDrops(),
                    UniformInt.of(3, 7)
            ), ModCreativeModeTab.TUTORIAL_TAB);


    public static final RegistryObject<Block> JUMPY_BLOCK = registerBlock("jumpy_block",
            () -> new JumpyBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(0.8f).requiresCorrectToolForDrops()
            ), ModCreativeModeTab.TUTORIAL_TAB);

    public static final RegistryObject<Block> ZIRCON_LAMP = registerBlock("zircon_lamp",
            () -> new ZirconLampBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(0.8f).requiresCorrectToolForDrops()
                    // BooleanProperty 상태에 따라서 lightLevel을 적용해준다.
                    .lightLevel(state -> state.getValue(ZirconLampBlock.LIT) ? 0 : 30)
            ), ModCreativeModeTab.TUTORIAL_TAB);

    public static final RegistryObject<Block> BLUEBERRY_CROP = BLOCKS.register("blueberry_crop",
            () -> new BlueberryCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<LiquidBlock> SOAP_WATER_BLOCK = BLOCKS.register("soap_water_block",
            () -> new LiquidBlock(ModFluids.SOURCE_SOAP_WATER, BlockBehaviour.Properties.copy(Blocks.WATER)));

    public static final RegistryObject<Block> GEM_INFUSING_STATION = registerBlock("gem_infusing_station",
            () -> new GemInfusingStationBlock(BlockBehaviour.Properties.of(Material.METAL)
                    .strength(6f).requiresCorrectToolForDrops().noOcclusion()), ModCreativeModeTab.TUTORIAL_TAB);



    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    public static <T extends Block> RegistryObject<Item> registerBlockItem(String name, Supplier<T> block, CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }
}
