package net.kokochi.tutorialmod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kokochi.tutorialmod.TutorialMod;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ThirstHudOverlay {
    // 목마름 바의 이미지 리소스 위치를 정의합니다. 'filled_thirst'는 목마름이 차 있을 때, 'empty_thirst'는 비어 있을 때의 상태를 나타냅니다.
    private static final ResourceLocation FILLED_THIRST = new ResourceLocation(TutorialMod.MOD_ID, "textures/thirst/filled_thirst.png");
    private static final ResourceLocation EMPTY_THIRST = new ResourceLocation(TutorialMod.MOD_ID, "textures/thirst/empty_thirst.png");

    // HUD_THIRST는 HUD에 목마름 바를 그리는 데 사용됩니다.
    public static final IGuiOverlay HUD_THIRST = ((gui, poseStack, partialTick, width, height) -> {
        // 화면의 중앙과 바닥을 기준으로 목마름 바를 배치합니다.
        int x = width / 2;
        int y = height;

        // 렌더링 상태를 설정합니다. 색상과 텍스처를 정의합니다.
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, EMPTY_THIRST);

        // 비어 있는 목마름 바를 그립니다. 이 반복문은 10개의 비어 있는 상태의 바를 그립니다.
        for (int i = 0; i < 10; i++) {
            GuiComponent.blit(poseStack, x - 94 + (i * 9), y - 54, 0, 0, 12, 12, 12, 12);
        }

        // 채워진 목마름 바의 텍스처를 설정합니다.
        RenderSystem.setShaderTexture(0, FILLED_THIRST);
        // 플레이어의 현재 목마름 상태에 따라 채워진 목마름 바를 그립니다.
        for (int i = 0; i < 10; i++) {
            // 플레이어의 목마름 수치가 반복 변수 i보다 크면, 채워진 상태의 바를 그립니다.
            if (ClientThirstData.getPlayerThirst() > i) {
                GuiComponent.blit(poseStack, x - 94 + (i * 9), y - 54, 0, 0, 12, 12, 12, 12);
            } else {
                // 플레이어의 목마름 수치가 i보다 작거나 같으면, 더 이상 채워진 바를 그리지 않고 반복문을 종료합니다.
                break;
            }
        }
    });
}
