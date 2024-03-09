package net.kokochi.kkotycoon.client.screen;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.kokochi.kkotycoon.KkoTycoon;
import net.kokochi.kkotycoon.entity.codex.CodexInfo;
import net.kokochi.kkotycoon.entity.player.ClientPlayerDataManager;
import net.kokochi.kkotycoon.entity.player.KkotycoonPlayerData;
import net.kokochi.kkotycoon.entity.product.KkoShopProduct;
import net.kokochi.kkotycoon.entity.product.KkoShopProductDataManager;
import net.kokochi.kkotycoon.packet.CodexC2SPostPacket;
import net.kokochi.kkotycoon.packet.ShopScreenPurchaseC2SPacket;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static net.kokochi.kkotycoon.KkoTycoon.MOD_ID;

public class ShopScreen extends Screen {
    public ShopScreen(Text title) {
        super(title);
    }

    // 맨 위아래 padding
    public int outSidePaddingY;

    // box끼리 margin
    public int boxMarginY;

    // box 크기
    public int boxWidth;
    public int boxHeight;
    public int boxPosX;

    public int itemOutSidePadding;
    public int itemOutSidePos;
    public int itemOutSideHeight;
    public int itemOutSideMargin;

    public int purchaseButtonWidth;
    public int purchaseButtonHeight;

    private Rectangle[] arrowPositions;
    private final int MAX_PAGE = 2;
    private int currentPage = 0;

    private List<ButtonWidget> buttonWidgets;

    @Override
    protected void init() {
        super.init();

        // 맨 위아래 padding
        outSidePaddingY = 15;

        // box끼리 margin
        boxMarginY = 10;

        // box 크기
        boxWidth = 150;
        boxHeight = (this.height - (outSidePaddingY * 2) - (boxMarginY * 3)) / 4;
        boxPosX = (this.width / 2) - (boxWidth / 2);

        itemOutSidePadding = (boxHeight / 2) - 8;
        itemOutSidePos = 8;
        itemOutSideHeight = boxHeight - (itemOutSidePos * 2);
        itemOutSideMargin = 5;

        purchaseButtonWidth = 35;
        purchaseButtonHeight = 12;

        // 버튼 위젯 그리기
        addButtonWidgets();

        int leftArrowX = 10; // 화살표 위치 조정 필요
        int rightArrowX = this.width - 30; // 화살표 위치 조정 필요
        int arrowSize = 20;
        int arrowY = this.height / 2 - 10; // 화살표 위치 조정 필요
        // 도감 왼쪽, 오른쪽 화살표
        arrowPositions = new Rectangle[2];
        arrowPositions[0] = new Rectangle(leftArrowX, arrowY, arrowSize, arrowSize);
        arrowPositions[1] = new Rectangle(rightArrowX, arrowY, arrowSize, arrowSize);
    }

    public boolean isFirstPurchaseToday(KkotycoonPlayerData playerData) {
        LocalDateTime lastPurchaseDate = playerData.getLastPurchaseProductDate();

        // 마지막 구매 날짜가 null인 경우, 오늘 최초 구매로 간주
        if (lastPurchaseDate == null) {
            return true;
        }

        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = LocalDateTime.of(today, LocalTime.of(5, 0)); // 오늘 새벽 5시

        // 마지막 구매 날짜가 오늘 새벽 5시 이전이거나, 오늘이고 새벽 5시 이전인 경우 최초 구매로 간주
        return lastPurchaseDate.isBefore(todayStart) ||
                (lastPurchaseDate.toLocalDate().isEqual(today) && lastPurchaseDate.toLocalTime().isBefore(LocalTime.of(5, 0)));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);

        // 화살표 버튼 그리기
        Identifier leftArrowIdentifier = new Identifier(MOD_ID, "textures/gui/arrow.png");
        Identifier rightArrowIdentifier = new Identifier(MOD_ID, "textures/gui/right_arrow.png");

        context.fill(arrowPositions[0].x, arrowPositions[0].y, arrowPositions[0].x + arrowPositions[0].width, arrowPositions[0].y + arrowPositions[0].height, 0x90FFFFFF); // 왼쪽 화살표 (색상 변경 필요)
        context.fill(arrowPositions[1].x, arrowPositions[1].y, arrowPositions[1].x + arrowPositions[1].width, arrowPositions[1].y + arrowPositions[1].height, 0x90FFFFFF); // 오른쪽 화살표 (색상 변경 필요)
        context.drawTexture(leftArrowIdentifier, arrowPositions[0].x, arrowPositions[0].y, 0, 0, 20, 20, 20, 20);
        context.drawTexture(rightArrowIdentifier, arrowPositions[1].x, arrowPositions[1].y, 0, 0, 20, 20, 20, 20);

        List<KkoShopProduct> productList = KkoShopProductDataManager.PRODUCT_LIST;
        for (int i = 0; i < 4; i ++) {
            int selectedItemIndex = (this.currentPage * 4) + i;
            if (selectedItemIndex >= productList.size()) {
                break;
            }
            KkoShopProduct kkoShopProduct = productList.get(selectedItemIndex);

            // 아이템 박스 배경
            int boxPosY = outSidePaddingY + (i * (boxHeight + boxMarginY));
            context.fill(boxPosX, boxPosY, boxPosX + boxWidth, boxPosY + boxHeight, 0x15AFF8DF);

            // 아이템 박스의 아이템 이미지
            context.drawItem(new ItemStack(kkoShopProduct.getItem()), boxPosX + itemOutSidePadding, boxPosY + itemOutSidePadding);
            // 아이템 박스의 아이템 이미지 테두리 뒷배경
            context.fill(boxPosX + itemOutSidePos,
                    boxPosY + itemOutSidePos,
                    boxPosX + itemOutSidePos + itemOutSideHeight,
                    boxPosY + itemOutSidePos + itemOutSideHeight,
                    0xA0000000);

            // 아이템 이름
            context.drawText(textRenderer, kkoShopProduct.getItem().getName(),
                    boxPosX + itemOutSidePos + itemOutSideHeight + itemOutSideMargin, boxPosY + itemOutSidePos, 0xFFFFFF, true);

            // 가격 텍스트
            context.drawText(textRenderer, Text.of("가격 :§6" + NumberFormat.getInstance().format(kkoShopProduct.getPrice()) + "kc"),
                    boxPosX + itemOutSidePos + itemOutSideHeight + itemOutSideMargin, boxPosY + itemOutSidePos + 12, 0xFFFFFF, true);
        }
    }

    // 페이지 변경 메서드
    private void changePage(int direction) {
        this.currentPage += direction;

        if (this.currentPage < 0) {
            this.currentPage = 0;
        } else if (this.currentPage >= MAX_PAGE) {
            this.currentPage = MAX_PAGE - 1;
        } else {
            this.clearChildren();
            addButtonWidgets();
        }
    }

    private void addButtonWidgets() {
        // 구매 버튼
        List<KkoShopProduct> productList = KkoShopProductDataManager.PRODUCT_LIST;
        buttonWidgets = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int boxPosY = outSidePaddingY + (i * (boxHeight + boxMarginY));
            int selectedItemIndex = (this.currentPage * 4) + i;
            if (selectedItemIndex >= productList.size()) {
                break;
            }
            KkoShopProduct kkoShopProduct = productList.get(selectedItemIndex);
            ButtonWidget purchaseButtonWidget = ButtonWidget.builder(Text.of("구매"), button -> {
                if (button.isSelected()) {
                    // 수령할 보상이 없다면 서버로 요청을 보내지 않습니다. (과부하 방지용)
                    if (button.isSelected()) {
                        int emptySlot = this.client.player.getInventory().getEmptySlot();
                        if (emptySlot == -1) {
                            this.client.player.sendMessage(Text.of("인벤토리 창이 꽉 차있습니다."));
                            return;
                        }

                        PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
                        ShopScreenPurchaseC2SPacket.encode(new ShopScreenPurchaseC2SPacket(kkoShopProduct.getProductId()), packetByteBuf);
                        Identifier identifier = new Identifier(KkoTycoon.MOD_ID, ShopScreenPurchaseC2SPacket.SHOP_PURCHASE_SCREEN_REQUEST_ID);
                        ClientPlayNetworking.send(identifier, packetByteBuf);
                    }
                }
            }).dimensions(boxPosX + boxWidth - purchaseButtonWidth - 8,
                    boxPosY + itemOutSidePos + 24, purchaseButtonWidth, purchaseButtonHeight).build();
            buttonWidgets.add(purchaseButtonWidget);
            // 버튼 위젯 그리기
            addDrawableChild(buttonWidgets.get(i));
        }
    }

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
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }


    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
