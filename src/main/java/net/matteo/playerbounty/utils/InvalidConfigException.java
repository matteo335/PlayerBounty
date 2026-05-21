package net.matteo.playerbounty.utils;

import net.matteo.playerbounty.configs.Config;
import net.matteo.playerbounty.configs.SGEconomyConfig;
import net.matteo.playerbounty.PlayerBountyMod;

public class InvalidConfigException extends Exception {
    public InvalidConfigException(String string) {
        super(string);
    }

    public static void CheckInvalidConfigs() throws InvalidConfigException {
        String string = "We have found some issues in the PlayerBounty configuration files";
        boolean trigger = false;

        if (Config.RandomGainMin.get() > Config.RandomGainMax.get()) {
            string += "\nMinimum Random Gain is GREATER than Maximum Random Gain\n";
            trigger = true;
        }

        if (Config.RandomLossMin.get() > Config.RandomLossMax.get()) {
            string += "\nMinimum Random Loss is GREATER than Maximum Random Loss\n";
            trigger = true;
        }

        if (Config.RandomGainMultiplierMin.get() > Config.RandomGainMultiplierMax.get()) {
            string += "\nRandom Gain Multiplier Min is GREATER than Random Gain Multiplier Max\n";
            trigger = true;
        }

        if (Config.RandomLossMultiplierMin.get() > Config.RandomGainMultiplierMax.get()) {
            string += "\nRandom Loss Multiplier Min is GREATER than Random Loss Multiplier Max\n";
            trigger = true;
        }

        if (PlayerBountyMod.sg_economy_system) {
            if (SGEconomyConfig.RandomGainMin.get() > SGEconomyConfig.RandomGainMax.get()) {
                string += "\nMinimum Random Coin Gain is GREATER than Maximum Random Coin Gain\n";
                trigger = true;
            }

            if (SGEconomyConfig.RandomLossMin.get() > SGEconomyConfig.RandomLossMax.get()) {
                string += "\nMinimum Random Coin Loss is GREATER than Maximum Random Coin Loss\n";
                trigger = true;
            }

            if (SGEconomyConfig.RandomGainMultiplierMin.get() > SGEconomyConfig.RandomGainMultiplierMax.get()) {
                string += "\nMinimum Random Coin Gain Multiplier is GREATER than Maximum Random Coin Gain Multiplier\n";
                trigger = true;
            }

            if (SGEconomyConfig.RandomLossMultiplierMin.get() > SGEconomyConfig.RandomLossMultiplierMax.get()) {
                string += "\nMinimum Random Coin Loss Multiplier is GREATER than Maximum Random Coin Loss Multiplier\n";
                trigger = true;
            }
        }

        if (!trigger) return;
        throw new InvalidConfigException(string);
    }
}
