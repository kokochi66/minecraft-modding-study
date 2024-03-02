package net.kokochi.kkotycoon.client.screen;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.kokochi.kkotycoon.packet.CodexC2SPostPacket;
import net.kokochi.kkotycoon.entity.player.ClientPlayerDataManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static net.kokochi.kkotycoon.KkoTycoon.MOD_ID;

// 도감 화면 GUI를 개발
public class CodexScreen extends Screen {
    private List<Rectangle> itemSlotPositions;
    private List<ItemStack> itemStacks;
    private List<Text> itemTooltips;
    private Rectangle[] arrowPositions;
    private ButtonWidget rewardButtonWidget;

    private final int MAX_PAGE = 2;
    private int currentPage = 0;
    public CodexScreen(Text title) {
        super(title);
    }


    @Override
    protected void init() {
        super.init();
        initPositionData();

        // 보상 수령을 위한 버튼
        Text rewardButtonText = Text.of("보상 수령");
        int rewardButtonTextWidth = textRenderer.getWidth(rewardButtonText);
        rewardButtonWidget = ButtonWidget.builder(rewardButtonText, button -> {
            if (button.isSelected()) {
                // 수령할 보상이 없다면 서버로 요청을 보내지 않습니다. (과부하 방지용)
                long codexReward = ClientPlayerDataManager.playerData.calculCodexReward();
                if (codexReward <= 0) {
                    this.client.player.sendMessage(Text.of("수령할 보상이 없습니다. 잠시만 기다려 주세요."));
                    return;
                }

                // 서버로 보상 수령 요청을 보냅니다. (따로 데이터를 담아서 보낼 필요는 없습니다.)
                PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
                Identifier identifier = new Identifier(MOD_ID, CodexC2SPostPacket.CODEX_POST_REWARD_REQUEST_ID);
                ClientPlayNetworking.send(identifier, packetByteBuf);
            }
        }).dimensions(this.width - (rewardButtonTextWidth + 40), 15, rewardButtonTextWidth + 20, 30).build();
        addDrawableChild(rewardButtonWidget);
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


        itemStacks = ClientPlayerDataManager.codexList.stream()
                .map(id -> new ItemStack(Item.byRawId(id)))
                .collect(Collectors.toList());
        itemTooltips = itemStacks.stream()
                .map(itemStack ->
                        Text.of(getTooltipFromItem(Objects.requireNonNull(this.client), itemStack)
                                .stream().findFirst().orElse(Text.literal("none")).getString() + "(10)")
                )
                .collect(Collectors.toList());

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

        super.render(context, mouseX, mouseY, delta);

        // 어두운 오버레이 그리기
        context.fill(0, 0, this.width, this.height, 0x01000000); // 반투명 검은색

        // 화살표 버튼 그리기
        Identifier leftArrowIdentifier = new Identifier(MOD_ID, "textures/gui/arrow.png");
        Identifier rightArrowIdentifier = new Identifier(MOD_ID, "textures/gui/right_arrow.png");

        context.fill(arrowPositions[0].x, arrowPositions[0].y, arrowPositions[0].x + arrowPositions[0].width, arrowPositions[0].y + arrowPositions[0].height, 0x90FFFFFF); // 왼쪽 화살표 (색상 변경 필요)
        context.fill(arrowPositions[1].x, arrowPositions[1].y, arrowPositions[1].x + arrowPositions[1].width, arrowPositions[1].y + arrowPositions[1].height, 0x90FFFFFF); // 오른쪽 화살표 (색상 변경 필요)
        context.drawTexture(leftArrowIdentifier, arrowPositions[0].x, arrowPositions[0].y, 0, 0, 20, 20, 20, 20);
        context.drawTexture(rightArrowIdentifier, arrowPositions[1].x, arrowPositions[1].y, 0, 0, 20, 20, 20, 20);

        // 도감 달성도 표시
        byte[] codexArray = ClientPlayerDataManager.playerData.getCodexArray();
        int achievedCount = 0; // 달성한 도감 수 (변수로 처리)
        for (byte b : codexArray) {
            if (b == 1) {
                achievedCount++;
            }
        }
        String titleText = "도감 달성도 " + achievedCount + "/300";
        int titleWidth = textRenderer.getWidth(titleText); // 텍스트 너비 계산
        int titleX = this.width / 2 - titleWidth / 2; // 텍스트 X 좌표 (가운데 정렬)
        int titleY = 30; // 텍스트 Y 좌표 (상단에서부터의 거리)
        context.drawText(textRenderer, titleText, titleX, titleY, 0xFFFFFFFF, true); // 텍스트 그리기

        // 아이템 슬롯 그리기
        int iconOffset = 2;
        for (int i = 0; i < 105; i ++) {
            Rectangle rectangle = itemSlotPositions.get(i);
            int x = rectangle.x;
            int y = rectangle.y;
            int slotBoxSize = rectangle.width;
            int stackIndex = (currentPage * 105) + i;

            // 도감 달성 여부를 확인하여, 도감을 달성했다면 초록색 배경을 칠해줍니다.
            if (stackIndex < codexArray.length && codexArray[stackIndex] == 1) {
                context.fill(x, y, x + slotBoxSize, y + slotBoxSize, 0xFF006400);
            } else {
                context.fill(x, y, x + slotBoxSize, y + slotBoxSize, 0x88555555);
            }

            if (stackIndex >= itemStacks.size()) {
                stackIndex = itemStacks.size() - 1;
            }
            ItemStack itemStack = itemStacks.get(stackIndex);
            // 마우스가 아이템 슬롯 위에 있는지 확인
            if (mouseX >= x && mouseX <= x + slotBoxSize && mouseY >= y && mouseY <= y + slotBoxSize) {
                this.setTooltip(itemTooltips.get(stackIndex));
            }

            // 아이템 이미지 렌더링
            context.drawItem(itemStack, x + iconOffset, y + iconOffset);

        }

        Tooltip tooltip = Tooltip.of(Text.of("현재 보상 :" + NumberFormat.getInstance().format(ClientPlayerDataManager.playerData.calculCodexReward()) + " kc"));
        rewardButtonWidget.setTooltip(tooltip);
    }

    // 화살표 클릭 이벤트 처리
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if (button == 0) {
            // 페이지 버튼 클릭시 페이지 이동
            if (mouseX >= arrowPositions[0].x
                    && mouseX <= arrowPositions[0].x + arrowPositions[0].width
                    && mouseY >= arrowPositions[0].y
                    && mouseY <= arrowPositions[0].y + arrowPositions[0].height) {
                changePage(-1); // 이전 페이지로
                return true;
            } else if (mouseX >= arrowPositions[1].x
                    && mouseX <= arrowPositions[1].x + arrowPositions[1].width
                    && mouseY >= arrowPositions[1].y
                    && mouseY <= arrowPositions[1].y + arrowPositions[1].height) {
                changePage(1); // 다음 페이지로
                return true;
            }

            // 아이템 클릭 시에 도감 채우기 이벤트
            for (int i = 0; i < 105; i ++) {
                Rectangle rectangle = itemSlotPositions.get(i);
                int x = rectangle.x;
                int y = rectangle.y;
                int slotBoxSize = rectangle.width;
                if (mouseX >= x
                        && mouseX <= x + slotBoxSize
                        && mouseY >= y
                        && mouseY <= y + slotBoxSize) {
                    int selectedItemIndex = (this.currentPage * 105) + i;
                    Integer codexItemId = ClientPlayerDataManager.codexList.get(selectedItemIndex);

                    // 서버로 도감 완료 요청을 보냅니다.
                    PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
                    CodexC2SPostPacket.encode(new CodexC2SPostPacket(codexItemId), packetByteBuf);
                    Identifier identifier = new Identifier(MOD_ID, CodexC2SPostPacket.CODEX_POST_PACKET_REQUEST_ID);
                    ClientPlayNetworking.send(identifier, packetByteBuf);
                }
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