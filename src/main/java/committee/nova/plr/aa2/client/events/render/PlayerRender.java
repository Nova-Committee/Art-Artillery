package committee.nova.plr.aa2.client.events.render;

import committee.nova.plr.aa2.common.AA2;
import committee.nova.plr.aa2.common.item.api.IThirdPersonRenderable;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AA2.MOD_ID, value = Dist.CLIENT)
public class PlayerRender {
    @SubscribeEvent
    public static void onPlayerRender(final RenderPlayerEvent event) {
        final Player player = event.getPlayer();
        if (player.getMainHandItem().getItem() instanceof IThirdPersonRenderable renderable) {
            if (!event.isCancelable()) {
                return;
            }
            event.getRenderer().getModel().rightArmPose =
                    player.isUsingItem() ? renderable.getUsingArmPose() :
                            player.isSprinting() ? renderable.getSprintingArmPose() :
                                    renderable.getIdleArmPose();
        }
    }
}
