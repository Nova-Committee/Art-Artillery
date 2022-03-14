package committee.nova.plr.aa2.client.events.overlay;


import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import committee.nova.plr.aa2.client.config.ClientConfig;
import committee.nova.plr.aa2.common.item.init.ItemInit;
import committee.nova.plr.aa2.common.tool.maths.FormatUtils;
import committee.nova.plr.aa2.common.tool.player.PlayerHandler;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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

import static committee.nova.plr.aa2.common.item.base.HydroSearcherItem.ACTIVATED;
import static committee.nova.plr.aa2.common.item.base.HydroSearcherItem.TIME_LEFT;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class Overlay {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void magazineRender(RenderGameOverlayEvent.Pre event) {
        if (!ClientConfig.DISPLAY_SHELL_INDICATOR.get()) {
            return;
        }
        final Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        final ItemStack stackInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!stackInHand.is(ItemInit.PORTABLE_LAUNCHERS)) {
            return;
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            final int h = event.getWindow().getGuiScaledHeight();
            final int amount = stackInHand.getOrCreateTag().getInt("c_magazine");
            startRender();
            blitShellIndicator(event.getMatrixStack(), amount, h);
            endRender();
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void indicatorRender(RenderGameOverlayEvent.Pre event) {
        final Player player = checkFirstPersonAvailable();
        if (player == null) {
            return;
        }
        final ItemStack stackInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!stackInHand.is(ItemInit.PORTABLE_LAUNCHERS)) {
            return;
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            final int h = event.getWindow().getGuiScaledHeight() / 2;
            final int w = event.getWindow().getGuiScaledWidth() / 2;
            startRender();
            blitCrossHairAdditions(event.getMatrixStack(), player, stackInHand, h, w);
            endRender();
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void hsRender(RenderGameOverlayEvent.Pre event) {
        final Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        final ItemStack stackInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!stackInHand.is(ItemInit.hydroSearcherItem.get())) {
            return;
        }
        final CompoundTag tag = stackInHand.getOrCreateTag();
        if (!tag.contains(ACTIVATED)) return;
        if (!tag.contains(TIME_LEFT)) return;
        startRender();
        blitHydroSearchIndication(event.getMatrixStack(), player, stackInHand, tag, event.getWindow().getGuiScaledHeight());
        endRender();
    }

    private static void blitShellIndicator(PoseStack matrixStack, int amount, int height) {
        final ResourceLocation texture = new ResourceLocation(MessageFormat.format("aa2:textures/overlay/{0}shell.png", (amount == 0) ? "no_" : ""));
        RenderSystem.setShaderTexture(0, texture);
        GuiComponent.blit(matrixStack, 3, height - 18, 0, 0, 16, 16, 16, 16);
        Minecraft.getInstance().font.draw(matrixStack, new TextComponent((amount == 0) ? "" : String.valueOf(amount))
                , 15, height - 12, -1);
    }

    private static void blitCrossHairAdditions(PoseStack matrixStack, Player player, ItemStack stack, int h, int w) {
        if (stack.is(ItemInit.FLAK_LAUNCHERS)) {
            if (!ClientConfig.DISPLAY_FLAK_INDICATOR.get()) return;
            final ResourceLocation texture = new ResourceLocation("aa2:textures/overlay/r_indicator.png");
            RenderSystem.setShaderTexture(0, texture);
            final float raw = player.getUseItem() != stack ? 0.0F : Math.min(2F, (stack.getUseDuration() - player.getUseItemRemainingTicks()) / 50.0F);
            final String range = FormatUtils.formattedNumber(raw, 2);
            GuiComponent.blit(matrixStack, w + 6, h - 8, 0, 0, 16, 16, 16, 16);
            final Component textToDisplay = ClientConfig.USE_TEXT_IN_FLAK_OVERLAY.get() ? new TextComponent(new TranslatableComponent("overlay.aa2.launch_speed").getString() + range) : new TextComponent(range);
            Minecraft.getInstance().font.draw(matrixStack, textToDisplay, w + 15, h - 4, -1);
            return;
        }
        if (!ClientConfig.DISPLAY_ACCURACY_INDICATOR.get()) return;
        final boolean isAccurate = PlayerHandler.canShootAccurately(player, player.getItemInHand(InteractionHand.MAIN_HAND));
        final ResourceLocation tex = new ResourceLocation(MessageFormat.format("aa2:textures/overlay/accuracy_{0}.png", isAccurate ? "1" : "0"));
        RenderSystem.setShaderTexture(0, tex);
        GuiComponent.blit(matrixStack, w - 7 - h % 2, h - 8 + h % 2, 0, 0, 16, 16, 16, 16);
    }

    private static void blitHydroSearchIndication(PoseStack matrixStack, Player player, ItemStack stack, CompoundTag tag, int h) {
        final boolean activated = tag.getBoolean(ACTIVATED);
        final float raw = (activated ? tag.getInt(TIME_LEFT) : player.getCooldowns().getCooldownPercent(stack.getItem(), 0) * 1800) / 20F;
        final boolean ready = raw == 0 && !activated;
        final String toBlit = ready ? new TranslatableComponent("overlay.aa2.ready").getString() : FormatUtils.formattedNumber(raw, 1);
        RenderSystem.setShaderTexture(0, new ResourceLocation("aa2:textures/overlay/hs_act.png"));
        GuiComponent.blit(matrixStack, 25, h - 18, 0, 0, 16, 16, 16, 16);
        Minecraft.getInstance().font.draw(matrixStack, toBlit, 41, h - 12, (ready || activated) ? -16711936 : -3355648);
    }

    private static void startRender() {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    private static void endRender() {
        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    private static Player checkFirstPersonAvailable() {
        return Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON ? Minecraft.getInstance().player : null;
    }
}
