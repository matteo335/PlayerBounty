package net.matteo.playerbounty.configs;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {

    public static ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

    public static ModConfigSpec.BooleanValue StartupWarning;

    public static ModConfigSpec.BooleanValue DefaultSystem;

    public static ModConfigSpec.BooleanValue EnableDisplay;
    public static ModConfigSpec.ConfigValue<Integer> DisplayCooldown;

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
                 Make sure you do the calculations correctly, you can use the link below to do the math yourself.
                
                 Killer is calculated like this: Your Bounty + (Gain On Killing + Random Gain) + (Your Bounty * (Killer-Multiplier + Random Gain Multiplier)) + (Target Bounty * Claim-Multiplier)
                 
                 Target is calculated like this: Your Bounty - (Loss On Death + Random Loss) + (Your Bounty * (Target-Multiplier + Random Loss Multiplier)
                
                 If LossCompleteBountyOnDeath is true: Your Bounty = (-LossOnDeath - randomLoss) + (Bounty * (Target-Multiplier + RandomLossMultiplier))
                 
                 https://onlinegdb.com/BCyy-0Pi-Q
                 """);

        StartupWarning = builder.define("Enable the warning in the server start", true);

        DefaultSystem = builder.define("Enable the default mechanics", true);

        EnableDisplay = builder.define("Enable the display", true);
        DisplayCooldown = builder.define("How much ticks before displays get updated", 100);

        BountyDisplay1 = builder.define("Formatting Codes Before Bounty", " [$§6§l");
        BountyDisplay2 = builder.define("Formatting Codes After Bounty", "§r]");

        LoseCompleteBountyOnDeath = builder.define("Lose Complete Bounty On Death", false);
        BountyMinimumValue = builder.defineInRange("Bounty Minimum Value", -Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE);
        BountyMaximumValue = builder.defineInRange("Bounty Maximum Value", Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE);

        GainOnKilling = builder.define("Bounty Gain On Killing", 10.0);
        LossOnDeath = builder.define("Bounty Loss On Death", 10.0);

        KillerMultiplier = builder.defineInRange("Killer-Multiplier of your own bounty 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        ClaimMultiplier = builder.defineInRange("Claim-Multiplier, how much you take from your victim 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        TargetMultiplier = builder.defineInRange("Target-Multiplier, how much do the victim loss 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);

        RandomGainMin = builder.defineInRange("Minimum Random Gain", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomGainMax = builder.defineInRange("Maximum Random Gain", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomLossMin = builder.defineInRange("Minimum Random Loss", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomLossMax = builder.defineInRange("Maximum Random Loss", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);

        RandomGainMultiplierMin = builder.defineInRange("Random Killer Multiplier Min 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomGainMultiplierMax = builder.defineInRange("Random Killer Multiplier Max 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomLossMultiplierMin = builder.defineInRange("Random Target Multiplier Min 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
        RandomLossMultiplierMax = builder.defineInRange("Random Target Multiplier Max 1 = 100%", 0.0, -Double.MAX_VALUE, Double.MAX_VALUE);
    }
}
