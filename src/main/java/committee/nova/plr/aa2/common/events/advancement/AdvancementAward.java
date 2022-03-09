package committee.nova.plr.aa2.common.events.advancement;

import committee.nova.plr.aa2.common.entity.impl.ShellProjectile;
import committee.nova.plr.aa2.common.entity.init.EntityInit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static committee.nova.plr.aa2.common.tool.misc.AdvancementReference.*;
import static committee.nova.plr.aa2.common.tool.player.PlayerHandler.tryAwardAdvancement;

@Mod.EventBusSubscriber
public class AdvancementAward {
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event.isCanceled()) return;
        final LivingEntity victim = event.getEntityLiving();
        final DamageSource source = event.getSource();
        if (source == ShellProjectile.SHOT_BY_SHELL) listenEntityBombarded(victim);
    }

    private static void listenEntityBombarded(LivingEntity victim) {
        if (victim instanceof ServerPlayer player) tryAwardAdvancement(player, getAdvId(aShellFromTheBlue));
        final CombatTracker tracker = victim.getCombatTracker();
        final Entity killer = tracker.getKiller();
        if (killer instanceof ServerPlayer player) {
            if (EntityInit.BOSSES.contains(victim.getType())) tryAwardAdvancement(player, getAdvId(timesHaveChanged));
        }
        final boolean farEnough = killer != null && victim.distanceTo(killer) >= 30F;
        if (!farEnough) return;
        if (killer instanceof ServerPlayer player) tryAwardAdvancement(player, getAdvId(gunner));
    }
}
