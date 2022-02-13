package committee.nova.plr.aa2.client.render;

import committee.nova.plr.aa2.common.entity.init.EntityInit;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class RendererInit {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.shell.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityInit.flakCannon.get(), ThrownItemRenderer::new);
    }

}
