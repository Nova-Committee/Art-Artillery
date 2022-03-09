package committee.nova.plr.aa2.common.entity.init;

import committee.nova.plr.aa2.common.AA2;
import committee.nova.plr.aa2.common.entity.impl.FlakCannonProjectile;
import committee.nova.plr.aa2.common.entity.impl.ShellProjectile;
import committee.nova.plr.aa2.common.util.RegistryHandler;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;

import static net.minecraft.tags.EntityTypeTags.bind;

public class EntityInit {
    public static final Tag.Named<EntityType<?>> BOSSES = bind("bosses");

    public static final RegistryObject<EntityType<ShellProjectile>> shell = RegistryHandler.Entities.register("shell",
            () -> EntityType.Builder.of((EntityType<ShellProjectile> e, Level l) -> new ShellProjectile(e, l), MobCategory.MISC)
                    .sized(0.25f, 0.25f).fireImmune().build(AA2.MOD_ID + ".shell"));
    public static final RegistryObject<EntityType<FlakCannonProjectile>> flakCannon = RegistryHandler.Entities.register("flak",
            () -> EntityType.Builder.of((EntityType<FlakCannonProjectile> e, Level l) -> new FlakCannonProjectile(e, l), MobCategory.MISC)
                    .sized(0.25f, 0.25f).fireImmune().build(AA2.MOD_ID + ".flak"));

    public static void register() {

    }
}
