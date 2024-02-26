package net.kokochi.kkotycoon.client.screen;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.kokochi.kkotycoon.KkoTycoon;
import net.kokochi.kkotycoon.client.data.CodexSet;
import net.kokochi.kkotycoon.client.packet.CodexC2SPostPacket;
import net.kokochi.kkotycoon.client.packet.CodexS2CGetPacket;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

        // 예제용으로 돌 블럭 텍스처를 띄우도록 수정
        // 나중에 다 바까줘야함
        Identifier identifier = new Identifier("kkotycoon", "textures/block/stone.png");
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
            // TODO :: 당장 표기가 헷갈려서 그냥 drawItem을 씀. 나중에 도감 완성되면 drawTexture로 바꿔주고, 아이템 텍스처 작업을 따로 해줘야함.
//            context.drawTexture(identifier, x + iconOffset, y + iconOffset, 16, 16, 0, 0, 16, 16, 16, 16);
            context.drawItem(itemStack, x + iconOffset, y + iconOffset);
            // 도감 달성을 위해 필요한 개수 지정 (일단 디폴드로 10개로 설정)
            context.drawText(textRenderer, "10", x + 8, y + 10, 0xFFFFFFFF, true); // 텍스트 그리기
        }
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
                    // 먼저 사용자 도감 정보를 조회해서 이미 도감이 완료된 상태인지를 파악합니다.
                    NbtCompound nbtCompound = new NbtCompound();
                    this.client.player.writeNbt(nbtCompound);
                    byte[] codexArray = nbtCompound.getByteArray(CodexS2CGetPacket.CODEX_LIST_NBT_KEY);
                    CodexSet[] codexSetsArray = CodexSet.values();
                    int selectedItemIndex = (this.currentPage * 105) + i;

                    ItemStack itemStack = itemStacks.get(selectedItemIndex);
                    // 이미 등록된 아이템인지 여부도 서버에서 검증합니다.
//                    for (int codexIndex : codexArray) {
//                        CodexSet codexSet = codexSetsArray[codexIndex];
//                        if (Item.getRawId(codexSet.getItem()) == Item.getRawId(itemStack.getItem())
//                                && codexArray[codexIndex] == 1) {
//                            // 이미 도감이 완료된 상태이면 이미 완료되었다는 메세지를 띄웁니다.
//                            this.client.player.sendMessage(Text.of("이미 도감에 등록된 아이템입니다."));
//                            return true;
//                        }
//                    }

                    PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
                    CodexC2SPostPacket.encode(new CodexC2SPostPacket(codexSetsArray[selectedItemIndex]), packetByteBuf);
                    Identifier identifier = new Identifier(KkoTycoon.MOD_ID, CodexC2SPostPacket.CODEX_POST_PACKET_REQUEST_ID);
                    ClientPlayNetworking.send(identifier, packetByteBuf);

                    // 도감이 완료된 상태가 아니라면, 아이템 개수가 충분한지 체크합니다.
                    // 개수가 충분한지와 아이템 소모 처리는 서버에서 처리합니다.
//                    PlayerInventory inventory = this.client.player.getInventory();
//                    ItemStack targetItem = itemStacks.get(i);
//                    List<ItemStack> stacks = new ArrayList<>();
//                    for (int inventorySlot = 0; inventorySlot < this.client.player.getInventory().size(); inventorySlot++) {
//                        ItemStack stack = inventory.getStack(inventorySlot);
//                        if (areItemStacksEqual(targetItem, stack)) {
//                            stacks.add(stack);
//                        }
//                    }

//                    if (stacks.stream().mapToInt(ItemStack::getCount).sum() >= 10) {
//                        // 소지 개수가 충분하다면 도감을 등록하고, 해당 아이템을 제거해줍니다.
//
//                        // 도감 등록 정보를 서버로 보냅니다.
//                        nbtCompound.putBoolean(itemCodexKey, true);
//                        this.client.player.sendMessage(Text.of("도감에 \"" + targetItem.getName().toString() + "\" 이 등록되었습니다."));
//                        int itemRemoveCount = 10;
//                        for (ItemStack stack : stacks) {
//                            if (stack.getCount() <= itemRemoveCount) {
//                                // 한 아이템에 스택이 초과로 갖고있다면 해당 스택을 일단 다 가져가고 남은 아이템을 다시 지급합니다.
//                                int remainedCount = stack.getCount() - itemRemoveCount;
//                                this.client.player.getInventory().removeOne(stack);
//                                this.client.player.getInventory().insertStack(new ItemStack(targetItem.getItem(), remainedCount));
//                                itemRemoveCount = 0;
//                                break;
//                            } else {
//                                itemRemoveCount -= stack.getCount();
//                                this.client.player.getInventory().removeOne(stack);
//                            }
//                        }
//                    } else {
//                        // 갯수가 부족하다면 갯수가 부족하다는 내용의 메세지를 보냅니다.
//                        this.client.player.sendMessage(Text.of("도감에 등록하기 위한 아이템 갯수가 부족합니다."));
//                    }
                }
            }
        }



        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean areItemStacksEqual(ItemStack stack1, ItemStack stack2) {
        // 둘 다 비어있는 경우 동일하다고 판단
        if (stack1.isEmpty() && stack2.isEmpty()) {
            return true;
        }

        // 하나만 비어있는 경우 다르다고 판단
        if (stack1.isEmpty() || stack2.isEmpty()) {
            return false;
        }

        // 아이템 유형 비교
        if (stack1.getItem() != stack2.getItem()) {
            return false;
        }

        // 손상 값(메타데이터) 비교
        if (stack1.getDamage() != stack2.getDamage()) {
            return false;
        }

        // NBT 데이터 비교 (NBT 데이터가 모두 있는 경우에만 비교)
        if (stack1.hasNbt() && stack2.hasNbt()) {
            return stack1.getNbt().equals(stack2.getNbt());
        }

        // NBT 데이터가 없는 경우 위의 조건들을 모두 통과하면 동일하다고 판단
        return true;
    }



    // esc를 눌러서 닫을 수 있는지 여부 설정
    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}