package committee.nova.plr.aa2.client.render;

import committee.nova.plr.aa2.common.entity.init.EntityInit;
import committee.nova.plr.aa2.common.item.init.ItemInit;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RendererInit {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.shell.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityInit.flakCannon.get(), ThrownItemRenderer::new);
    }

    @SubscribeEvent
    public static void overrideRegistry(FMLClientSetupEvent event) {
        overrideFlakLauncher(event, ItemInit.portableFlakLauncher.get());
        overrideLaserTracker(event, ItemInit.laserTracker.get());
    }

    public static void overrideFlakLauncher(FMLClientSetupEvent event, Item launcher) {
        event.enqueueWork(() -> ItemProperties.register(launcher, new ResourceLocation("pull"), (stack, world, entity, i) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != stack ? 0.0F : Math.min(2F, (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 50.0F);
            }
        }));
    }

    public static void overrideLaserTracker(FMLClientSetupEvent event, Item laser) {
        event.enqueueWork(() -> ItemProperties.register(laser, new ResourceLocation("on"), (stack, world, entity, i) -> {
            if (entity == null) {
                return 0;
            } else if (stack != entity.getItemInHand(InteractionHand.MAIN_HAND)) {
                return 0;
            } else {
                return stack == entity.getUseItem() ? 1 : 0;
            }
        }));
    }
}