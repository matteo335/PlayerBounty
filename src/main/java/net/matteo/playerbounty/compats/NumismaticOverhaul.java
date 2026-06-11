package net.matteo.playerbounty.compats;

import static net.matteo.playerbounty.configs.NumismaticOverhaulConfig.*;
import static net.matteo.playerbounty.utils.mods.NumismaticOverhaulUtils.*;

import tallestred.numismaticoverhaul.cap.CurrencyHolder;
import static tallestred.numismaticoverhaul.cap.CurrencyHolderAttacher.EXAMPLE_CAPABILITY;

import net.minecraft.world.entity.player.Player;

public class NumismaticOverhaul {

    public static void handleKiller(Player killer, Player target) {
        double killerCoins = (double) killer.getCapability(EXAMPLE_CAPABILITY).map(CurrencyHolder::getValue).orElse(0L);
        final long result = (long) (killerCoins + (Gain.get() + randomGain())
                + (killerCoins * (randomGainMultiplier() + KillerMultiplier.get()))
                + ((double) target.getCapability(EXAMPLE_CAPABILITY).map(CurrencyHolder::getValue).orElse(0L) * ClaimMultiplier.get()));

        killer.getCapability(EXAMPLE_CAPABILITY).ifPresent(CurrencyHolder -> CurrencyHolder.setValue(result));
    }

    public static void handleVictim(Player target) {
        double targetCoins = (double) target.getCapability(EXAMPLE_CAPABILITY).map(CurrencyHolder::getValue).orElse(0L);

        if (CompleteLoss.get()) {
            final long result = (long) ((-Loss.get() - randomLoss()) - (targetCoins * (TargetMultiplier.get() + randomLossMultiplier())));
            target.getCapability(EXAMPLE_CAPABILITY).ifPresent(CurrencyHolder -> CurrencyHolder.setValue(result));
        } else {
            final long result = (long) (targetCoins - (Loss.get() + randomLoss()) + (targetCoins * (TargetMultiplier.get() + randomLossMultiplier())));
            target.getCapability(EXAMPLE_CAPABILITY).ifPresent(CurrencyHolder -> CurrencyHolder.setValue(result));
        }
    }
}
