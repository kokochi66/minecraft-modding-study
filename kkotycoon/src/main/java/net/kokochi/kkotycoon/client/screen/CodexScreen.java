package net.kokochi.kkotycoon.client.screen;

import net.kokochi.kkotycoon.client.data.CodexSet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// 도감 화면 GUI를 개발
public class CodexScreen extends Screen {

    private final int MAX_PAGE = 2;
    private int currentPage = 0;
    public CodexScreen(Text title) {
        super(title);
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

        super.render(context, mouseX, mouseY, delta);

        // 어두운 오버레이 그리기
        context.fill(0, 0, this.width, this.height, 0x01000000); // 반투명 검은색

        // 화살표 버튼 그리기
        int leftArrowX = 10; // 화살표 위치 조정 필요
        int rightArrowX = this.width - 30; // 화살표 위치 조정 필요
        int arrowSize = 20;
        int arrowY = this.height / 2 - 10; // 화살표 위치 조정 필요
        context.fill(leftArrowX, arrowY, leftArrowX + arrowSize, arrowY + arrowSize, 0xFF0000FF); // 왼쪽 화살표 (색상 변경 필요)
        context.fill(rightArrowX, arrowY, rightArrowX + arrowSize, arrowY + arrowSize, 0xFF0000FF); // 오른쪽 화살표 (색상 변경 필요)


        // 도감 달성도 표시
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int achievedCount = 0; // 달성한 도감 수 (변수로 처리)
        String achievementText = "도감 달성도 " + achievedCount + "/300";
        int textWidth = textRenderer.getWidth(achievementText); // 텍스트 너비 계산
        int textX = this.width / 2 - textWidth / 2; // 텍스트 X 좌표 (가운데 정렬)
        int textY = 30; // 텍스트 Y 좌표 (상단에서부터의 거리)
        context.drawText(textRenderer, achievementText, textX, textY, 0xFFFFFFFF, true); // 텍스트 그리기

        // 아이템 슬롯 그리기
        // 테스트용 흙블럭 텍스쳐를 띄워줌
        // 아이템 렌더링 준비
        List<ItemStack> itemStacks = Arrays.stream(CodexSet.values()).map(c -> new ItemStack(c.getItem())).toList();
        int slotBoxSize = 20;
        int padding = 2;
        int yOffset = 10; // 아래로 이동시키고 싶은 픽셀 값 (제목 영역 고려)

        int iconSize = 16;
        int iconOffset = (slotBoxSize - iconSize) / 2;
        for (int j = 0; j < 7; j ++) {
            for (int i = 0; i < 15; i++) {
                int x = this.width / 2 - (15 * (slotBoxSize + padding) / 2) + (i * (slotBoxSize + padding)); // x 위치 계산
                int y = this.height / 2 - (7 * (slotBoxSize + padding) / 2) + (j * (slotBoxSize + padding)) + yOffset; // y 위치 계산
                context.fill(x, y, x + slotBoxSize, y + slotBoxSize, 0x88555555); // 더 큰 어두운 사각형

                int stackIndex = (currentPage * 100) + (j * 15) + i;
                if (stackIndex >= itemStacks.size()) {
                    stackIndex = itemStacks.size() - 1;
                }
                ItemStack itemStack = itemStacks.get(stackIndex);
                // 마우스가 아이템 슬롯 위에 있는지 확인
                if (mouseX >= x && mouseX <= x + slotBoxSize && mouseY >= y && mouseY <= y + slotBoxSize) {
                    // 마우스 위치에 따라 아이템 이름 표시
                    List<Text> tooltip = getTooltipFromItem(MinecraftClient.getInstance(), itemStack);
                    // 툴팁 설정
                    this.setTooltip(tooltip.stream().findAny().orElse(Text.literal("none")));
                }

                // 흙 블럭 렌더링
                context.drawItem(itemStack, x + iconOffset, y + iconOffset);
            }
        }

    }

    // 화살표 클릭 이벤트 처리
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // 화살표 버튼 영역 계산 필요
        int leftArrowX = 10; // 화살표 위치 조정 필요
        int rightArrowX = this.width - 30; // 화살표 위치 조정 필요
        int arrowSize = 20;
        int arrowY = this.height / 2 - 10; // 화살표 위치 조정 필요
        if (mouseX >= leftArrowX && mouseX <= leftArrowX + arrowSize && mouseY >= arrowY && mouseY <= arrowY + arrowSize) {
            changePage(-1); // 이전 페이지로
            return true;
        } else if (mouseX >= rightArrowX && mouseX <= rightArrowX + arrowSize && mouseY >= arrowY && mouseY <= arrowY + arrowSize) {
            changePage(1); // 다음 페이지로
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }



    // esc를 눌러서 닫을 수 있는지 여부 설정
    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}