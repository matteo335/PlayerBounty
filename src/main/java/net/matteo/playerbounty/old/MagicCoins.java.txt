package matteo.PlayerBounty.compats;

import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;

import matteo.PlayerBounty.BountyConfig;
import matteo.PlayerBounty.DisplayEvents;

import net.sirgrantd.magic_coins.api.MagicCoinsApi;

public class MagicCoins {

    public static void Death(LivingDeathEvent death) {
        ServerPlayer killer = (ServerPlayer) death.getSource().getEntity();
        ServerPlayer target = (ServerPlayer) death.getEntity();

        //KILLER
        double killerCoins = MagicCoinsApi.getTotalCoins(killer);
        boolean nullifyRandomGain = BountyConfig.RandomGainCoinsMin.get().equals(BountyConfig.RandomGainCoinsMin.getDefault()) && BountyConfig.RandomGainCoinsMax.get().equals(BountyConfig.RandomGainMax.getDefault());
        boolean nullifyRandomGainMultiplier = BountyConfig.RandomGainMultiplierMin.get().equals(BountyConfig.RandomGainMultiplierMin.getDefault()) && BountyConfig.RandomGainCoinsMultiplierMax.get().equals(BountyConfig.RandomGainCoinsMultiplierMax.getDefault());
        double randomGainMultiplier = 0;
        double randomGain = 0;

        if (!nullifyRandomGain) {
            randomGain = (RandomSource.create().nextDouble() * (BountyConfig.RandomGainCoinsMax.get() - BountyConfig.RandomGainCoinsMin.get()) + BountyConfig.RandomGainCoinsMin.get());
        }
        if (!nullifyRandomGainMultiplier) {
            randomGainMultiplier = (RandomSource.create().nextDouble() * (BountyConfig.RandomGainCoinsMultiplierMax.get() - BountyConfig.RandomGainCoinsMultiplierMin.get()) + BountyConfig.RandomGainCoinsMultiplierMin.get());
        }

        killerCoins = killerCoins + BountyConfig.GainCoins.get() + randomGain + (MagicCoinsApi.getTotalCoins(killer) * (BountyConfig.MultiplierSelfCoins.get() + randomGainMultiplier)) + (MagicCoinsApi.getTotalCoins(target) * (BountyConfig.MultiplierOfCoinsStealing.get() + randomGainMultiplier));

        MagicCoinsApi.setTotalCoins(killer, (int) killerCoins);
        DisplayEvents.onTracking(new PlayerEvent.StartTracking(killer, killer));

        //TARGET
        double targetCoins = MagicCoinsApi.getTotalCoins(target);
        boolean nullifyRandomLoss = BountyConfig.RandomLossCoinsMin.get().equals(BountyConfig.RandomLossCoinsMin.getDefault()) && BountyConfig.RandomLossCoinsMax.get().equals(BountyConfig.RandomLossCoinsMax.getDefault());
        boolean nullifyRandomLossMultiplier = BountyConfig.RandomLossCoinsMultiplierMin.get().equals(BountyConfig.RandomLossCoinsMultiplierMin.getDefault()) && BountyConfig.RandomLossCoinsMultiplierMax.get().equals(BountyConfig.RandomLossCoinsMultiplierMax.getDefault());
        double randomLoss = 0;
        double randomLossMultiplier = 0;

        if (!nullifyRandomLoss) {
            randomLoss = (RandomSource.create().nextDouble() * (BountyConfig.RandomLossCoinsMax.get() - BountyConfig.RandomLossCoinsMin.get()) + BountyConfig.RandomGainCoinsMin.get());
        }
        if (!nullifyRandomLossMultiplier) {
            randomLossMultiplier = (RandomSource.create().nextDouble() * (BountyConfig.RandomLossCoinsMultiplierMax.get() - BountyConfig.RandomLossCoinsMultiplierMin.get()) - BountyConfig.RandomLossCoinsMultiplierMin.get());
        }

        targetCoins = targetCoins - BountyConfig.LossCoins.get() + randomLoss + (BountyConfig.MultiplierOfCoinsLoss.get() + randomLossMultiplier);
        if (targetCoins < 0) {
            target.displayClientMessage(Component.literal("<PlayerBounty & MagicCoins compat>: The base MagicCoins mod doesn't support negative coins, the result will likely be 0."), false);
        }

        MagicCoinsApi.setTotalCoins(target, (int) targetCoins);
        DisplayEvents.onTracking(new PlayerEvent.StartTracking(target, target));
    }
}
