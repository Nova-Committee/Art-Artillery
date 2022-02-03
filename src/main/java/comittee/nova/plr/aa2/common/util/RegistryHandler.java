package comittee.nova.plr.aa2.common.util;

import comittee.nova.plr.aa2.common.AA2;
import comittee.nova.plr.aa2.common.entity.init.EntityInit;
import comittee.nova.plr.aa2.common.item.init.ItemInit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
    public static final DeferredRegister<Item> Items = DeferredRegister.create(ForgeRegistries.ITEMS, AA2.MOD_ID);
    public static final DeferredRegister<EntityType<?>> Entities = DeferredRegister.create(ForgeRegistries.ENTITIES, AA2.MOD_ID);

    public static void register() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemInit.register();
        EntityInit.register();
        Items.register(eventBus);
        Entities.register(eventBus);
    }
}
