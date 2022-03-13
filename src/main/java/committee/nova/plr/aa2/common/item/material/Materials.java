package committee.nova.plr.aa2.common.item.material;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public enum Materials implements ArmorMaterial {
    BOMBING("bombing_armor", 24, new int[]{0, 7, 0, 0}, 9, SoundEvents.ARMOR_EQUIP_IRON, 1f, 1f,
            () -> Ingredient.of(Items.NETHERITE_INGOT)
    );
    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    Materials(String name, int multiplier, int[] protections, int enchantability, SoundEvent sound
            , float tough, float knockback, Supplier<Ingredient> item) {
        this.name = name;
        this.durabilityMultiplier = multiplier;
        this.slotProtections = protections;
        this.enchantmentValue = enchantability;
        this.sound = sound;
        this.toughness = tough;
        this.knockbackResistance = knockback;
        this.repairIngredient = new LazyLoadedValue<>(item);
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlot slot) {
        return HEALTH_PER_SLOT[slot.getIndex()] * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlot slot) {
        return this.slotProtections[slot.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Nonnull
    @Override
    public SoundEvent getEquipSound() {
        return this.sound;
    }

    @Nonnull
    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Nonnull
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
