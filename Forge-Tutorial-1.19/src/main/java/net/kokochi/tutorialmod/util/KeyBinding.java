package net.kokochi.tutorialmod.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

// KeyBinding 클래스는 사용자 정의 키 이벤트를 설정하기 위한 클래스입니다.
public class KeyBinding {
    // KEY_CATEGORY_TUTORIAL는 키 바인딩을 그룹화하기 위한 카테고리 이름입니다.
    // 게임 내의 컨트롤 설정 메뉴에서 이 이름 아래에 해당 키 바인딩이 표시됩니다.
    public static final String KEY_CATEGORY_TUTORIAL = "key.category.tutorialmod.tutorial";

    // KEY_DRINK_WATER는 실제 키 바인딩의 식별자로 사용됩니다. 이 식별자는 설정 파일 등에서
    // 해당 키 바인딩을 참조할 때 사용됩니다.
    public static final String KEY_DRINK_WATER = "key.tutorialmod.drink_water";

    // DRINKING_KEY는 사용자가 '물 마시기' 동작을 할당받을 키를 정의합니다.
    // KeyMapping 생성자는 키 바인딩을 정의할 때 사용되며, 여러 파라미터를 받습니다:
    // 1. 키의 식별자(KEY_DRINK_WATER)
    // 2. 키 이벤트가 적용될 컨텍스트(KeyConflictContext.IN_GAME은 게임 내에서만 해당 키가 활성화됨을 의미)
    // 3. 입력 유형(InputConstants.Type.KEYSYM은 키보드 키를 의미)
    // 4. 실제 키 코드(GLFW.GLFW_KEY_O는 키보드의 'O' 키를 의미)
    // 5. 키 바인딩의 카테고리(KEY_CATEGORY_TUTORIAL)
    public static final KeyMapping DRINKING_KEY = new KeyMapping(KEY_DRINK_WATER, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_O, KEY_CATEGORY_TUTORIAL);

}
