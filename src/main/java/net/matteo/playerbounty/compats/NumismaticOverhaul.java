package net.matteo.playerbounty.compats;

import static net.matteo.playerbounty.configs.NumismaticOverhaulConfig.*;
import static net.matteo.playerbounty.utils.mods.NumismaticOverhaulUtils.*;

import tallestred.numismaticoverhaul.cap.CurrencyHolder;

import net.minecraft.world.entity.player.Player;

public class NumismaticOverhaul {

    public static void handleKiller(Player killer, Player target) {
        double killerCoins = CurrencyHolder.getValue(killer);

        killerCoins +=
                (Gain.get() + randomGain())
                + (killerCoins * (randomGainMultiplier() + KillerMultiplier.get()))
                + (CurrencyHolder.getValue(target) * ClaimMultiplier.get());

        CurrencyHolder.setValue(killer, (long) killerCoins);
    }

    public static void handleVictim(Player target) {
        double targetCoins = CurrencyHolder.getValue(target);

        if (CompleteLoss.get()) {
            targetCoins = (-Loss.get() - randomLoss()) - (targetCoins * (TargetMultiplier.get() + randomLossMultiplier()));
        } else {
            targetCoins -= (Loss.get() + randomLoss()) + (targetCoins * (TargetMultiplier.get() + randomLossMultiplier()));
        }

        CurrencyHolder.setValue(target, (long) targetCoins);
    }
}
