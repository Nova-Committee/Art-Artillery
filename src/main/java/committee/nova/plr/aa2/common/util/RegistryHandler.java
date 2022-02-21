package committee.nova.plr.aa2.common.util;

import committee.nova.plr.aa2.client.config.ClientConfig;
import committee.nova.plr.aa2.common.AA2;
import committee.nova.plr.aa2.common.config.CommonConfig;
import committee.nova.plr.aa2.common.entity.init.EntityInit;
import committee.nova.plr.aa2.common.item.init.ItemInit;
import committee.nova.plr.aa2.common.sound.init.SoundInit;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
    public static final DeferredRegister<SoundEvent> Sounds = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AA2.MOD_ID);
    public static final DeferredRegister<Item> Items = DeferredRegister.create(ForgeRegistries.ITEMS, AA2.MOD_ID);
    public static final DeferredRegister<EntityType<?>> Entities = DeferredRegister.create(ForgeRegistries.ENTITIES, AA2.MOD_ID);

    public static void register() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        SoundInit.register();
        ItemInit.register();
        EntityInit.register();
        Sounds.register(eventBus);
        Items.register(eventBus);
        Entities.register(eventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.COMMON_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_CONFIG);
    }
}
