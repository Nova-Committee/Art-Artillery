package committee.nova.plr.aa2.common.events.dispenser;

import committee.nova.plr.aa2.common.entity.impl.FlakCannonProjectile;
import committee.nova.plr.aa2.common.entity.impl.ShellProjectile;
import committee.nova.plr.aa2.common.entity.init.EntityInit;
import committee.nova.plr.aa2.common.item.init.ItemInit;
import committee.nova.plr.aa2.common.sound.init.SoundInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DispenserRegistry {
    @SubscribeEvent
    public static void onDispenserRegister(FMLCommonSetupEvent event) {
        DispenserBlock.registerBehavior(ItemInit.genericShell.get(), new DefaultDispenseItemBehavior() {
            @Override
            @Nonnull
            public ItemStack execute(@Nonnull BlockSource source, @Nonnull ItemStack stack) {
                final Vec3i d = source.getBlockState().getValue(DispenserBlock.FACING).getNormal();
                final Vec3i v = new Vec3i(d.getX() * 2, 1, d.getZ() * 2);
                final BlockPos pos = source.getPos().offset(d);
                final Level level = source.getLevel();
                final ShellProjectile shell = new ShellProjectile(EntityInit.shell.get(), pos.getX(), pos.getY(), pos.getZ(), level);
                shell.shoot(v.getX(), v.getY(), v.getZ(), ShellProjectile.power, 0);
                shell.setDeltaMovement(v.getX(), v.getY(), v.getZ());
                shell.setSilent(true);
                shell.setCritArrow(false);
                level.addFreshEntity(shell);
                return decrement(stack);
            }
        });
        DispenserBlock.registerBehavior(ItemInit.flakCannon.get(), new DefaultDispenseItemBehavior() {
            @Override
            @Nonnull
            public ItemStack execute(@Nonnull BlockSource source, @Nonnull ItemStack stack) {
                final Direction dir = source.getBlockState().getValue(DispenserBlock.FACING);
                if (dir != Direction.UP) {
                    return stack;
                }
                final BlockPos pos = source.getPos().above();
                final double x = pos.getX() + 0.5D;
                final double y = pos.getY();
                final double z = pos.getZ() + 0.5D;
                final Level level = source.getLevel();
                final FlakCannonProjectile cannon = new FlakCannonProjectile(EntityInit.flakCannon.get(), x, y, z, level, 40);
                cannon.shoot(0, 1, 0, 2, 0);
                cannon.setNoGravity(true);
                cannon.setSilent(true);
                cannon.setCritArrow(false);
                level.addFreshEntity(cannon);
                level.playSound(null, x, y, z,
                        SoundInit.FLAK_LAUNCH, SoundSource.BLOCKS, 1,
                        1f);
                return decrement(stack);
            }
        });


    }

    private static ItemStack decrement(ItemStack stack) {
        final int count = stack.getCount() - 1;
        if (count > 0) {
            stack.setCount(count);
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }
}
