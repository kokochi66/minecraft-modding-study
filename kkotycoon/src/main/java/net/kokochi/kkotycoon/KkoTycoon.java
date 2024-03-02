package net.kokochi.kkotycoon;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.kokochi.kkotycoon.entity.item.KkoTycoonItems;
import net.kokochi.kkotycoon.server.handler.CommandHandler;
import net.kokochi.kkotycoon.server.handler.PlayerActionEventHandler;
import net.kokochi.kkotycoon.server.handler.ServerRequestHandler;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KkoTycoon implements ModInitializer {
    public static final String MOD_ID = "kkotycoon";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    @Override
    public void onInitialize() {

        ServerRequestHandler.initDataRequestHandler();
        ServerRequestHandler.initCodexRequestHandler();

        // 명령어 생성
        CommandHandler.initCommand();

        // 아이템 추가
        KkoTycoonItems.initModItems();

		// 상인과 상호작용 불가능
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			// 마을 주민과 상호작용하는 경우
			if (entity instanceof VillagerEntity) {
				// 거래 UI가 열리지 않도록 이벤트를 취소
                player.sendMessage(Text.of("§d꼬타이쿤§f에서 마을 주민§f과의 거래는 §c금지§f됩니다."));
				return ActionResult.FAIL;
			}
			// 다른 엔티티와의 상호작용은 기본 동작을 유지
			return ActionResult.PASS;
		});
    }
}