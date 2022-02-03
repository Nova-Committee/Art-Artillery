package comittee.nova.plr.aa2.client.creativeTab;

import comittee.nova.plr.aa2.common.item.init.ItemInit;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class TabInit {
    public static final CreativeModeTab AA2_TAB = new CreativeModeTab("aa2") {
        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return ItemInit.portableLauncherVI.get().getDefaultInstance();
        }
    };
}
