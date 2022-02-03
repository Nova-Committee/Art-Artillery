package comittee.nova.plr.aa2.common;

import comittee.nova.plr.aa2.common.util.RegistryHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AA2.MOD_ID)
public class AA2 {
    public static final String MOD_ID = "aa2";
    public static final Logger LOGGER = LogManager.getLogger();

    public AA2() {
        RegistryHandler.register();
        MinecraftForge.EVENT_BUS.register(this);
    }

}
