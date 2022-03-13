package committee.nova.plr.aa2.common.item.base;

import committee.nova.plr.aa2.client.creativeTab.TabInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class BombardControllerItem extends Item {
    public BombardControllerItem() {
        super(new Properties().tab(TabInit.AA2_TAB).stacksTo(1));
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        final ItemStack itemstack = player.getItemInHand(hand);
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.pass(itemstack);
        }
        final CompoundTag tag = itemstack.getOrCreateTag();
        if (!tag.contains("type")) return InteractionResultHolder.pass(itemstack);
        final Iterable<ItemStack> i = player.getArmorSlots();
        for (final ItemStack s : i) {
            if (s.getItem() instanceof BombingArmorLeggingsItem leggings) {
                if (player.getCooldowns().isOnCooldown(leggings.asItem())) {
                    player.displayClientMessage(new TranslatableComponent("msg.aa2.bal_cd"), true);
                    return InteractionResultHolder.pass(itemstack);
                }
                leggings.launch(s, tag.getString("type"), player);
                //todo:c
                player.getCooldowns().addCooldown(s.getItem(), 100);
                return InteractionResultHolder.consume(itemstack);
            }
        }
        player.displayClientMessage(new TranslatableComponent("msg.aa2.need_bal"), true);
        return InteractionResultHolder.pass(itemstack);
    }

}
