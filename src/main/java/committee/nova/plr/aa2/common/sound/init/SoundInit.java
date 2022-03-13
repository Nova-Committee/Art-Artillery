package committee.nova.plr.aa2.common.sound.init;

import committee.nova.plr.aa2.common.AA2;
import committee.nova.plr.aa2.common.util.RegistryHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.HashMap;
import java.util.Map;

public class SoundInit {
    public static final Map<String, SoundEvent> SOUNDS = new HashMap<>();

    public static final String LAUNCH = "flak_launch";
    public static final String EXPLODE = "flak_explode";
    public static final String HS_A = "hydro_search_act";
    public static final String HS_L = "hydro_search_loop";

    public static final String[] SOUND_NAMES = new String[]{LAUNCH, EXPLODE, HS_A, HS_L};

    public static void register() {
        for (final String soundName : SOUND_NAMES) {
            initSound(soundName);
            SOUNDS.put(soundName, getSound(soundName));
        }
    }

    public static void initSound(String soundId) {
        RegistryHandler.Sounds.register(soundId, () -> new SoundEvent(new ResourceLocation(AA2.MOD_ID, soundId)));
    }

    public static SoundEvent getSound(String soundId) {
        return new SoundEvent(new ResourceLocation(AA2.MOD_ID, soundId));
    }
}
