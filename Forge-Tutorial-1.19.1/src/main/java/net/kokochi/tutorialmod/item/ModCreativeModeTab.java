package net.kokochi.tutorialmod.item;


import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

// 크리에이티브 모드에서 내 모드만의 특별한 탭 만들기
public class ModCreativeModeTab {
    // 탭이름 다국어 설정은 /resoureces/assets/{modid}/lang/en_us.json 에서 해주면 됨


    public static final CreativeModeTab TUTORIAL_TAB = new CreativeModeTab("tutorialmodtab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.ZIRCON.get());
            // 아이콘을 특정 아이템의 아이콘으로 지정한다.
        }
    };

    public static final CreativeModeTab TUTORIAL_TAB_2 = new CreativeModeTab("tutorialmodtab2") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.RAW_ZIRCON.get());
            // 아이콘을 특정 아이템의 아이콘으로 지정한다.
        }
    };
}
