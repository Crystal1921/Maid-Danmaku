package com.crystal.maid_danmaku.danmaku;

import com.github.tartaricacid.touhoulittlemaid.datagen.EnchantmentKeys;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemHakureiGohei;
import dev.xkmc.danmakuapi.content.entity.ItemBulletEntity;
import dev.xkmc.danmakuapi.content.spell.spellcard.CardHolder;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static com.github.tartaricacid.touhoulittlemaid.datagen.EnchantmentKeys.getEnchantmentLevel;

public class DanmakuAttack {
    private static final int DANMAKU_LIFE = 100;

    public static void performDanmakuAttack(EntityMaid shooter, LivingEntity target, float distanceFactor) {
        shooter.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).ifPresent(livingEntities -> {
            ItemStack mainHandItem = shooter.getMainHandItem();
            if (ItemHakureiGohei.isGohei(mainHandItem)) {
                CardHolder cardHolder = (CardHolder) shooter;
                long entityCount = livingEntities.stream().filter(test -> enemyEntityTest(shooter, target, test)).count();
                Level level = shooter.level();
                AttributeInstance attackDamage = shooter.getAttribute(Attributes.ATTACK_DAMAGE);
                float attackValue = 2.0f;
                if (attackDamage != null) {
                    attackValue = (float) attackDamage.getBaseValue();
                }
                RegistryAccess access = shooter.level().registryAccess();

                int impedingLevel = getEnchantmentLevel(access, EnchantmentKeys.IMPEDING, mainHandItem);
                int speedyLevel = getEnchantmentLevel(access, EnchantmentKeys.SPEEDY, mainHandItem);
                int multiShotLevel = getEnchantmentLevel(access, Enchantments.MULTISHOT, mainHandItem);
                int endersEnderLevel = getEnchantmentLevel(access, EnchantmentKeys.ENDERS_ENDER, mainHandItem);
                float speed = (0.3f * (distanceFactor + 1)) * (speedyLevel + 1);
                boolean hurtEnderman = endersEnderLevel > 0;

                // 依据距离调整弹幕速度和不准确度
                float distance = shooter.distanceTo(target);
                speed = speed + Mth.clamp(distance / 40f - 0.4f, 0, 2.4f);
                float inaccuracy = 1 - Mth.clamp(distance / 100f, 0, 0.8f);

                Vec3 scale = target.position().subtract(cardHolder.center()).normalize().scale(speed);

                ItemBulletEntity itemBulletEntity = cardHolder.prepareDanmaku(DANMAKU_LIFE, scale, DanmakuItems.Bullet.STAR, DyeColor.RED);
                cardHolder.shoot(itemBulletEntity);


                mainHandItem.hurtAndBreak(1, shooter, EquipmentSlot.MAINHAND);
            }
        });
    }

    private static boolean enemyEntityTest(EntityMaid shooter, LivingEntity target, LivingEntity test) {
        boolean canAttack = shooter.canAttack(test);
        boolean sameType = target.getType().equals(test.getType());
        return canAttack && sameType && shooter.canSee(test);
    }
}
