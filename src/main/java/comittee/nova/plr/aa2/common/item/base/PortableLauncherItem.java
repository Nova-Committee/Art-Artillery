package comittee.nova.plr.aa2.common.item.base;

import comittee.nova.plr.aa2.client.creativeTab.TabInit;
import comittee.nova.plr.aa2.common.entity.impl.ShellProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

import static comittee.nova.plr.aa2.common.tool.player.PlayerHandler.*;

public class PortableLauncherItem extends Item {
    private final int reloadTime = 60;
    private final int magazine;

    public PortableLauncherItem(int magazine) {
        super(new Properties().durability(300 * magazine).fireResistant().tab(TabInit.AA2_TAB));
        this.magazine = magazine;
    }

    public static void launchShell(ItemStack stack, Player player) {
        final Level level = player.level;
        if (!level.isClientSide) {
            final ShellProjectile shell = ShellProjectile.shoot(level, player, true);
            stack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(player.getUsedItemHand()));
            shell.pickup = AbstractArrow.Pickup.DISALLOWED;
            player.getCooldowns().addCooldown(stack.getItem(), 20);
        }
    }

    public static void initializeNbt(CompoundTag tag, int magazine) {
        if (!tag.contains(CURRENT)) tag.putInt(CURRENT, (int) 0);
        if (!tag.contains(MAX)) tag.putInt(MAX, magazine);
    }

    public int getUseDuration(@Nonnull ItemStack stack) {
        return 72000;
    }

    @Nonnull
    public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (player.getCooldowns().isOnCooldown(stack.getItem())) {
                return true;
            }
            ;
            if (consumeShell(stack, player)) launchShell(stack, player);
        }
        return true;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        return true;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        return true;
    }

    public int reloadable(ItemStack stack, Player player) {
        final int c = stack.getOrCreateTag().getInt(CURRENT);
        return (c < magazine) ? 0 : 1;
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int i, boolean bool) {
        initializeNbt(stack.getOrCreateTag(), magazine);
    }

    @Override
    public void onCraftedBy(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Player player) {
        initializeNbt(stack.getOrCreateTag(), magazine);
    }

    public void load(CompoundTag tag, Player player) {
        tag.putInt(CURRENT, tag.getInt(CURRENT) + 1);
        if (player.level.isClientSide) {
            player.playSound(SoundEvents.WOODEN_BUTTON_CLICK_OFF, 1f, 1f);
        }
    }
}
