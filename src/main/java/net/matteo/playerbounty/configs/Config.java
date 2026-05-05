package net.matteo.playerbounty.configs;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {

    public static ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

    public static ModConfigSpec.BooleanValue StartupWarning;

    public static ModConfigSpec.BooleanValue DefaultSystem;

    public static ModConfigSpec.BooleanValue IsPlayerBountyDisplayEnabled;
    public static ModConfigSpec.BooleanValue DisableDisplay;
    public static ModConfigSpec.ConfigValue<String> BountyDisplay1;
    public static ModConfigSpec.ConfigValue<String> BountyDisplay2;

    public static ModConfigSpec.BooleanValue LoseCompleteBountyOnDeath;

    public static ModConfigSpec.DoubleValue BountyMinimumValue;
    public static ModConfigSpec.DoubleValue BountyMaximumValue;

    public static ModConfigSpec.ConfigValue<Double> GainOnKilling;
    public static ModConfigSpec.ConfigValue<Double> LossOnDeath;

    public static ModConfigSpec.DoubleValue KillerMultiplier;
    public static ModConfigSpec.DoubleValue ClaimMultiplier;
    public static ModConfigSpec.DoubleValue TargetMultiplier;


    public static ModConfigSpec.DoubleValue RandomGainMin;
    public static ModConfigSpec.DoubleValue RandomGainMax;
    public static ModConfigSpec.DoubleValue RandomLossMin;
    public static ModConfigSpec.DoubleValue RandomLossMax;

    public static ModConfigSpec.DoubleValue RandomGainMultiplierMin;
    public static ModConfigSpec.DoubleValue RandomGainMultiplierMax;
    public static ModConfigSpec.DoubleValue RandomLossMultiplierMin;
    public static ModConfigSpec.DoubleValue RandomLossMultiplierMax;

    static {
        builder.comment("""
                 Note that you can use + and - but you CANNOT use %
                 Make sure you do the calculations correctly, some values might do the complete opposite in some scenarios.
                
                 Target is calculated like this: your Bounty * (Target-Multiplier + RandomLossMultiplier) - (LossOnDeath + RandomLoss)
                
                 If LossCompleteBountyOnDeath is true: your Bounty = (-RandomLoss - LossOnDeath) + (Bounty * (Target-Multiplier + RandomLossMultiplier)) - Bounty
                
                 Killer is calculated like this: your Bounty = (BountyTarget * Claim-Multiplier) + (BountyKiller * (Killer-Multiplier + RandomGainMultiplier)) + (GainOnKilling + RandomLoss)
                """);

        StartupWarning = builder.define("Enable the warning in the server start", true);

        DefaultSystem = builder.define("Enable the default mechanics, disable it if you prefer the compats", true);

        IsPlayerBountyDisplayEnabled = builder.define("Display for the default system", true);
        DisableDisplay = builder.define("Disable the display", false);
        BountyDisplay1 = builder.define("Formatting Codes Before Bounty", " [$§6§l");
        BountyDisplay2 = builder.define("Formatting Codes After Bounty", "§r]");

        LoseCompleteBountyOnDeath = builder.define("Lose Complete Bounty On Death", false);
        BountyMinimumValue = builder.defineInRange("Bounty Minimum Value", -Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE);
        BountyMaximumValue = builder.defineInRange("Bounty Maximum Value", Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE);

        GainOnKilling = builder.define("Bounty Gain On Killing", 10.0);
        LossOnDeath = builder.define("Bounty Loss On Death", 10.0);

        KillerMultiplier = builder.defineInRange("Killer-Multiplier of your own bounty 1 = 100%", 1.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        ClaimMultiplier = builder.defineInRange("Claim-Multiplier, how much you take from your victim 1 = 100%", 0.5, -Double.MAX_VALUE, Double.MAX_VALUE);
        TargetMultiplier = builder.defineInRange("Target-Multiplier, how much do the victim loss 1 = 100%", 1.0, -Double.MAX_VALUE, Double.MAX_VALUE);

        RandomGainMin = builder.defineInRange("Minimum Random Gain", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomGainMax = builder.defineInRange("Maximum Random Gain", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomLossMin = builder.defineInRange("Minimum Random Loss", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomLossMax = builder.defineInRange("Maximum Random Loss", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);

        RandomGainMultiplierMin = builder.defineInRange("Random Gain Multiplier Min 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomGainMultiplierMax = builder.defineInRange("Random Gain Multiplier Max 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomLossMultiplierMin = builder.defineInRange("Random Loss Multiplier Min 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomLossMultiplierMax = builder.defineInRange("Random Loss Multiplier Max 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
    }
}
