package net.matteo.playerbounty.events;


import net.matteo.playerbounty.old.DisplayEvents;
import net.matteo.playerbounty.utils.NullifyValues;
import net.matteo.playerbounty.configs.SGEconomyConfig;
import net.matteo.playerbounty.configs.Config;
import net.matteo.playerbounty.compats.SG_Economy;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.ModList;

@EventBusSubscriber(Dist.DEDICATED_SERVER)
public class PlayerDeathEvent {

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        Entity source = event.getSource().getEntity();

        if (!(entity instanceof ServerPlayer killer)) return;
        if (!(source instanceof ServerPlayer target)) return;

        if (killer == target) return;
        if (!target.gameMode.isSurvival() || !killer.gameMode.isSurvival()) return;

        handleKillerMath(killer, target);
        handleTargetMath(target, killer);
    }

    public static void handleKillerMath(ServerPlayer killer, ServerPlayer target) {
        if (Config.DefaultSystem.get()) {
            double killerBounty = killer.getPersistentData().getDouble("bounty");

            double randomGain = NullifyValues.randomGain();
            double randomGainMultiplier = NullifyValues.randomGainMultiplier();

            killerBounty += (Config.GainOnKilling.get() + randomGain + (killerBounty * Config.KillerMultiplier.get()) + (killerBounty * Config.ClaimMultiplier.get() + randomGainMultiplier));
            killer.getPersistentData().putDouble("bounty", killerBounty);
        }

        if (ModList.get().isLoaded("sg_economy") && SGEconomyConfig.CoinsSystem.get()) {
            SG_Economy.handleKillerEconomy(killer, target);
        }


        DisplayEvents.onTracking(new PlayerEvent.StartTracking(killer, killer));
    }

    public static void handleTargetMath(ServerPlayer target, ServerPlayer killer) {

    }

}