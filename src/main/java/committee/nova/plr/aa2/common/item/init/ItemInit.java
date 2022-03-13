package committee.nova.plr.aa2.common.item.init;

import committee.nova.plr.aa2.client.creativeTab.TabInit;
import committee.nova.plr.aa2.common.item.base.*;
import committee.nova.plr.aa2.common.util.RegistryHandler;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.tags.ItemTags.bind;

public class ItemInit {
    public static final Tag.Named<Item> PORTABLE_LAUNCHERS = bind("portable_launcher");
    public static final Tag.Named<Item> FLAK_LAUNCHERS = bind("flak_launcher");

    public static final Item.Properties DEFAULT = new Item.Properties().tab(TabInit.AA2_TAB);

    public static final Map<String, RegistryObject<Item>> BOMB_ITEMS = new HashMap<>();

    public static final String[] list = new String[]{"barrel", "machine", "handle", "butt", "mag_3", "mag_6", "mag_u"};

    public static final RegistryObject<Item> portableLauncher = RegistryHandler.Items.register("pl_0", () -> new PortableLauncherItem(1));
    public static final RegistryObject<Item> portableLauncherIII = RegistryHandler.Items.register("pl_1", () -> new PortableLauncherItem(3));
    public static final RegistryObject<Item> portableLauncherVI = RegistryHandler.Items.register("pl_2", () -> new PortableLauncherItem(6));
    public static final RegistryObject<Item> portableFlakLauncher = RegistryHandler.Items.register("pfl", () -> new PortableFlakLauncherItem(10));
    public static final RegistryObject<Item> laserTracker = RegistryHandler.Items.register("ltr", LaserTrackerItem::new);

    public static final RegistryObject<Item> genericShell = RegistryHandler.Items.register("generic_shell", () -> new Item(DEFAULT));
    public static final RegistryObject<Item> flakCannon = RegistryHandler.Items.register("flak_cannon", () -> new Item(DEFAULT));

    public static final RegistryObject<Item> bombingArmorLeggings = RegistryHandler.Items.register("bombing_armor_leggings",
            BombingArmorLeggingsItem::new);
    //todo:bombard controller
    public static final RegistryObject<Item> hydroSearcherItem = RegistryHandler.Items.register("hydro_searcher", HydroSearcherItem::new);

    public static void register() {
        for (final String id : list) {
            lazyRegister(id);
        }
        for (final String type : BombingArmorLeggingsItem.BOMBS) {
            BOMB_ITEMS.put(type, lazyRegisterBomb(type));
        }
    }

    public static void lazyRegister(String id) {
        RegistryHandler.Items.register(id, () -> new Item(new Item.Properties().tab(TabInit.AA2_TAB)));
    }

    public static RegistryObject<Item> lazyRegisterBomb(String type) {
        return RegistryHandler.Items.register(type, () -> new Item(DEFAULT));
    }

}
