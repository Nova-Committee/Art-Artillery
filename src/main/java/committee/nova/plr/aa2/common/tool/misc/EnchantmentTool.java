package committee.nova.plr.aa2.common.tool.misc;

import committee.nova.plr.aa2.common.item.init.ItemInit;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class EnchantmentTool {
    public static final EnchantmentCategory PORTABLE_LAUNCHER = EnchantmentCategory.create("pl", ItemInit.PORTABLE_LAUNCHERS::contains);

    public static Enchantment.Rarity getRarityFromInt(int i) {
        return switch (i) {
            case 1 -> Enchantment.Rarity.UNCOMMON;
            case 2 -> Enchantment.Rarity.RARE;
            case 3 -> Enchantment.Rarity.VERY_RARE;
            default -> Enchantment.Rarity.COMMON;
        };
    }

    public static int getTimeToConsume(int origin, int level) {
        return Math.max(2, (int) (Math.pow(0.8, level) * origin));
    }

}
