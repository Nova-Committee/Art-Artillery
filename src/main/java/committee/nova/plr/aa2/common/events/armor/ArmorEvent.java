package committee.nova.plr.aa2.common.events.armor;

import committee.nova.plr.aa2.common.item.base.BombingArmorLeggingsItem;
import committee.nova.plr.aa2.common.item.init.ItemInit;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ArmorEvent {
    @SubscribeEvent
    public static void onEquipmentChanged(LivingEquipmentChangeEvent event) {
        final ItemStack leggings = event.getTo();
        if (!leggings.is(ItemInit.bombingArmorLeggings.get())) {
            return;
        }
        BombingArmorLeggingsItem.tryInitializeNbt(leggings);
    }
}
