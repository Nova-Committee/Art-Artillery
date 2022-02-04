package comittee.nova.plr.aa2.common.tool.player;

import comittee.nova.plr.aa2.common.item.init.ItemInit;
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
        final NonNullList<ItemStack> itemList = player.getInventory().items;
        final int size = itemList.size();
        final Item shell = ItemInit.genericShell.get();
        for (final ItemStack stackToTest : itemList) {
            if (stackToTest.getItem() == shell) {
                stackToTest.shrink(1);
                if (stackToTest.isEmpty()) player.getInventory().removeItem(stackToTest);
                return true;
            }
            ;
        }
        return false;
    }

    public static boolean consumeShell(ItemStack stack, Player player) {
        final CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("c_magazine")) {
            return false;
        }
        if (!tag.contains("m_magazine")) {
            return false;
        }
        final int c = tag.getInt("c_magazine");
        if (c >= 1) {
            tag.putInt("c_magazine", c - 1);
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
        final double y = player.getDeltaMovement().y;
        if (y > 0.2D) {
            return false;
        }
        final double x = player.getDeltaMovement().x;
        final double z = player.getDeltaMovement().z;
        final double h = Math.sqrt(x * x + z * z);
        return !(h > 1.4D);
    }
}
