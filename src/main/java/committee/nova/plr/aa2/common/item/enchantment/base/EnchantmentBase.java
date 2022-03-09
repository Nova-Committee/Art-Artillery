package committee.nova.plr.aa2.common.item.enchantment.base;

import committee.nova.plr.aa2.common.item.init.ItemInit;
import committee.nova.plr.aa2.common.tool.misc.EnchantmentTool;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import javax.annotation.Nonnull;

public class EnchantmentBase extends Enchantment {
    private final int type;
    private final int minLevel;
    private final int maxLevel;
    private final boolean isTreasureOnly;

    public EnchantmentBase(int type, int rarity, int minLevel, int maxLevel, boolean isTreasure) {
        super(EnchantmentTool.getRarityFromInt(rarity), EnchantmentTool.PORTABLE_LAUNCHER, EquipmentSlot.values());
        this.type = type;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.isTreasureOnly = isTreasure;
    }

    @Override
    public int getMinLevel() {
        return minLevel;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack) {
        final Item toEnchant = stack.getItem();
        return switch (type) {
            case 0 -> ItemInit.PORTABLE_LAUNCHERS.contains(toEnchant) && !ItemInit.FLAK_LAUNCHERS.contains(toEnchant);
            case 1 -> ItemInit.FLAK_LAUNCHERS.contains(toEnchant);
            default -> ItemInit.PORTABLE_LAUNCHERS.contains(toEnchant);
        };
    }

    @Override
    public boolean isTreasureOnly() {
        return isTreasureOnly;
    }
}
