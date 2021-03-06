package committee.nova.plr.aa2.common.item.base;

import committee.nova.plr.aa2.client.creativeTab.TabInit;
import committee.nova.plr.aa2.common.config.CommonConfig;
import committee.nova.plr.aa2.common.entity.impl.ShellProjectile;
import committee.nova.plr.aa2.common.item.api.IReloadable;
import committee.nova.plr.aa2.common.item.api.IThirdPersonRenderable;
import committee.nova.plr.aa2.common.item.enchantment.init.EnchantmentInit;
import committee.nova.plr.aa2.common.item.init.ItemInit;
import committee.nova.plr.aa2.common.tool.misc.EnchantmentTool;
import committee.nova.plr.aa2.common.tool.player.PlayerHandler;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

import static committee.nova.plr.aa2.common.tool.player.PlayerHandler.*;
import static net.minecraft.world.item.enchantment.EnchantmentHelper.getItemEnchantmentLevel;

public class PortableLauncherItem extends Item implements IReloadable, IThirdPersonRenderable {
    private final int reloadTime = CommonConfig.RELOAD_CD.get();
    private final int magazine;

    public PortableLauncherItem(int magazine) {
        super(new Properties().durability(300 * magazine).fireResistant().tab(TabInit.AA2_TAB));
        this.magazine = magazine;
    }

    public static void launchShell(ItemStack stack, Player player) {
        final Level level = player.level;
        if (!level.isClientSide) {
            final boolean isHeavy = getItemEnchantmentLevel(EnchantmentInit.superHeavyShell.get(), stack) >= 1;
            final boolean caseEnhanced = getItemEnchantmentLevel(EnchantmentInit.enhancedCase.get(), stack) >= 1;
            final ShellProjectile shell = ShellProjectile.shoot(level, player, PlayerHandler.canShootAccurately(player, stack), isHeavy, caseEnhanced);
            stack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(player.getUsedItemHand()));
            shell.pickup = AbstractArrow.Pickup.DISALLOWED;
            player.getCooldowns().addCooldown(stack.getItem(),
                    EnchantmentTool.getTimeToConsume(CommonConfig.FIRE_CD.get(), getItemEnchantmentLevel(EnchantmentInit.quickCooling.get(), stack)));
        }
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return 30;
    }

    @Override
    public boolean isFoil(@Nonnull ItemStack stack) {
        return false;
    }

    public static void initializeNbt(CompoundTag tag, int magazine) {
        if (!tag.contains(CURRENT)) tag.putInt(CURRENT, 0);
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
        if (entity.getItemInHand(InteractionHand.MAIN_HAND) != stack) {
            return true;
        }
        if (entity instanceof Player player) {
            if (player.getCooldowns().isOnCooldown(stack.getItem())) {
                return true;
            }
            if (consumeMagazine(stack, player)) launchShell(stack, player);
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
        if (player.getCooldowns().isOnCooldown(stack.getItem())) {
            return 2;
        }
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

    public void load(CompoundTag tag, Player player, ItemStack launcher) {
        tag.putInt(CURRENT, tag.getInt(CURRENT) + 1);
        if (player.level.isClientSide) {
            player.playSound(SoundEvents.WOODEN_BUTTON_CLICK_OFF, 1f, 1f);
        }
        player.getCooldowns().addCooldown(launcher.getItem(),
                EnchantmentTool.getTimeToConsume(reloadTime, getItemEnchantmentLevel(EnchantmentInit.quickReload.get(), launcher)));
    }

    @Override
    public Item getAmmunition() {
        return ItemInit.genericShell.get().asItem();
    }

    @Override
    public HumanoidModel.ArmPose getIdleArmPose() {
        return HumanoidModel.ArmPose.BOW_AND_ARROW;
    }

}
