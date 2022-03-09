package committee.nova.plr.aa2.common.message.hotkey;

import committee.nova.plr.aa2.common.AA2;
import committee.nova.plr.aa2.common.item.api.IReloadable;
import committee.nova.plr.aa2.common.message.init.MessageInit;
import committee.nova.plr.aa2.common.tool.player.PlayerHandler;
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


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ReloadKeyMessage {
    final int type;

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
        context.enqueueWork(() -> pressAction(player));
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player) {
        final Level world = player.level;
        if (!world.hasChunkAt(player.blockPosition())) return;
        final ItemStack stack = player.getMainHandItem();
        if ((stack.getItem() instanceof IReloadable launcher)) {

            final int status = launcher.reloadable(stack, player);
            switch (status) {
                case 2 -> player.displayClientMessage(new TranslatableComponent("msg.aa2.cooling"), true);
                case 1 -> player.displayClientMessage(new TranslatableComponent("msg.aa2.full_mag"), true);
                case 0 -> {
                    if (!PlayerHandler.loadShell(stack, player)) {
                        player.displayClientMessage(new TranslatableComponent("msg.aa2.insufficient_ammunition"), true);
                        return;
                    }
                    launcher.load(stack.getOrCreateTag(), player, stack);
                }
                default -> AA2.LOGGER.error(MessageFormat.format("Unexpected status {0}, should be 0~2!", status));
            }
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        MessageInit.addNetworkMessage(ReloadKeyMessage.class, ReloadKeyMessage::buffer, ReloadKeyMessage::new, ReloadKeyMessage::handler);
    }

}
