package committee.nova.plr.aa2.client.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.BooleanValue DISPLAY_SHELL_INDICATOR = BUILDER.comment(
                    "Common Configuration of Art Artillery 2",
                    "[Overlay Settings]",
                    "Should we display an indicator for the ammunition status?",
                    "Default is true.")
            .define("shell_indicator", true);
    public static final ForgeConfigSpec.BooleanValue DISPLAY_ACCURACY_INDICATOR = BUILDER.comment(
                    "Should we display an indicator for the portable launcher's accuracy?",
                    "Default is true.")
            .define("accuracy_indicator", true);
    public static final ForgeConfigSpec.BooleanValue DISPLAY_FLAK_INDICATOR = BUILDER.comment(
                    "Should we display an indicator to let the player know the flak launcher's launching speed?",
                    "Default is true.")
            .define("flak_indicator", true);
    public static final ForgeConfigSpec.BooleanValue USE_TEXT_IN_FLAK_OVERLAY = BUILDER.comment(
                    "Should we display a text for flak launcher's launching speed indicator, or an icon?",
                    "true -> text, false -> icon")
            .define("flak_indicator_text", true);
    public static final ForgeConfigSpec CLIENT_CONFIG = BUILDER.build();
}
