package net.kokochi.kkotycoon.entity.villager;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class KkoTrader extends VillagerEntity {
    public KkoTrader(EntityType<? extends VillagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        // 그냥 가만히 서있는 주민이다.
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        player.sendMessage(Text.of("상인에게 말을 걸었다."));
        return ActionResult.PASS;
    }
}
