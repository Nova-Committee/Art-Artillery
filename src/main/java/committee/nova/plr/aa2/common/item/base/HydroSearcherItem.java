package committee.nova.plr.aa2.common.item.base;

import committee.nova.plr.aa2.client.creativeTab.TabInit;
import committee.nova.plr.aa2.common.sound.init.SoundInit;
import committee.nova.plr.aa2.common.tool.player.PlayerHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.List;

import static committee.nova.plr.aa2.common.sound.init.SoundInit.HS_A;
import static committee.nova.plr.aa2.common.sound.init.SoundInit.HS_L;

public class HydroSearcherItem extends Item {
    public static final MobEffectInstance SEARCHED = new MobEffectInstance(MobEffects.GLOWING, 120, 1);
    public static final String T_START_TIME = "st";
    public static final String TIME_LEFT = "tl";
    public static final String ACTIVATED = "at";

    public HydroSearcherItem() {
        super(new Properties().tab(TabInit.AA2_TAB).durability(60));
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int i, boolean bool) {
        if (entity instanceof Player player) {
            initializeNbt(stack);
            final CompoundTag tag = stack.getOrCreateTag();
            if (!tag.getBoolean(ACTIVATED)) {
                return;
            }
            final int t = tag.getInt(TIME_LEFT);
            if (t >= 1) {
                final long loop = level.getDayTime() - tag.getLong(T_START_TIME);
                if (loop != 0 && loop % 120 == 0) search(player, stack, false);
                tag.putInt(TIME_LEFT, t - 1);
            } else {
                tag.putLong(T_START_TIME, 0);
                tag.putBoolean(ACTIVATED, false);
                coolDown(player, stack);
            }
        }
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        final ItemStack itemstack = player.getItemInHand(hand);
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.pass(itemstack);
        }
        final CompoundTag tag = itemstack.getOrCreateTag();
        final boolean act = tag.getBoolean(ACTIVATED);
        if (!act) {
            open(itemstack, tag, player);
        } else {
            close(tag);
        }
        coolDown(player, itemstack);
        return InteractionResultHolder.success(itemstack);
    }

    private void initializeNbt(ItemStack stack) {
        final CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(T_START_TIME)) tag.putLong(T_START_TIME, 0);
        if (!tag.contains(TIME_LEFT)) tag.putInt(TIME_LEFT, 0);
        if (!tag.contains(ACTIVATED)) tag.putBoolean(ACTIVATED, false);
    }

    private void search(Player player, ItemStack stack, boolean init) {
        if (init) PlayerHandler.notifyTeamPlayer(player, new TranslatableComponent("msg.aa2.team.hydro_searching"));
        player.playSound(SoundInit.getSound(init ? HS_A : HS_L), 1F, 1F);
        if (!player.isInWater()) return;
        final Vec3 pos = player.getEyePosition();
        final List<LivingEntity> entityInRange = player.level.getEntitiesOfClass(LivingEntity.class, new AABB(pos, pos).inflate(64));
        for (final LivingEntity entityIn : entityInRange) {
            detect(player, entityIn);
        }
        stack.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(player.getUsedItemHand()));
    }

    private void detect(Player player, LivingEntity entityIn) {
        if (entityIn == player) return;
        if (!entityIn.isInWater()) return;
        if (entityIn.isAlliedTo(player)) return;
        entityIn.forceAddEffect(new MobEffectInstance(MobEffects.GLOWING, 140, 1), player);
    }

    private void close(CompoundTag tag) {
        tag.putBoolean(ACTIVATED, false);
        tag.putInt(TIME_LEFT, 0);
        tag.putLong(T_START_TIME, 0);
    }

    private void open(ItemStack stack, CompoundTag tag, Player player) {
        tag.putBoolean(ACTIVATED, true);
        tag.putLong(T_START_TIME, player.level.getDayTime());
        tag.putInt(TIME_LEFT, 1800);
        stack.setTag(tag);
        search(player, stack, true);
    }

    private void coolDown(Player player, ItemStack itemStack) {
        player.getCooldowns().addCooldown(itemStack.getItem(), 1800);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
}
