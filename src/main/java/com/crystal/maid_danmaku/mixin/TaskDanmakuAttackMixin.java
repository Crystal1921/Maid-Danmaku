package com.crystal.maid_danmaku.mixin;

import com.crystal.maid_danmaku.danmaku.DanmakuAttack;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskDanmakuAttack;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TaskDanmakuAttack.class)
public class TaskDanmakuAttackMixin {
    /**
     * @author Crystal1921
     * @reason Better Danmaku Performances
     */
    @Overwrite
    public void performRangedAttack(EntityMaid shooter, LivingEntity target, float distanceFactor) {
        DanmakuAttack.performDanmakuAttack(shooter, target, distanceFactor);
    }
}
