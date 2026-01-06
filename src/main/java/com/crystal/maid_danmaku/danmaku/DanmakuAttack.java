package com.crystal.maid_danmaku.danmaku;

import com.github.tartaricacid.touhoulittlemaid.datagen.EnchantmentKeys;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.ItemHakureiGohei;
import dev.xkmc.danmakuapi.api.DanmakuBullet;
import dev.xkmc.danmakuapi.content.entity.ItemBulletEntity;
import dev.xkmc.danmakuapi.content.spell.spellcard.CardHolder;
import dev.xkmc.danmakuapi.init.registrate.DanmakuEntities;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.datagen.EnchantmentKeys.getEnchantmentLevel;

public class DanmakuAttack {
    private static final int DANMAKU_LIFE = 100;
    private static final RandomSource random = RandomSource.create();
    private static final DyeColor[] dyeColors = DyeColor.values();

    public static void performDanmakuAttack(EntityMaid shooter, LivingEntity target, float distanceFactor) {
        shooter.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).ifPresent(livingEntities -> {
            ItemStack mainHandItem = shooter.getMainHandItem();
            if (ItemHakureiGohei.isGohei(mainHandItem)) {
                CardHolder cardHolder = (CardHolder) shooter;
                long entityCount = livingEntities.stream().filter(test -> enemyEntityTest(shooter, target, test)).count();
                AttributeInstance attackDamage = shooter.getAttribute(Attributes.ATTACK_DAMAGE);
                float attackValue = 2.0f;
                if (attackDamage != null) {
                    attackValue = (float) attackDamage.getBaseValue();
                }
                RegistryAccess access = shooter.level().registryAccess();

                int speedyLevel = getEnchantmentLevel(access, EnchantmentKeys.SPEEDY, mainHandItem);
                int multiShotLevel = getEnchantmentLevel(access, Enchantments.MULTISHOT, mainHandItem);
                float speed = (0.3f * (distanceFactor + 1)) * (speedyLevel + 1);

                // 依据距离调整弹幕速度和不准确度
                float distance = shooter.distanceTo(target);
                speed = speed + Mth.clamp(distance / 40f - 0.4f, 0, 2.4f);

                Vec3 baseDir = target.position()
                        .subtract(cardHolder.center())
                        .normalize()
                        .scale(speed * 2);

                // ===== 发射逻辑 =====
                if (entityCount <= 1) {
                    if (multiShotLevel > 0) {
                        // 三连扇形
                        shootFan(cardHolder, baseDir, 3, 0.2617994f,
                                attackValue * (distanceFactor + 1.2f));
                    } else {
                        // 单发精准
                        ItemBulletEntity bullet = prepareDanmaku(
                                cardHolder,
                                DANMAKU_LIFE,
                                baseDir,
                                DanmakuItems.Bullet.STAR,
                                getRandomColor(),
                                attackValue * (distanceFactor + 1.0f)
                        );
                        cardHolder.shoot(bullet);
                    }
                } else if (entityCount <= 5) {
                    // 中等数量敌人
                    shootFan(cardHolder, baseDir, 8, (float) (Math.PI / 3),
                            attackValue * (distanceFactor + 1.2f));
                } else {
                    // 大量敌人
                    shootFan(cardHolder, baseDir, 32, 2.0943951f,
                            attackValue * (distanceFactor + 1.5f));
                }

                mainHandItem.hurtAndBreak(1, shooter, EquipmentSlot.MAINHAND);
            }
        });
    }

    private static void shootFan(
            CardHolder shooter,
            Vec3 baseDir,
            int count,
            float yawTotal,
            float damage
    ) {
        float half = yawTotal / 2f;

        for (int i = 0; i < count; i++) {
            float progress = count == 1 ? 0f : (float) i / (count - 1);
            float yaw = Mth.lerp(progress, -half, half);

            Vec3 dir = baseDir.yRot(yaw);

            ItemBulletEntity bullet = prepareDanmaku(
                    shooter,
                    DANMAKU_LIFE,
                    dir,
                    DanmakuItems.Bullet.BALL,
                    getRandomColor(),
                    damage
            );
            shooter.shoot(bullet);
        }
    }

    private static boolean enemyEntityTest(EntityMaid shooter, LivingEntity target, LivingEntity test) {
        boolean canAttack = shooter.canAttack(test);
        boolean sameType = target.getType().equals(test.getType());
        return canAttack && sameType && shooter.canSee(test);
    }

    private static @NotNull ItemBulletEntity prepareDanmaku(CardHolder cardHolder, int life, @NotNull Vec3 vec, DanmakuBullet type, @NotNull DyeColor color, float attackValue) {
        ItemBulletEntity danmaku = new ItemBulletEntity(DanmakuEntities.ITEM_DANMAKU.get(), cardHolder.self(), cardHolder.self().level());
        danmaku.setItem(type.get(color).asStack());
        danmaku.setup(attackValue, life, true, true, vec);
        danmaku.setPos(cardHolder.center());
        return danmaku;
    }

    private static DyeColor getRandomColor() {
        return dyeColors[random.nextInt(dyeColors.length)];
    }
}
