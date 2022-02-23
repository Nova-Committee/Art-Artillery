package committee.nova.plr.aa2.common.tool.maths;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

//By aurilisdev in Electrodynamics
public class RayTraceUtils {
    public static Vec3 getRaytracedBlock(Entity entity) {
        return getRaytracedBlock(entity, 100);
    }

    public static Vec3 getRaytracedBlock(Entity entity, double rayLength) {
        return getRaytracedBlock(entity.level, entity.getLookAngle(), entity.getEyePosition(0), rayLength);
    }

    public static Vec3 getRaytracedBlock(Level world, Vec3 direction, Vec3 from, double rayLength) {
        final Vec3 rayPath = direction.normalize().scale(rayLength);
        final Vec3 to = from.add(rayPath);
        final ClipContext rayContext = new ClipContext(from, to, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, null);
        final BlockHitResult rayHit = world.clip(rayContext);
        return rayHit.getType() != HitResult.Type.BLOCK ? null : getVecByPos(rayHit.getBlockPos());
    }

    public static Vec3 getVecByPos(BlockPos pos) {
        return new Vec3(pos.getX(), pos.getY(), pos.getZ());
    }

    public static String vecToIntString(Vec3 v) {
        return "[" + (int) v.x + ", " + (int) v.y + ", " + (int) v.z + "]";
    }
}
