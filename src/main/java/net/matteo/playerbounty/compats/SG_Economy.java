package net.matteo.playerbounty.compats;

import net.matteo.playerbounty.utils.GetValues;
import net.matteo.playerbounty.configs.SGEconomyConfig;

import net.sirgrantd.sg_economy.api.SGEconomyApi;

import net.minecraft.world.entity.player.Player;

public class SG_Economy {

    public static void handleKillerEconomy(Player killer, Player target) {
        double killerCoins = SGEconomyApi.get().getBalance(killer);

        killerCoins +=
                (SGEconomyConfig.GainCoins.get() + GetValues.randomCoinsGain())
                + (killerCoins * (SGEconomyConfig.KillerMultiplier.get() + GetValues.randomCoinsGainMultiplier()))
                + (SGEconomyApi.get().getBalance(target) * SGEconomyConfig.ClaimMultiplier.get());

        SGEconomyApi.get().setBalance(killer, killerCoins);
    }

    public static void handleTargetEconomy(Player target) {
        double targetCoins = SGEconomyApi.get().getBalance(target);

        targetCoins -=
                (SGEconomyConfig.LossCoins.get() + GetValues.randomCoinsLoss())
                + (targetCoins * SGEconomyConfig.TargetMultiplier.get() + GetValues.randomCoinsLossMultiplier());

        SGEconomyApi.get().setBalance(target, targetCoins);
    }
}
