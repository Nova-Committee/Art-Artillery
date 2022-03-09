package committee.nova.plr.aa2.common.tool.misc;

import committee.nova.plr.aa2.common.AA2;
import net.minecraft.resources.ResourceLocation;

public class AdvancementReference {
    public static final String gunner = "gunner";
    public static final String aShellFromTheBlue = "a_shell_from_the_blue";
    public static final String timesHaveChanged = "times_have_changed";

    public static ResourceLocation getAdvId(String id) {
        return new ResourceLocation(AA2.MOD_ID, id);
    }
}
