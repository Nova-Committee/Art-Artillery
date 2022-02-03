package comittee.nova.plr.aa2.common.entity.impl;

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
    public static final float power = 1.0F;
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
        final float xO = isAccurate ? 0 : random.nextFloat() - 0.5F;
        final float yO = isAccurate ? 0 : random.nextFloat() - 0.5F;
        final float zO = isAccurate ? 0 : random.nextFloat() - 0.5F;
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
        entity.hurt(SHOT_BY_SHELL, 5F);
        this.level.explode(this, getX(), getY(), getZ(), 2F, Explosion.BlockInteraction.NONE);
        this.discard();
        //todo:make damage configurable
    }

    @Override
    public void tick() {
        super.tick();
        if (this.inGround || this.isUnderWater()) {
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), 2F, Explosion.BlockInteraction.NONE);
            this.discard();
        }
    }

}

