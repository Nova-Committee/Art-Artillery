package committee.nova.plr.aa2.common.sound.init;

import committee.nova.plr.aa2.common.AA2;
import committee.nova.plr.aa2.common.util.RegistryHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class SoundInit {
    public static final String LAUNCH = "flak_launch";
    public static final String EXPLODE = "flak_explode";

    public static final SoundEvent FLAK_LAUNCH = new SoundEvent(new ResourceLocation(AA2.MOD_ID, LAUNCH));
    public static final SoundEvent FLAK_EXPLODE = new SoundEvent(new ResourceLocation(AA2.MOD_ID, EXPLODE));

    public static void register() {
        initSound(LAUNCH);
        initSound(EXPLODE);
    }

    public static void initSound(String soundId) {
        RegistryHandler.Sounds.register(soundId, () -> new SoundEvent(new ResourceLocation(AA2.MOD_ID, soundId)));
    }
}
