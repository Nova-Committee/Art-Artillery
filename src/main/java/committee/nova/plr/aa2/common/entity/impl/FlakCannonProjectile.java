package committee.nova.plr.aa2.common.entity.impl;

import committee.nova.plr.aa2.common.config.CommonConfig;
import committee.nova.plr.aa2.common.entity.init.EntityInit;
import committee.nova.plr.aa2.common.item.init.ItemInit;
import committee.nova.plr.aa2.common.sound.init.SoundInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
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

import static committee.nova.plr.aa2.common.tool.misc.ParticleReference.smoke_ball;

public class FlakCannonProjectile extends AbstractArrow implements ItemSupplier {
    public static final DamageSource SHOT_BY_FLAK = new DamageSource("shotByFlak").setProjectile().bypassMagic();
    private final int remainTime = CommonConfig.FLAK_REMAIN_TIME.get();
    private final float directDamage = CommonConfig.FLAK_DIRECT_DAMAGE.get().floatValue();
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
        flakCannon.shoot(entity.getLookAngle().x, entity.getLookAngle().y, entity.getLookAngle().z, 2, 0);
        flakCannon.setNoGravity(true);
        flakCannon.setSilent(true);
        flakCannon.setCritArrow(false);
        world.addFreshEntity(flakCannon);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                SoundInit.FLAK_LAUNCH, SoundSource.PLAYERS, 1,
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
    public ItemStack getItem() {
        return new ItemStack(ItemInit.flakCannon.get());
    }

    @Nonnull
    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void doPostHurtEffects(@Nonnull LivingEntity entity) {
        super.doPostHurtEffects(entity);
        entity.hurt(SHOT_BY_FLAK, Math.max(0F, directDamage - directDamage * (tickCount - fuseTime - 80) / remainTime));
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        final boolean timeOut = tickCount > fuseTime + remainTime + 75;
        if (this.inGround || this.isUnderWater() || this.onGround || timeOut) {
            if (!timeOut)
                level.explode(this, this.getX(), this.getY(), this.getZ(), 1F, Explosion.BlockInteraction.NONE);
            this.discard();
        } else if (tickCount > fuseTime) {
            final Vec3i pos = new Vec3i(this.getX(), this.getY(), this.getZ());
            if (tickCount == fuseTime + 70) {
                this.level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundInit.FLAK_EXPLODE, SoundSource.PLAYERS, 1F, 1F);
            }
            this.setDeltaMovement(0, 0, 0);
            generateParticle(level, pos);
            if (tickCount > fuseTime + 75) {
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
        for (final Vec3 v : smoke_ball) {
            level.addParticle(ParticleTypes.LARGE_SMOKE,
                    pos.getX() + v.x,
                    pos.getY() + v.y,
                    pos.getZ() + v.z,
                    0, 0, 0);
        }
    }

}
