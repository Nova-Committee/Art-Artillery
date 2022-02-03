package comittee.nova.plr.aa2.common.message.hotkey;

import comittee.nova.plr.aa2.common.AA2;
import comittee.nova.plr.aa2.common.item.base.PortableLauncherItem;
import comittee.nova.plr.aa2.common.message.init.MessageInit;
import comittee.nova.plr.aa2.common.tool.player.PlayerHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.text.MessageFormat;
import java.util.function.Supplier;

import static comittee.nova.plr.aa2.common.tool.player.PlayerHandler.notifyClientPlayer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ReloadKeyMessage {
    int type;

    public ReloadKeyMessage(int type) {
        this.type = type;
    }

    public ReloadKeyMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
    }

    public static void buffer(ReloadKeyMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
    }

    public static void handler(ReloadKeyMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        final ServerPlayer player = context.getSender();
        if (player == null) {
            return;
        }
        context.enqueueWork(() -> {
            pressAction(player);
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player) {
        final Level world = player.level;
        if (!world.hasChunkAt(player.blockPosition())) return;
        final ItemStack stack = player.getMainHandItem();
        if ((stack.getItem() instanceof PortableLauncherItem launcher)) {

            final int status = launcher.reloadable(stack, player);
            switch (status) {
                case 1 -> {
                    notifyClientPlayer(player, new TranslatableComponent("msg.aa2.full_mag"));
                }
                case 0 -> {
                    if (!PlayerHandler.loadShell(stack, player)) {
                        notifyClientPlayer(player, new TranslatableComponent("msg.aa2.insufficient_shell"));
                        return;
                    }
                    launcher.load(stack.getOrCreateTag(), player);
                }
                default -> {
                    AA2.LOGGER.error(MessageFormat.format("Unexpected status {0}, should be 0~1!", status));
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        MessageInit.addNetworkMessage(ReloadKeyMessage.class, ReloadKeyMessage::buffer, ReloadKeyMessage::new, ReloadKeyMessage::handler);
    }

}
