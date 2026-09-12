package net.matteo.playerbounty.compats;

import net.matteo.playerbounty.utils.mods.SGEconomyUtils;
import net.matteo.playerbounty.configs.SGEconomyConfig;
import net.matteo.playerbounty.utils.GetValues;

import net.sirgrantd.sg_economy.api.SGEconomyApi;

import net.minecraft.world.entity.player.Player;

public class SG_Economy {

    public static void handleKiller(Player killer, Player target) {
        double killerCoins = SGEconomyApi.getBalance(killer);

        killerCoins +=
                (SGEconomyConfig.Gain.get() + SGEconomyUtils.randomGain())
                + (killerCoins * (SGEconomyConfig.KillerMultiplier.get() + SGEconomyUtils.randomGainMultiplier()))
                + (SGEconomyApi.getBalance(target) * SGEconomyConfig.ClaimMultiplier.get());

        SGEconomyApi.setBalance(killer, killerCoins);
        GetValues.sg_economy.put(killer.getUUID(), (int) killerCoins);
    }

    public static void handleTarget(Player target) {
        double targetCoins = SGEconomyApi.getBalance(target);

        targetCoins -=
                (SGEconomyConfig.Loss.get() + SGEconomyUtils.randomLoss())
                + (targetCoins * SGEconomyConfig.TargetMultiplier.get() + SGEconomyUtils.randomLossMultiplier());

        SGEconomyApi.setBalance(target, targetCoins);
        GetValues.sg_economy.put(target.getUUID(), (int) targetCoins);
    }
}
