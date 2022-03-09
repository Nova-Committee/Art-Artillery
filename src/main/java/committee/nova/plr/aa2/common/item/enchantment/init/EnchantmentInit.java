package committee.nova.plr.aa2.common.item.enchantment.init;

import committee.nova.plr.aa2.common.item.enchantment.base.EnchantmentBase;
import committee.nova.plr.aa2.common.util.RegistryHandler;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentInit {
    public static final RegistryObject<Enchantment> quickReload = RegistryHandler.Enchantments.register("quick_reload",
            () -> new EnchantmentBase(2, 0, 1, 3, false));
    public static final RegistryObject<Enchantment> quickCooling = RegistryHandler.Enchantments.register("quick_cooling",
            () -> new EnchantmentBase(2, 0, 1, 3, false));
    public static final RegistryObject<Enchantment> grandet = RegistryHandler.Enchantments.register("grandet",
            () -> new EnchantmentBase(2, 3, 1, 4, false));
    public static final RegistryObject<Enchantment> superHeavyShell = RegistryHandler.Enchantments.register("shs",
            () -> new EnchantmentBase(0, 2, 1, 1, false));
    public static final RegistryObject<Enchantment> enhancedCase = RegistryHandler.Enchantments.register("enhanced_case",
            () -> new EnchantmentBase(0, 1, 1, 1, false));
    public static final RegistryObject<Enchantment> stabilizer = RegistryHandler.Enchantments.register("stabilizer",
            () -> new EnchantmentBase(0, 3, 1, 1, true));

    public static void register() {
    }
}
