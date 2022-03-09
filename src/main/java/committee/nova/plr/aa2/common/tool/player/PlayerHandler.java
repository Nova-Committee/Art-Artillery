package committee.nova.plr.aa2.common.tool.player;

import committee.nova.plr.aa2.common.config.CommonConfig;
import committee.nova.plr.aa2.common.item.api.IReloadable;
import committee.nova.plr.aa2.common.item.enchantment.init.EnchantmentInit;
import net.minecraft.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Iterator;
import java.util.Random;

public class PlayerHandler {

    public static final String CURRENT = "c_magazine";
    public static final String MAX = "m_magazine";

    public static boolean loadShell(ItemStack stack, Player player) {
        if (stack.getItem() instanceof IReloadable launcher) {
            final NonNullList<ItemStack> itemList = player.getInventory().items;
            final int size = itemList.size();
            final Item ammunition = launcher.getAmmunition();
            for (final ItemStack stackToTest : itemList) {
                if (stackToTest.getItem() == ammunition) {
                    stackToTest.shrink(1);
                    if (stackToTest.isEmpty()) player.getInventory().removeItem(stackToTest);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean consumeMagazine(ItemStack stack, Player player) {
        final CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(CURRENT)) {
            return false;
        }
        if (!tag.contains(MAX)) {
            return false;
        }

        final int c = tag.getInt(CURRENT);
        if (c >= 1) {
            final double grandet = 2 - Math.pow(1.1, EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.grandet.get(), stack));
            final Random random = player.getRandom();
            tag.putInt(CURRENT, c - (random.nextDouble() > grandet ? 0 : 1));
            return true;
        }
        return false;
    }

    public static void notifyClientPlayer(Player player, Component text) {
        if (player.level.isClientSide) {
            player.sendMessage(text, Util.NIL_UUID);
        }
    }

    public static boolean canShootAccurately(Player player, ItemStack launcher) {
        if (!CommonConfig.INACCURACY_SYSTEM.get()) {
            return true;
        }
        if (EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.stabilizer.get(), launcher) >= 1) {
            return true;
        }
        if (!player.isOnGround() && !player.isPassenger()) {
            return false;
        }
        if (player.isSprinting() && !player.isCrouching()) {
            return false;
        }
        return !player.isSwimming();
    }

    public static void tryAwardAdvancement(ServerPlayer player, ResourceLocation advancementId) {
        if (player == null) {
            return;
        }
        final Advancement advancement = player.server.getAdvancements().getAdvancement(advancementId);
        if (advancement == null) {
            return;
        }
        final AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
        if (!progress.isDone()) completeTheAdvancement(player, advancement, progress);
    }

    private static void completeTheAdvancement(ServerPlayer player, Advancement advancement, AdvancementProgress progress) {
        final Iterator<String> iterator = progress.getRemainingCriteria().iterator();
        if (iterator.hasNext()) {
            player.getAdvancements().award(advancement, iterator.next());
            completeTheAdvancement(player, advancement, progress);
        }
    }
}
