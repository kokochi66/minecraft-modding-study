package net.kokochi.tutorialmod.item;

import net.kokochi.tutorialmod.TutorialMod;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TutorialMod.MOD_ID);
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
    /*
    * 새로운 모드 아이템을 만드는 순서
    *
    * 1.
    * 모드 아이템 클래스를 생성한다.
    * RegisteryObejct 로 해당 아이템의 이름과 기본적이 아이템의 성질에 대한 부분을 정의한다.
    *
    * 2.
    * 에셋 폴더에서 해당 아이템의 대한 에셋 정보를 정의한다.
    *   /resources/assets/{modid}/lang/en_us.json
    *       item.{modid}.{itemName} : {itemName} 인벤토리에 표기될 아이템 이름을 작성한다.
    *
    *   /resources/assets/{modid}/models/item/{itemName}.json
    *       아이템에 대한 추가적인 모델링 관련 정보 저장
    *       parent: 모델이 상속받을 부모 모델의 경로 지정, item/generated가 일반적인 마인크래프트에서 기본적으로 제공되는 아이템 모델임
    *       texture: 아이템의 텍스쳐를 지정, 아이템은 기본적으로 레이어 하나만 쓰면 되므로, layer0 만 지정해주면 됨. (여러개의 텍스처 레이어가 필요한 경우에는 layer1, layer2 등과 같이 사용할 수 있음)
    *           텍스쳐 폴더의 나머지 키값은 해당 디렉토리를 직접 지정함 item/{itemName} 등과 같이 정리를 잘 해보자
    * */

    // 새로운 아이템을 만드려면 새로운 클래스를 만들 필요는 없고 요것을 여러개 만들어주면 된다.
    public static final RegistryObject<Item> ZIRCON = ITEMS.register("zircon",
            // modid 와 같이 이 아이템 이름도 모두 영소문자로만 이루어져있어야 한다.
            () -> new Item(new Item.Properties()
                    .stacksTo(5) // 스택될 수 있는 개수를 정의 (default 64)
                    .tab(ModCreativeModeTab.TUTORIAL_TAB)  // 크리에이티브 모드 어느 탭에 존재할지를 정의
            ));

    public static final RegistryObject<Item> RAW_ZIRCON = ITEMS.register("raw_zircon",
            () -> new Item(new Item.Properties()
                    .stacksTo(5)
                    .tab(ModCreativeModeTab.TUTORIAL_TAB_2)
            ));
}
