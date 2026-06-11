package net.matteo.playerbounty.events;

import static net.matteo.playerbounty.utils.mods.PlayerBountyUtils.*;
import static net.matteo.playerbounty.configs.Config.*;
import net.matteo.playerbounty.utils.GetValues;
import net.matteo.playerbounty.compats.*;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.api.distmarker.Dist;

@EventBusSubscriber(Dist.DEDICATED_SERVER)
public class PlayerDeathEvent {

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        Entity source = event.getSource().getEntity();

        if (!(source instanceof ServerPlayer killer)) return;
        if (!(entity instanceof ServerPlayer target)) return;

        if (killer == target) return;
        if (!target.gameMode.isSurvival() || !killer.gameMode.isSurvival()) return;

        if (System.get()) {
            handleKiller(killer, target);
            handleTarget(target);
        }

        if (GetValues.numismatic_overhaul_system) {
            NumismaticOverhaul.handleKiller(killer, target);
            NumismaticOverhaul.handleVictim(target);
        }

        if (GetValues.create_numismatics_system) CreateNumismatics.deathEvent(killer, target);
    }

    public static void handleKiller(ServerPlayer killer, ServerPlayer target) {
        double killerBounty = killer.getPersistentData().getDouble("bounty");

        killerBounty += (Gain.get() + randomGain()) + (killerBounty * (randomGainMultiplier() + KillerMultiplier.get())
                + (target.getPersistentData().getDouble("bounty") * ClaimMultiplier.get()));


        killer.getPersistentData().putDouble("bounty", killerBounty);
        GetValues.playerbounty.put(killer.getUUID(), (int) killerBounty);
    }

    public static void handleTarget(ServerPlayer target) {
        double targetBounty = target.getPersistentData().getDouble("bounty");

        if (LoseCompleteBountyOnDeath.get()) {
            targetBounty = (-Loss.get() - randomLoss()) - (targetBounty * (TargetMultiplier.get() + randomLossMultiplier()));
        } else {
            targetBounty -= (Loss.get() + randomLoss()) + (targetBounty * (TargetMultiplier.get() + randomLossMultiplier()));
        }

        target.getPersistentData().putDouble("bounty", targetBounty);
        GetValues.playerbounty.put(target.getUUID(), (int) targetBounty);
    }
}