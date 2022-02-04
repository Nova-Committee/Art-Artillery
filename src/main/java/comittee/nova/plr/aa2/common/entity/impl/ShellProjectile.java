package comittee.nova.plr.aa2.common.entity.impl;

import comittee.nova.plr.aa2.common.config.CommonConfig;
import comittee.nova.plr.aa2.common.entity.init.EntityInit;
import comittee.nova.plr.aa2.common.item.init.ItemInit;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nonnull;
import java.util.Random;

public class ShellProjectile extends AbstractArrow implements ItemSupplier {
    //todo:config
    public static final float power = CommonConfig.SHELL_SPEED.get().floatValue();
    public static final DamageSource SHOT_BY_SHELL = new DamageSource("shotByShell").setProjectile().bypassMagic();

    public ShellProjectile(PlayMessages.SpawnEntity packet, Level world) {
        super(EntityInit.shell.get(), world);
    }

    public ShellProjectile(EntityType<? extends ShellProjectile> type, Level world) {
        super(type, world);
    }

    public ShellProjectile(EntityType<? extends ShellProjectile> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world);
    }

    public ShellProjectile(EntityType<? extends ShellProjectile> type, LivingEntity entity, Level world) {
        super(type, entity, world);
    }

    //todo:more to do with accuracy
    public static ShellProjectile shoot(Level world, LivingEntity entity, boolean isAccurate) {
        final ShellProjectile shell = new ShellProjectile(EntityInit.shell.get(), entity, world);
        final Random random = world.random;
        final float inaccuracy = CommonConfig.INACCURACY_AMPLIFIER.get().floatValue();
        final float xO = isAccurate ? 0 : (random.nextFloat() - 0.5F) * inaccuracy;
        final float yO = isAccurate ? 0 : (random.nextFloat() - 0.5F) * inaccuracy;
        final float zO = isAccurate ? 0 : (random.nextFloat() - 0.5F) * inaccuracy;
        shell.shoot(entity.getLookAngle().x + xO, entity.getLookAngle().y + yO, entity.getLookAngle().z + zO, power * 2, 0);
        shell.setSilent(true);
        shell.setCritArrow(false);
        world.addFreshEntity(shell);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.WOODEN_BUTTON_CLICK_ON, SoundSource.PLAYERS, 1,
                1f);
        return shell;
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
        entity.hurt(SHOT_BY_SHELL, CommonConfig.SHELL_DIRECT_DAMAGE.get().floatValue());
        this.level.explode(this, getX(), getY(), getZ(), CommonConfig.SHELL_HIT_ENTITY_EXPLOSION_POWER.get().floatValue(), getExplosionTypeFromInt(CommonConfig.SHELL_HIT_ENTITY_EXPLOSION_TYPE.get()));
        this.discard();
        //todo:make damage configurable
    }

    @Override
    public void tick() {
        super.tick();
        final boolean timeOut = tickCount > 200;
        if (this.inGround || this.isUnderWater() || timeOut) {
            final float designedPower = CommonConfig.SHELL_HIT_BLOCK_EXPLOSION_POWER.get().floatValue();
            this.level.explode(this, this.getX(), this.getY(), this.getZ(),
                    timeOut ? Math.min(1.0F, designedPower) : designedPower,
                    timeOut ? Explosion.BlockInteraction.NONE : getExplosionTypeFromInt(CommonConfig.SHELL_HIT_BLOCK_EXPLOSION_TYPE.get()));
            this.discard();
        }
    }

    private Explosion.BlockInteraction getExplosionTypeFromInt(int type) {
        switch (type) {
            case 0 -> {
                return Explosion.BlockInteraction.NONE;
            }
            case 1 -> {
                return Explosion.BlockInteraction.BREAK;
            }
            case 2 -> {
                return Explosion.BlockInteraction.DESTROY;
            }
            default -> {
                LOGGER.warn("Unexpected explosion type! Defaulted it to 0 -> NONE.");
                return Explosion.BlockInteraction.NONE;
            }
        }
    }

}

