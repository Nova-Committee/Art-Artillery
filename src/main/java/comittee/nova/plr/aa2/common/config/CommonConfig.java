package comittee.nova.plr.aa2.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.BooleanValue INACCURACY_SYSTEM = BUILDER.comment(
                    "Configuration of Art Artillery 2",
                    "[Mechanisms]",
                    "Should we activate inaccuracy system?",
                    " If yes, player should stand still to keep the mod's ranged item accurate.")
            .define("inaccuracy", true);
    public static final ForgeConfigSpec.DoubleValue INACCURACY_AMPLIFIER = BUILDER.comment(
                    "What should the inaccuracy multiplier be?",
                    "The larger the multiplier, the higher the inaccuracy.",
                    "Default is 1.0."
            )
            .defineInRange("inaccuracy_amplifier", 1D, 0D, 50D);
    public static final ForgeConfigSpec.IntValue FIRE_CD = BUILDER.comment(
                    "[Cool Down Times]",
                    "How long should be the cool down time after using the launcher?",
                    "Default is 20 ticks = 1 sec.")
            .defineInRange("fire_cd", 20, 0, 100);
    public static final ForgeConfigSpec.IntValue RELOAD_CD = BUILDER.comment(
                    "How long should be the cool down time after reloading the launcher?",
                    "Default is 60 ticks = 3 sec.")
            .defineInRange("reload_cd", 60, 0, 300);
    public static final ForgeConfigSpec.DoubleValue SHELL_SPEED = BUILDER.comment(
                    "[Shell Projectile Properties]",
                    "How quick should the shell be launched?",
                    "Default is 1.0, like the speed of vanilla arrows.")
            .defineInRange("shell_power", 1D, 0.2D, 8D);
    public static final ForgeConfigSpec.DoubleValue SHELL_DIRECT_DAMAGE = BUILDER.comment(
                    "How big should be the damage of directly hit by a shell?",
                    "Default is 10.0 (5 hearts).")
            .defineInRange("shell_direct_damage", 10D, 0D, 200D);
    public static final ForgeConfigSpec.DoubleValue SHELL_HIT_ENTITY_EXPLOSION_POWER = BUILDER.comment(
                    "How big should be the explosion power of a shell directly hit into an entity?",
                    "Default is 2.0.")
            .defineInRange("exp_power_ent", 2D, 0D, 200D);
    public static final ForgeConfigSpec.DoubleValue SHELL_HIT_BLOCK_EXPLOSION_POWER = BUILDER.comment(
                    "How big should be the explosion power of a shell directly hit into a block?",
                    "Default is 2.0.")
            .defineInRange("exp_power_block", 2D, 0D, 200D);
    public static final ForgeConfigSpec.IntValue SHELL_HIT_ENTITY_EXPLOSION_TYPE = BUILDER.comment(
                    "Which type of explosion should occur when a shell hit into an entity?",
                    "Explosion Types:",
                    "0 -> NONE(No terrain damage);",
                    "1 -> BREAK(Break the blocks and loot);",
                    "2 -> DESTROY(Destroy the blocks, nothing left).",
                    "Default is 0.")
            .defineInRange("exp_type_ent", 0, 0, 2);
    public static final ForgeConfigSpec.IntValue SHELL_HIT_BLOCK_EXPLOSION_TYPE = BUILDER.comment(
                    "Which type of explosion should occur when a shell hit into a block?",
                    "Explosion Types:",
                    "0 -> NONE(No terrain damage);",
                    "1 -> BREAK(Break the blocks and loot);",
                    "2 -> DESTROY(Destroy the blocks, nothing left).",
                    "Default is 0.")
            .defineInRange("exp_type_block", 0, 0, 2);
    public static final ForgeConfigSpec COMMON_CONFIG = BUILDER.build();
}
