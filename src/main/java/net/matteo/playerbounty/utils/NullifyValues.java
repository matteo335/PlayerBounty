package net.matteo.playerbounty.utils;

import net.matteo.playerbounty.configs.Config;
import net.matteo.playerbounty.configs.MagicCoinsConfig;

import net.minecraft.util.RandomSource;

public class NullifyValues {

    public static Double randomGain() {
        if (Config.RandomGainMin.get().equals(Config.RandomGainMax.get())) {
            return Config.RandomGainMin.get();
        } else {
            return RandomSource.create().nextDouble() * (Config.RandomGainMax.get() - Config.RandomGainMin.get()) + Config.RandomGainMin.get();
        }
    }

    public static Double randomLoss() {
        if (Config.RandomLossMin.get().equals(Config.RandomLossMin.get())) {
            return Config.RandomLossMin.get();
        } else {
            return RandomSource.create().nextDouble() * (Config.RandomLossMax.get() - Config.RandomLossMin.get()) + Config.RandomLossMin.get();
        }
    }

    public static Double randomGainMultiplier() {
        if (Config.RandomGainMultiplierMax.get().equals(Config.RandomGainMultiplierMin.get())) {
            return Config.RandomGainMultiplierMin.get();
        } else {
            return RandomSource.create().nextDouble() * (Config.RandomGainMultiplierMax.get() - Config.RandomGainMultiplierMin.get()) + Config.RandomGainMultiplierMin.get();
        }
    }

    public static Double randomGainLossMultiplier() {
        if (Config.RandomLossMultiplierMin.get().equals(Config.RandomLossMultiplierMax.get())) {
            return Config.RandomLossMultiplierMin.get();
        } else {
            return RandomSource.create().nextDouble() * (Config.RandomLossMultiplierMax.get() - Config.RandomLossMultiplierMin.get()) + Config.RandomLossMin.get();
        }
    }

    public static Double randomCoinsGain() {
        if (MagicCoinsConfig.RandomGainCoinsMin.get().equals(MagicCoinsConfig.RandomGainCoinsMax.get())) {
            return MagicCoinsConfig.RandomGainCoinsMin.get();
        } else {
            return RandomSource.create().nextDouble() * (MagicCoinsConfig.RandomGainCoinsMax.get() - MagicCoinsConfig.RandomGainCoinsMin.get()) + MagicCoinsConfig.RandomGainCoinsMin.get();
        }
    }
}
