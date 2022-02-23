package committee.nova.plr.aa2.common.item.base;

import committee.nova.plr.aa2.client.creativeTab.TabInit;
import committee.nova.plr.aa2.common.item.api.IThirdPersonRenderable;
import committee.nova.plr.aa2.common.tool.maths.RayTraceUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public class LaserTrackerItem extends Item implements IThirdPersonRenderable {
    public LaserTrackerItem() {
        super(new Properties().tab(TabInit.AA2_TAB).stacksTo(1));
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level worldIn, @Nonnull Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        if (entityIn instanceof Player player) {
            final Vec3 trace = RayTraceUtils.getRaytracedBlock(entityIn);
            if (!worldIn.isClientSide) {
                if (isSelected && trace != null) {
                    player.displayClientMessage(new TranslatableComponent("msg.aa2.laser_tracker", RayTraceUtils.vecToIntString(trace)), true);
                }
            }
        }
    }

    @Override
    public HumanoidModel.ArmPose getSprintingArmPose() {
        return HumanoidModel.ArmPose.BOW_AND_ARROW;
    }

    @Override
    public HumanoidModel.ArmPose getIdleArmPose() {
        return HumanoidModel.ArmPose.BOW_AND_ARROW;
    }
}
