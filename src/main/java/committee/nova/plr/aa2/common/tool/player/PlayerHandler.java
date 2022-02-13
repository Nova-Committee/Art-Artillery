package committee.nova.plr.aa2.common.tool.player;

import committee.nova.plr.aa2.common.item.api.IReloadable;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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
            tag.putInt(CURRENT, c - 1);
            return true;
        }
        return false;
    }

    public static void notifyClientPlayer(Player player, Component text) {
        if (player.level.isClientSide) {
            player.sendMessage(text, Util.NIL_UUID);
        }
    }

    public static boolean canShootAccurately(Player player) {
        if (!player.isOnGround() && !player.isPassenger()) {
            return false;
        }
        if (player.isSprinting()) {
            return false;
        }
        return !player.isSwimming();
    }
}
