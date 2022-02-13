package committee.nova.plr.aa2.common.item.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IReloadable {
    void load(CompoundTag tag, Player player, Item launcher);

    int reloadable(ItemStack stack, Player player);

    Item getAmmunition();
}
