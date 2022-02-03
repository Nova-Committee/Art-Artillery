package comittee.nova.plr.aa2.common.hotkey.init;

import comittee.nova.plr.aa2.common.message.hotkey.ReloadKeyMessage;
import comittee.nova.plr.aa2.common.message.init.MessageInit;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class KeyInit {
    public static final KeyMapping RELOAD = new KeyMapping("key.aa2.reload", GLFW.GLFW_KEY_R, "key.categories.aa2");

    @SubscribeEvent
    public static void registerKeyBindings(FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(RELOAD);
    }

    @Mod.EventBusSubscriber({Dist.CLIENT})
    public static class KeyEventListener {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.KeyInputEvent event) {
            if (Minecraft.getInstance().screen == null) {
                if (event.getKey() == RELOAD.getKey().getValue()) {
                    if (event.getAction() == GLFW.GLFW_PRESS) {
                        final LocalPlayer player = Minecraft.getInstance().player;
                        if (player == null) return;
                        MessageInit.PACKET_HANDLER.sendToServer(new ReloadKeyMessage(0));
                        ReloadKeyMessage.pressAction(player);
                    }
                }
            }
        }
    }

}
