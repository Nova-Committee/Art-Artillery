package committee.nova.plr.aa2.client.overlay;


import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import committee.nova.plr.aa2.common.item.init.ItemInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.text.MessageFormat;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class MagazineOverlay {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGameOverlayEvent.Pre event) {
        final Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        final ItemStack stackInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!ItemInit.PORTABLE_LAUNCHERS.contains(stackInHand.getItem())) {
            return;
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            final int h = event.getWindow().getGuiScaledHeight();
            final int amount = stackInHand.getOrCreateTag().getInt("c_magazine");
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            blitShellIndicator(event.getMatrixStack(), amount, h);
            RenderSystem.depthMask(true);
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }
    }

    public static void blitShellIndicator(PoseStack matrixStack, int amount, int height) {
        final ResourceLocation texture = new ResourceLocation(MessageFormat.format("aa2:textures/overlay/{0}shell.png", (amount == 0) ? "no_" : ""));
        RenderSystem.setShaderTexture(0, texture);
        GuiComponent.blit(matrixStack, 3, height - 18, 0, 0, 16, 16, 16, 16);
        Minecraft.getInstance().font.draw(matrixStack, new TextComponent((amount == 0) ? "" : String.valueOf(amount))
                , 15, height - 12, -1);
    }

}
