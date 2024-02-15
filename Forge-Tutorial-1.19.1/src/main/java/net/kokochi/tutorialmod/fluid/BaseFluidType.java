package net.kokochi.tutorialmod.fluid;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

// BaseFluidType 클래스는 커스텀 액체의 기본 속성과 렌더링 방식을 정의합니다.
public class BaseFluidType extends FluidType {
    // 액체의 정지 상태, 흐르는 상태, 오버레이 텍스처를 저장합니다.
    private final ResourceLocation stillTexture;
    private final ResourceLocation flowingTexture;
    private final ResourceLocation overlayTexture;

    // 액체의 색상과 안개 색상을 정의합니다.
    private final int tintColor;
    private final Vector3f fogColor;

    // 커스텀 액체 타입의 생성자입니다. 여기서 액체의 텍스처, 색상, 속성을 초기화합니다.
    public BaseFluidType(ResourceLocation stillTexture, ResourceLocation flowingTexture, ResourceLocation overlayTexture, int tintColor, Vector3f fogColor, final Properties properties) {
        super(properties);
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.overlayTexture = overlayTexture;
        this.fogColor = fogColor;
        this.tintColor = tintColor;
    }

    public ResourceLocation getStillTexture() {
        return stillTexture;
    }

    public ResourceLocation getFlowingTexture() {
        return flowingTexture;
    }

    public ResourceLocation getOverlayTexture() {
        return overlayTexture;
    }

    public int getTintColor() {
        return tintColor;
    }

    public Vector3f getFogColor() {
        return fogColor;
    }

    // 클라이언트 사이드에서 액체의 렌더링 방식을 정의합니다. 이 메소드는 Forge에서 자동으로 호출됩니다.
    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {

            @Override
            public ResourceLocation getStillTexture() {
                return stillTexture;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return flowingTexture;
            }

            @Override
            public int getTintColor() {
                return tintColor;
            }

            @Override
            public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return fogColor;
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {

                RenderSystem.setShaderFogStart(1f);
                RenderSystem.setShaderFogEnd(6f);
            }
        });
    }
}
