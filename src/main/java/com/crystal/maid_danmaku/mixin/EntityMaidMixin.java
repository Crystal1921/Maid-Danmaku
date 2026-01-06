package com.crystal.maid_danmaku.mixin;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import dev.xkmc.danmakuapi.api.DanmakuBullet;
import dev.xkmc.danmakuapi.api.DanmakuLaser;
import dev.xkmc.danmakuapi.content.entity.ItemBulletEntity;
import dev.xkmc.danmakuapi.content.entity.ItemLaserEntity;
import dev.xkmc.danmakuapi.content.spell.spellcard.CardHolder;
import dev.xkmc.danmakuapi.init.registrate.DanmakuEntities;
import dev.xkmc.fastprojectileapi.entity.SimplifiedProjectile;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityMaid.class)
public class EntityMaidMixin implements CardHolder  {
    @Override
    public @NotNull Vec3 center() {
        return ((EntityMaid)(Object)(this)).position();
    }

    @Override
    public @NotNull Vec3 forward() {
        Vec3 target = target();
        if (target != null) {
            return target.subtract(center()).normalize();
        } else return Vec3.ZERO;
    }

    @Override
    public @Nullable Vec3 target() {
        LivingEntity owner = ((EntityMaid) (Object) (this)).getOwner();
        if (owner != null) {return owner.position();}
        else {return null;}
    }

    @Override
    public @NotNull RandomSource random() {
        return ((EntityMaid)(Object)(this)).getRandom();
    }

    @Override
    public @NotNull ItemBulletEntity prepareDanmaku(int life, @NotNull Vec3 vec, DanmakuBullet type, @NotNull DyeColor color) {
        ItemBulletEntity danmaku = new ItemBulletEntity(DanmakuEntities.ITEM_DANMAKU.get(), (EntityMaid)(Object)(this), ((EntityMaid)(Object)(this)).level());
        danmaku.setItem(type.get(color).asStack());
        danmaku.setup((float)type.damage(), life, true, true, vec);
        danmaku.setPos(this.center());
        return danmaku;
    }

    @Override
    public @NotNull ItemLaserEntity prepareLaser(int life, @NotNull Vec3 pos, @NotNull Vec3 vec, int len, DanmakuLaser type, @NotNull DyeColor color) {
        ItemLaserEntity danmaku = new ItemLaserEntity(DanmakuEntities.ITEM_LASER.get(), (EntityMaid)(Object)(this), ((EntityMaid)(Object)(this)).level());
        danmaku.setItem(type.get(color).asStack());
        danmaku.setup((float)type.damage(), life, (float)len, true, vec);
        danmaku.setPos(pos);
        return danmaku;
    }

    @Override
    public void shoot(@NotNull SimplifiedProjectile simplifiedProjectile) {
        ((EntityMaid)(Object)(this)).level().addFreshEntity(simplifiedProjectile);
    }

    @Override
    public @NotNull LivingEntity self() {
        return (EntityMaid)(Object)(this);
    }

    @Override
    public @Nullable Vec3 targetVelocity() {
        return null;
    }
}
