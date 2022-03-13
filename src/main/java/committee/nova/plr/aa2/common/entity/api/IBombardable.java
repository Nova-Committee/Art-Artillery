package committee.nova.plr.aa2.common.entity.api;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;

public interface IBombardable extends ItemSupplier {
    EntityType<?> getEntity();

    void launch(LivingEntity entity);
}
