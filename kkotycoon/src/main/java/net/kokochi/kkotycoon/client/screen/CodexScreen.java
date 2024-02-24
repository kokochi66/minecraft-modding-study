package net.kokochi.kkotycoon.client.screen;

import net.kokochi.kkotycoon.client.data.CodexSet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.FontManager;
import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

// 도감 화면 GUI를 개발
public class CodexScreen extends Screen {
    private List<Rectangle> itemSlotPositions;
    private List<ItemStack> itemStacks;
    private List<Text> itemTooltips;
    private Rectangle[] arrowPositions;

    private final int MAX_PAGE = 2;
    private int currentPage = 0;
    public CodexScreen(Text title) {
        super(title);
    }

    private void initPositionData() {
        // 도감 아이템 슬롯
        itemSlotPositions = new ArrayList<>();
        int slotBoxSize = 20;
        int padding = 2;
        int yOffset = 10; // 제목 영역 고려
        for (int j = 0; j < 7; j++) {
            for (int i = 0; i < 15; i++) {
                int x = this.width / 2 - (15 * (slotBoxSize + padding) / 2) + (i * (slotBoxSize + padding));
                int y = this.height / 2 - (7 * (slotBoxSize + padding) / 2) + (j * (slotBoxSize + padding)) + yOffset;
                itemSlotPositions.add(new Rectangle(x, y, slotBoxSize, slotBoxSize));
            }
        }
        itemStacks = Arrays.stream(CodexSet.values()).map(c -> new ItemStack(c.getItem())).collect(Collectors.toList());
        itemTooltips = itemStacks.stream().map(itemStack -> getTooltipFromItem(Objects.requireNonNull(this.client), itemStack).stream().findFirst().orElse(Text.literal("none"))).collect(Collectors.toList());

        int leftArrowX = 10; // 화살표 위치 조정 필요
        int rightArrowX = this.width - 30; // 화살표 위치 조정 필요
        int arrowSize = 20;
        int arrowY = this.height / 2 - 10; // 화살표 위치 조정 필요
        // 도감 왼쪽, 오른쪽 화살표
        arrowPositions = new Rectangle[2];
        arrowPositions[0] = new Rectangle(leftArrowX, arrowY, arrowSize, arrowSize);
        arrowPositions[1] = new Rectangle(rightArrowX, arrowY, arrowSize, arrowSize);
    }



    // 페이지 변경 메서드
    private void changePage(int direction) {
        this.currentPage += direction;

        if (this.currentPage < 0) {
            this.currentPage = 0;
        } else if (this.currentPage >= MAX_PAGE) { {
            this.currentPage = MAX_PAGE;
        }}

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context); // 기본 배경 렌더링
        initPositionData();        // 포지션 관련 정보를 렌더링 할 때마다 초기화 (그때그때마다 화면 비율을 갖고 계산해야하기 때문)

        super.render(context, mouseX, mouseY, delta);

        // 어두운 오버레이 그리기
        context.fill(0, 0, this.width, this.height, 0x01000000); // 반투명 검은색

        // 화살표 버튼 그리기
        context.fill(arrowPositions[0].x, arrowPositions[0].y, arrowPositions[0].x + arrowPositions[0].width, arrowPositions[0].y + arrowPositions[0].height, 0xFF0000FF); // 왼쪽 화살표 (색상 변경 필요)
        context.fill(arrowPositions[1].x, arrowPositions[1].y, arrowPositions[1].x + arrowPositions[1].width, arrowPositions[1].y + arrowPositions[1].height, 0xFF0000FF); // 왼쪽 화살표 (색상 변경 필요)

        // 도감 달성도 표시
        int achievedCount = 0; // 달성한 도감 수 (변수로 처리)
        String titleText = "도감 달성도 " + achievedCount + "/300";
        int titleWidth = textRenderer.getWidth(titleText); // 텍스트 너비 계산
        int titleX = this.width / 2 - titleWidth / 2; // 텍스트 X 좌표 (가운데 정렬)
        int titleY = 30; // 텍스트 Y 좌표 (상단에서부터의 거리)
        context.drawText(textRenderer, titleText, titleX, titleY, 0xFFFFFFFF, true); // 텍스트 그리기

        Identifier identifier = new Identifier("textures/block/dirt.png");
        // 아이템 슬롯 그리기
        // 아이템 렌더링 준비
        List<ItemStack> itemStacks = Arrays.stream(CodexSet.values()).map(c -> new ItemStack(c.getItem())).toList();
        int iconOffset = 2;
        for (int i = 0; i < 105; i ++) {
            Rectangle rectangle = itemSlotPositions.get(i);
            int x = rectangle.x;
            int y = rectangle.y;
            int slotBoxSize = rectangle.width;

            context.fill(x, y, x + slotBoxSize, y + slotBoxSize, 0x88555555); // 더 큰 어두운 사각형

            int stackIndex = (currentPage * 105) + i;
            if (stackIndex >= itemStacks.size()) {
                stackIndex = itemStacks.size() - 1;
            }
            ItemStack itemStack = itemStacks.get(stackIndex);
            // 마우스가 아이템 슬롯 위에 있는지 확인
            if (mouseX >= x && mouseX <= x + slotBoxSize && mouseY >= y && mouseY <= y + slotBoxSize) {
                this.setTooltip(itemTooltips.get(stackIndex));
            }

            // 아이템 이미지 렌더링
//            context.drawItem(itemStack, x + iconOffset, y + iconOffset);
            context.drawTexture(identifier, x + iconOffset, y + iconOffset, 16, 16, 0, 0, 16, 16, 16, 16);
//            // 도감 달성을 위해 필요한 개수 지정 (일단 디폴드로 10개로 설정)
            context.drawText(textRenderer, "10", x + 8, y + 10, 0xFFFFFFFF, true); // 텍스트 그리기
//            context.drawTooltip(textRenderer, Text.of("10"), x + 8, y + 10);
        }
    }

    // 화살표 클릭 이벤트 처리
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if (button == 0) {
            if (mouseX >= arrowPositions[0].x && mouseX <= arrowPositions[0].x + arrowPositions[0].width && mouseY >= arrowPositions[0].y && mouseY <= arrowPositions[0].y + arrowPositions[0].height) {
                changePage(-1); // 이전 페이지로
                return true;
            } else if (mouseX >= arrowPositions[1].x && mouseX <= arrowPositions[1].x + arrowPositions[1].width && mouseY >= arrowPositions[1].y && mouseY <= arrowPositions[1].y + arrowPositions[1].height) {
                changePage(1); // 다음 페이지로
                return true;
            }
        }



        return super.mouseClicked(mouseX, mouseY, button);
    }


    // esc를 눌러서 닫을 수 있는지 여부 설정
    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}