package committee.nova.plr.aa2.common.item.base;

import committee.nova.plr.aa2.common.entity.api.IBombardable;
import committee.nova.plr.aa2.common.item.init.ItemInit;
import committee.nova.plr.aa2.common.item.material.Materials;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class BombingArmorLeggingsItem extends ArmorItem {
    //public static final String[] BOMBS = new String[]{"missile", "bomb", "bouncing_bomb", "torpedo"};
    public static final String[] BOMBS = new String[]{"bomb"};
    public static final Map<String, RegistryObject<? extends IBombardable>> BOMB_LIST = new HashMap<>();

    public BombingArmorLeggingsItem() {
        super(Materials.BOMBING, EquipmentSlot.LEGS, ItemInit.DEFAULT);
    }

    public static void tryInitializeNbt(ItemStack stack) {
        if (stack.getItem() != ItemInit.bombingArmorLeggings.get()) {
            return;
        }
        final CompoundTag tag = stack.getOrCreateTag();
        for (final String bomb : BOMBS) {
            if (!tag.contains(bomb)) tag.putInt(bomb, 0);
        }
    }

    public static int checkAmmunition(ItemStack stack, String type) {
        final CompoundTag tag = stack.getTag();
        return (tag != null) ? tag.getInt(type) : 0;
    }

    public void launch(ItemStack stack, String type, LivingEntity entity) {
        final int amount = checkAmmunition(stack, type);
        if (amount >= 1) {
            final IBombardable bomb = BOMB_LIST.get(type).get();
            //todo:bomb
            bomb.launch(entity);
        } else {
            if (entity instanceof Player player) {
                player.displayClientMessage(new TranslatableComponent("msg.aa2.insufficient_ammunition"), true);
            }
        }
    }
}
