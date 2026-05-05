package net.matteo.playerbounty.utils;

import net.matteo.playerbounty.configs.Config;
import net.matteo.playerbounty.configs.SGEconomyConfig;

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

    public static Double randomLossMultiplier() {
        if (Config.RandomLossMultiplierMin.get().equals(Config.RandomLossMultiplierMax.get())) {
            return Config.RandomLossMultiplierMin.get();
        } else {
            return RandomSource.create().nextDouble() * (Config.RandomLossMultiplierMax.get() - Config.RandomLossMultiplierMin.get()) + Config.RandomLossMin.get();
        }
    }

    public static Double randomCoinsGain() {
        if (SGEconomyConfig.RandomGainCoinsMin.get().equals(SGEconomyConfig.RandomGainCoinsMax.get())) {
            return SGEconomyConfig.RandomGainCoinsMin.get();
        } else {
            return RandomSource.create().nextDouble() * (SGEconomyConfig.RandomGainCoinsMax.get() - SGEconomyConfig.RandomGainCoinsMin.get()) + SGEconomyConfig.RandomGainCoinsMin.get();
        }
    }

    public static Double randomCoinsLoss() {
        if (SGEconomyConfig.RandomLossCoinsMin.get().equals(SGEconomyConfig.RandomLossCoinsMax.get())) {
            return SGEconomyConfig.RandomLossCoinsMin.get();
        } else {
            return RandomSource.create().nextDouble() * (SGEconomyConfig.RandomLossCoinsMax.get() - SGEconomyConfig.RandomLossCoinsMin.get()) + SGEconomyConfig.RandomLossCoinsMin.get();
        }
    }

    public static Double randomCoinsGainMultiplier() {
        if (SGEconomyConfig.RandomGainCoinsMultiplierMin.get().equals(SGEconomyConfig.RandomGainCoinsMultiplierMax.get())) {
            return SGEconomyConfig.RandomGainCoinsMin.get();
        } else {
            return RandomSource.create().nextDouble() * (SGEconomyConfig.RandomGainCoinsMultiplierMax.get() - SGEconomyConfig.RandomGainCoinsMultiplierMin.get()) + SGEconomyConfig.RandomGainCoinsMultiplierMin.get();
        }
    }

    public static Double randomCoinsLossMultiplier() {
        if (SGEconomyConfig.RandomLossCoinsMultiplierMin.get().equals(SGEconomyConfig.RandomLossCoinsMultiplierMax.get())) {
            return SGEconomyConfig.RandomLossCoinsMultiplierMin.get();
        } else {
            return RandomSource.create().nextDouble() * (SGEconomyConfig.RandomLossCoinsMultiplierMax.get() - SGEconomyConfig.RandomLossCoinsMultiplierMin.get()) + SGEconomyConfig.RandomLossCoinsMultiplierMin.get();
        }
    }
}
