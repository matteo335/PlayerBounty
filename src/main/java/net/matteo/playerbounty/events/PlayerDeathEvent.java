package net.matteo.playerbounty.events;

import net.matteo.playerbounty.PlayerBountyMod;
import net.matteo.playerbounty.configs.MagicCoinsConfig;
import net.matteo.playerbounty.internal.RulesGame;
import net.matteo.playerbounty.internal.provider.BountyHunterProvider;

import net.matteo.playerbounty.old.DisplayEvents;
import net.matteo.playerbounty.old.NetworkDisplay;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.sirgrantd.sg_economy.api.SGEconomyApi;


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
        double killerCoins = SGEconomyApi.get().getBalanceAsInt(killer);

        boolean nullifyRandomGain = MagicCoinsConfig.RandomGainCoinsMin.get().equals(MagicCoinsConfig.RandomGainCoinsMax.get());
        boolean nullifyRandomLoss = MagicCoinsConfig.RandomLossCoinsMin.get().equals(MagicCoinsConfig.RandomLossCoinsMax.get());

        boolean nullifyRandomGainMultiplier = MagicCoinsConfig.RandomGainCoinsMultiplierMin.get().equals(MagicCoinsConfig.RandomGainCoinsMultiplierMax.get());
        boolean nullifyRandomLossMultiplier = MagicCoinsConfig.RandomLossCoinsMultiplierMin.get().equals(MagicCoinsConfig.RandomLossCoinsMultiplierMax.get());

        //double randomGainMultiplier;
        //double randomGain;

        if (nullifyRandomGain) {
            double randomGain = MagicCoinsConfig.RandomGainCoinsMin.get();
        } else {
            double randomGain = (RandomSource.create().nextDouble() * (MagicCoinsConfig.RandomGainCoinsMax.get() - MagicCoinsConfig.RandomGainCoinsMin.get()) + MagicCoinsConfig.RandomGainCoinsMin.get());
        }

        if (nullifyRandomGainMultiplier) {
            double randomGainMultiplier = MagicCoinsConfig.RandomGainCoinsMultiplierMin.get();
        } else {
            double randomGainMultiplier = (RandomSource.create().nextDouble() * (BountyConfig.RandomGainCoinsMultiplierMax.get() - BountyConfig.RandomGainCoinsMultiplierMin.get()) + BountyConfig.RandomGainCoinsMultiplierMin.get());
        }

        killerCoins = killerCoins + BountyConfig.GainCoins.get() + randomGain + (MagicCoinsApi.getTotalCoins(killer) * (BountyConfig.MultiplierSelfCoins.get() + randomGainMultiplier)) + (MagicCoinsApi.getTotalCoins(target) * (BountyConfig.MultiplierOfCoinsStealing.get() + randomGainMultiplier));

        SGEconomyApi.get().setBalance(killer, (int) killerCoins);
        DisplayEvents.onTracking(new PlayerEvent.StartTracking(killer, killer));
    }

    private static void handleTargetMath(ServerPlayer target, ServerPlayer killer) {

    }
}