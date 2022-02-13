package committee.nova.plr.aa2.common.entity.impl;

import committee.nova.plr.aa2.common.entity.init.EntityInit;
import committee.nova.plr.aa2.common.item.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class FlakCannonProjectile extends AbstractArrow implements ItemSupplier {
    public static final DamageSource SHOT_BY_FLAK = new DamageSource("shotByFlak").setExplosion().bypassMagic();
    private static final int remainTime = 160;
    private final int fuseTime;

    public FlakCannonProjectile(PlayMessages.SpawnEntity packet, Level world, int fuseTime) {
        super(EntityInit.shell.get(), world);
        this.fuseTime = fuseTime;
    }

    public FlakCannonProjectile(EntityType<? extends FlakCannonProjectile> type, Level world) {
        super(type, world);
        this.fuseTime = 80;
    }

    public FlakCannonProjectile(EntityType<? extends FlakCannonProjectile> type, double x, double y, double z, Level world, int fuseTime) {
        super(type, x, y, z, world);
        this.fuseTime = fuseTime;
    }

    public FlakCannonProjectile(EntityType<? extends FlakCannonProjectile> type, LivingEntity entity, Level world, int fuseTime) {
        super(type, entity, world);
        this.fuseTime = fuseTime;
    }

    public static FlakCannonProjectile shoot(Level world, LivingEntity entity, int fuseTime) {
        final FlakCannonProjectile flakCannon = new FlakCannonProjectile(EntityInit.flakCannon.get(), entity, world, fuseTime);
        final Random random = world.random;
        flakCannon.shoot(entity.getLookAngle().x, entity.getLookAngle().y, entity.getLookAngle().z, 2, 0);
        flakCannon.setNoGravity(true);
        flakCannon.setSilent(true);
        flakCannon.setCritArrow(false);
        world.addFreshEntity(flakCannon);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.WOODEN_BUTTON_CLICK_ON, SoundSource.PLAYERS, 1,
                1f);
        return flakCannon;
    }

    @Nonnull
    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    @Nonnull
    //todo:own item
    public ItemStack getItem() {
        return new ItemStack(ItemInit.genericShell.get());
    }

    @Nonnull
    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void doPostHurtEffects(@Nonnull LivingEntity entity) {
        super.doPostHurtEffects(entity);
        entity.hurt(SHOT_BY_FLAK, 16F);
        //todo:particle
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        final boolean timeOut = tickCount > fuseTime + remainTime + 80;
        if (this.inGround || this.isUnderWater() || timeOut) {
            if (!timeOut)
                level.explode(this, this.getX(), this.getY(), this.getZ(), 1F, Explosion.BlockInteraction.NONE);
            this.discard();
        } else if (tickCount > fuseTime) {
            this.setDeltaMovement(0, 0, 0);
            final Vec3i pos = new Vec3i(this.getX(), this.getY(), this.getZ());
            generateParticle(level, pos);
            if (tickCount > fuseTime + 80) {
                generateDamage(this.level, pos);
            }
        }
    }

    private void generateDamage(Level level, Vec3i pos) {
        final List<LivingEntity> entityInRange = level.getEntitiesOfClass(LivingEntity.class, new AABB(new BlockPos(pos)).inflate(3));
        for (final LivingEntity entityIn : entityInRange) {
            if (entityIn.getEyePosition().distanceToSqr(Vec3.atLowerCornerOf(pos)) <= 9)
                entityIn.hurt(SHOT_BY_FLAK, 3F);
        }
    }

    private void generateParticle(Level level, Vec3i pos) {
        for (int sx = -5; sx <= 5; sx++) {
            for (int sy = -5; sy <= 5; sy++) {
                for (int sz = -5; sz <= 5; sz++) {
                    final double xO = sx / 1.875D;
                    final double yO = sy / 1.875D;
                    final double zO = sz / 1.875D;
                    if (Math.sqrt(xO * xO + yO * yO + zO * zO) <= (5 / 1.875D))
                        level.addParticle(ParticleTypes.LARGE_SMOKE,
                                pos.getX() + xO,
                                pos.getY() + yO,
                                pos.getZ() + zO, 0, 0, 0);
                }
            }
        }
    }

}
