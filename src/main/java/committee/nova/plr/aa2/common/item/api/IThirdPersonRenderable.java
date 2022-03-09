package committee.nova.plr.aa2.common.item.api;

import net.minecraft.client.model.HumanoidModel;

public interface IThirdPersonRenderable {
    default HumanoidModel.ArmPose getIdleArmPose() {
        return HumanoidModel.ArmPose.SPYGLASS;
    }

    default HumanoidModel.ArmPose getSprintingArmPose() {
        return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
    }

    default HumanoidModel.ArmPose getUsingArmPose() {
        return HumanoidModel.ArmPose.BOW_AND_ARROW;
    }
}

