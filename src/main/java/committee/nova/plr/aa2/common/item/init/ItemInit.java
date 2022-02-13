package committee.nova.plr.aa2.common.item.init;

import committee.nova.plr.aa2.client.creativeTab.TabInit;
import committee.nova.plr.aa2.common.item.base.PortableFlakLauncherItem;
import committee.nova.plr.aa2.common.item.base.PortableLauncherItem;
import committee.nova.plr.aa2.common.util.RegistryHandler;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraft.tags.ItemTags.bind;

public class ItemInit {
    public static final Tag.Named<Item> PORTABLE_LAUNCHERS = bind("portable_launcher");

    public static final String[] list = new String[]{"barrel", "machine", "handle", "butt", "mag_3", "mag_6", "mag_u"};

    public static final RegistryObject<Item> portableLauncher = RegistryHandler.Items.register("pl_0", () -> new PortableLauncherItem(1));
    public static final RegistryObject<Item> portableLauncherIII = RegistryHandler.Items.register("pl_1", () -> new PortableLauncherItem(3));
    public static final RegistryObject<Item> portableLauncherVI = RegistryHandler.Items.register("pl_2", () -> new PortableLauncherItem(6));
    public static final RegistryObject<Item> portableFlakCannon = RegistryHandler.Items.register("pfc", () -> new PortableFlakLauncherItem(1));

    public static final RegistryObject<Item> genericShell = RegistryHandler.Items.register("generic_shell", () -> new Item(new Item.Properties().tab(TabInit.AA2_TAB)));
    public static final RegistryObject<Item> flakCannon = RegistryHandler.Items.register("flak_cannon", () -> new Item(new Item.Properties().tab(TabInit.AA2_TAB)));

    public static void register() {
        for (String id : list) {
            lazyRegister(id);
        }
    }

    public static void lazyRegister(String id) {
        RegistryHandler.Items.register(id, () -> new Item(new Item.Properties().tab(TabInit.AA2_TAB)));
    }

}
