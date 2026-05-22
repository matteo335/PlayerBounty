package net.matteo.playerbounty.utils;

import net.matteo.playerbounty.configs.*;

public class InvalidConfigException extends Exception {
    public InvalidConfigException(String string) {
        super(string);
    }

    public static void CheckInvalidConfigs() throws InvalidConfigException {
        String string = "We have found some issues in the PlayerBounty configuration files";
        boolean trigger = false;

        if (Config.System.get()) {
            if (Config.RandomGainMin.get() > Config.RandomGainMax.get()) {
                string += "\nPlayerBounty: Minimum Random Gain is greater than Maximum Random Gain\n";
                trigger = true;
            }

            if (Config.RandomLossMin.get() > Config.RandomLossMax.get()) {
                string += "\nPlayerBounty: Minimum Random Loss is greater than Maximum Random Loss\n";
                trigger = true;
            }

            if (Config.RandomGainMultiplierMin.get() > Config.RandomGainMultiplierMax.get()) {
                string += "\nPlayerBounty: Random Gain Multiplier Min is greater than Random Gain Multiplier Max\n";
                trigger = true;
            }

            if (Config.RandomLossMultiplierMin.get() > Config.RandomGainMultiplierMax.get()) {
                string += "\nPlayerBounty: Random Loss Multiplier Min is greater than Random Loss Multiplier Max\n";
                trigger = true;
            }
        }

        if (GetValues.sg_economy_system) {
            if (SGEconomyConfig.RandomGainMin.get() > SGEconomyConfig.RandomGainMax.get()) {
                string += "\nSG_Economy Compat: Minimum Random Gain is greater than Maximum Random Gain\n";
                trigger = true;
            }

            if (SGEconomyConfig.RandomLossMin.get() > SGEconomyConfig.RandomLossMax.get()) {
                string += "\nSG_Economy Compat: Minimum Random is greater than Maximum Random Loss\n";
                trigger = true;
            }

            if (SGEconomyConfig.RandomGainMultiplierMin.get() > SGEconomyConfig.RandomGainMultiplierMax.get()) {
                string += "\nSG_Economy Compat: Minimum Random Gain Multiplier is greater than Maximum Random Gain Multiplier\n";
                trigger = true;
            }

            if (SGEconomyConfig.RandomLossMultiplierMin.get() > SGEconomyConfig.RandomLossMultiplierMax.get()) {
                string += "\nSG_Economy Compat: Minimum Random Loss Multiplier is greater than Maximum Random Loss Multiplier\n";
                trigger = true;
            }
        }

        if (GetValues.numismaticoverhaul_system) {
            if (NumismaticOverhaulConfig.RandomGainMin.get() > NumismaticOverhaulConfig.RandomGainMax.get()) {
                string += "\nNumismaticOverhaul Compat: Minimum Random Gain is greater than Maximum Random Gain\n";
                trigger = true;
            }

            if (NumismaticOverhaulConfig.RandomLossMin.get() > NumismaticOverhaulConfig.RandomLossMax.get()) {
                string += "\nNumismaticOverhaul Compat: Minimum Random Loss is greater than Maximum Random Loss\n";
                trigger = true;
            }

            if (NumismaticOverhaulConfig.RandomGainMultiplierMin.get() > NumismaticOverhaulConfig.RandomGainMultiplierMax.get()) {
                string += "\nNumismaticOverhaul Compat: Minimum Random Gain Multiplier is greater than Maximum Random Gain Multiplier\n";
                trigger = true;
            }

            if (NumismaticOverhaulConfig.RandomLossMultiplierMin.get() > NumismaticOverhaulConfig.RandomLossMultiplierMax.get()) {
                string += "\nNumismaticOverhaul Compat: Minimum Random Loss Multiplier is greater than Maximum Random Loss Multiplier\n";
                trigger = true;
            }
        }

        if (!trigger) return;
        throw new InvalidConfigException(string);
    }
}
