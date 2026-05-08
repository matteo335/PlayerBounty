package net.matteo.playerbounty.configs;

import net.neoforged.neoforge.common.ModConfigSpec;

public class SGEconomyConfig {

    public static ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

    public static ModConfigSpec.BooleanValue CoinsSystem;

    public static ModConfigSpec.BooleanValue EnableDisplay;

    public static ModConfigSpec.ConfigValue<String> CoinsDisplay1;
    public static ModConfigSpec.ConfigValue<String> CoinsDisplay2;

    public static ModConfigSpec.ConfigValue<Double> GainCoins;
    public static ModConfigSpec.ConfigValue<Double> LossCoins;

    public static ModConfigSpec.DoubleValue ClaimMultiplier;
    public static ModConfigSpec.DoubleValue TargetMultiplier;
    public static ModConfigSpec.DoubleValue KillerMultiplier;

    public static ModConfigSpec.DoubleValue RandomGainCoinsMin;
    public static ModConfigSpec.DoubleValue RandomGainCoinsMax;
    public static ModConfigSpec.DoubleValue RandomLossCoinsMin;
    public static ModConfigSpec.DoubleValue RandomLossCoinsMax;

    public static ModConfigSpec.DoubleValue RandomGainCoinsMultiplierMin;
    public static ModConfigSpec.DoubleValue RandomGainCoinsMultiplierMax;
    public static ModConfigSpec.DoubleValue RandomLossCoinsMultiplierMin;
    public static ModConfigSpec.DoubleValue RandomLossCoinsMultiplierMax;

    static {
        builder.comment("""
                Make sure you do the calculations correctly, you can use the link below to do the math yourself.
                
                Target is calculated like this: your Bounty * (Target-Multiplier + RandomLossMultiplier) - (LossOnDeath + RandomLoss)
                
                Killer is calculated like this: your Bounty = (BountyTarget * Claim-Multiplier) + (BountyKiller * (Killer-Multiplier + RandomGainMultiplier)) + (GainOnKilling + RandomLoss)
                
                There is no LossCompleteBounty in this config, because it is already present in the SGEconomy-API config.
                
                 https://onlinegdb.com/BCyy-0Pi-Q
                """);

        CoinsSystem = builder.define("Enable the SG Economy compat", false);
        EnableDisplay = builder.define("Display the balance in the player name", true);

        CoinsDisplay1 = builder.define("Formatting Codes before the coin display", " [$§d§l");
        CoinsDisplay2 = builder.define("Formatting Codes after the coin display", "§r]");

        GainCoins = builder.define("How much coins you gain after killing another player", 10.0);
        LossCoins = builder.define("How much coins you loss after being killed", 10.0);

        ClaimMultiplier = builder.defineInRange("Claim-Multiplier, how much you take from your victim = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        KillerMultiplier = builder.defineInRange("Killer-Multiplier of your own balance 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        TargetMultiplier = builder.defineInRange("Target-Multiplier, how much do the victim loss 1 = 100", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);

        RandomGainCoinsMin = builder.defineInRange("Minimum Random Coin Gain", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomGainCoinsMax = builder.defineInRange("Maximum Random Coin Gain", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomLossCoinsMin = builder.defineInRange("Minimum Random Coin Loss", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomLossCoinsMax = builder.defineInRange("Maximum Random Coin Loss", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);

        RandomGainCoinsMultiplierMin = builder.defineInRange("Minimum Random Coin Gain Multiplier 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomGainCoinsMultiplierMax = builder.defineInRange("Maximum Random Coin Gain Multiplier 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomLossCoinsMultiplierMin = builder.defineInRange("Minimum Random Coin Loss Multiplier 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomLossCoinsMultiplierMax = builder.defineInRange("Maximum Random Coin loss Multiplier 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
    }
}
