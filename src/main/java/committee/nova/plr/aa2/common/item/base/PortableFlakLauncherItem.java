package committee.nova.plr.aa2.common.item.base;

import committee.nova.plr.aa2.client.creativeTab.TabInit;
import committee.nova.plr.aa2.common.config.CommonConfig;
import committee.nova.plr.aa2.common.entity.impl.FlakCannonProjectile;
import committee.nova.plr.aa2.common.item.api.IReloadable;
import committee.nova.plr.aa2.common.item.api.IThirdPersonRenderable;
import committee.nova.plr.aa2.common.item.init.ItemInit;
import committee.nova.plr.aa2.common.tool.player.PlayerHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

import static committee.nova.plr.aa2.common.tool.player.PlayerHandler.CURRENT;
import static committee.nova.plr.aa2.common.tool.player.PlayerHandler.MAX;

public class PortableFlakLauncherItem extends Item implements IReloadable, IThirdPersonRenderable {
    private static final int MAX_DRAW_DURATION = 20;
    private final int reloadTime = 10;
    private final int magazine;

    public PortableFlakLauncherItem(int magazine) {
        super(new Properties().durability(300 * magazine).fireResistant().tab(TabInit.AA2_TAB));
        this.magazine = magazine;
    }

    public static void launchCannon(ItemStack stack, Player player, int fuseTime) {
        final Level level = player.level;
        if (!level.isClientSide) {
            final FlakCannonProjectile cannon = FlakCannonProjectile.shoot(level, player, fuseTime);
            cannon.pickup = AbstractArrow.Pickup.DISALLOWED;
            stack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(player.getUsedItemHand()));
            //todo: own CD
            player.getCooldowns().addCooldown(stack.getItem(), CommonConfig.FIRE_CD.get());
        }
    }

    public static void initializeNbt(CompoundTag tag, int magazine) {
        if (!tag.contains(CURRENT)) tag.putInt(CURRENT, 0);
        if (!tag.contains(MAX)) tag.putInt(MAX, magazine);
    }

    public static float getPowerForTime(int tick) {
        return Math.min(2F, tick / 50F);
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int i, boolean bool) {
        initializeNbt(stack.getOrCreateTag(), magazine);
    }

    public int getUseDuration(@Nonnull ItemStack stack) {
        return 72000;
    }

    @Nonnull
    public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public void releaseUsing(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity entity, int time) {
        if (entity instanceof Player player) {
            final boolean hasCannon = PlayerHandler.consumeMagazine(stack, player);
            if (!hasCannon) {
                return;
            }
            final int i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, level, player, this.getUseDuration(stack) - time, true);
            if (i < 0) return;

            final float f = getPowerForTime(i);
            if (!((double) f < 0.1D)) {
                if (!level.isClientSide) {
                    launchCannon(stack, player, (int) (20 * f));
                }
            }
        }
    }

    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        final ItemStack itemstack = player.getItemInHand(hand);
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.pass(itemstack);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    public int reloadable(ItemStack stack, Player player) {
        final int c = stack.getOrCreateTag().getInt(CURRENT);
        if (player.getCooldowns().isOnCooldown(stack.getItem())) {
            return 2;
        }
        return (c < magazine) ? 0 : 1;
    }

    @Override
    public void onCraftedBy(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Player player) {
        initializeNbt(stack.getOrCreateTag(), magazine);
    }

    @Override
    public void load(CompoundTag tag, Player player, Item launcher) {
        tag.putInt(CURRENT, tag.getInt(CURRENT) + 1);
        if (player.level.isClientSide) {
            player.playSound(SoundEvents.WOODEN_BUTTON_CLICK_OFF, 1f, 1f);
        }
        player.getCooldowns().addCooldown(launcher, reloadTime);
    }

    @Override
    public Item getAmmunition() {
        return ItemInit.flakCannon.get().asItem();
    }
}
